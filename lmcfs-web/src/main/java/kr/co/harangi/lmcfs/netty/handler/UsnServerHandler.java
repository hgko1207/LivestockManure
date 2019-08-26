package kr.co.harangi.lmcfs.netty.handler;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.AsyncResult;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import kr.co.harangi.lmcfs.netty.annotation.Usn;
import kr.co.harangi.lmcfs.netty.exception.InvalidDataSizeException;
import kr.co.harangi.lmcfs.netty.exception.NotSupprtedMessageIdException;
import kr.co.harangi.lmcfs.netty.group.UsnMessageSenderGroup;
import kr.co.harangi.lmcfs.netty.listener.MessageListener;
import kr.co.harangi.lmcfs.netty.listener.MessageSender;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.netty.msg.common.UsnOutgoingMessage;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * USN 과 연결되는 Channel을 통해서 데이터의 read, write 를 처리한다.
 * {@link MessageSender}<MessagePacket> 은 본 채널을 통해서 전송하기 위해서 사용하는 인터페이스.
 * 
 * @author hgko
 *
 */
@Slf4j
@Sharable
public class UsnServerHandler extends SimpleChannelInboundHandler<UsnIncomingMessage> implements MessageSender {
	
	private static final int SYNC_MESSAGE_TIMEOUT_SEC = 2000;
	private static final int RETRY_COUNT = 3;
	
	@Autowired
	private UsnMessageSenderGroup messageSenderGroup;
	
	@Autowired
	private TaskExecutor executor;
	
	@Autowired
	@Usn
	@Setter
	private MessageListener listener;
	
	private ConcurrentMap<Integer, BlockingQueue<UsnIncomingMessage>> syncQueueMap = new ConcurrentHashMap<>();
	
	private Channel channel;
	
	@Setter
	private int ackWaitTimeout = SYNC_MESSAGE_TIMEOUT_SEC;
	
	@Setter
	private int maxRetryCount = RETRY_COUNT;
	
	@PostConstruct
	private void init() {
		messageSenderGroup.addMessageSender("30", this);
	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		channel = ctx.channel();
		log.debug("{} is connected", getChannelName());
		if (listener != null) {
			listener.connectionStateChanged(true);
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		log.debug("{} was disconnected", getChannelName());
		if (listener != null) {
			listener.connectionStateChanged(false);
		}
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, UsnIncomingMessage packet) throws Exception {
		
		log.info("[Incoming] {} {}", getChannelName(), packet);
		
		if (isAckMessage(packet)) {
			BlockingQueue<UsnIncomingMessage> syncQueue = syncQueueMap.remove(syncQueueKeyByAckMessage(packet));
			if (syncQueue != null) {
				syncQueue.offer(packet);
			}
		} else {
			if (listener != null) {
				listener.messageReceived(packet);
			}
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		
		if (cause instanceof IOException) {
			log.warn("{} => {}", getChannelName(), cause.getMessage());
			return;
		}

		log.error("{}", getChannelName(), cause);
		
		if (cause instanceof NotSupprtedMessageIdException
				|| cause instanceof InvalidDataSizeException) {
			ctx.channel().close();
		}
	}
	
	private String getChannelName() {
		return channel.remoteAddress().toString();
	}

	private boolean isAckMessage(UsnIncomingMessage packet)  {
		return packet.getClass().getSimpleName().endsWith("Ack");
	}
	
	private Integer syncQueueKeyByAckMessage(UsnIncomingMessage in) {
		UsnMessageHeader inHeader = (UsnMessageHeader) in.getHeader();
		return inHeader.getSeq();
	}

	 /**
     * 응답이 요구 되는 메시지의 경우 BlockingQueue를 사용하여, 
     * 응답메시지가 오기를 기다린후 응답시간 10000 msec 이 초과했을때 실패로 간주한다.
     * - 응답 메시지는 요청 메시지와 메시지 아이디가 같다.
     * - ACK가 오지 않을 경우 3회 재전송
     */
	@Override
	public boolean sendSyncMessage(UsnOutgoingMessage packet) {
		if (channel == null || !channel.isActive()) return false;
    	
    	int retryCount = 0;

    	// 3회 재전송
    	while(retryCount <= maxRetryCount) {
    		if (send(packet)) {
    			return true;
    		}
    		retryCount++;
    	}
    	return false;
	}
	
	private boolean send(UsnOutgoingMessage packet) {
		channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);

		UsnMessageHeader outHeader = (UsnMessageHeader) packet.getHeader();
		Integer key = outHeader.getSeq();
		
		try {
			BlockingQueue<UsnIncomingMessage> queue = new SynchronousQueue<UsnIncomingMessage>();
			syncQueueMap.put(key, queue);
			UsnIncomingMessage recvMessage = queue.poll(ackWaitTimeout, TimeUnit.MILLISECONDS);
			if (recvMessage != null) {
				if (checkAckMessage(packet, recvMessage)) {
					return true;
				}
			}
		} catch (InterruptedException e) {
			log.warn("{}", e.getMessage());
		}
    	
    	return false;
	}
	
	private boolean checkAckMessage(UsnOutgoingMessage out, UsnIncomingMessage in) {
		return (out.getMessageType().getId() + 0x80) == in.getMessageType().getId();
	}
	
	/**
     * 비동기로 메시지 전송 (executor의 ThreadPool 이용) 
     * Ack 메시지 사용
     */
	@Override
	public Future<Boolean> sendAsyncMessage(UsnOutgoingMessage packet) {
		if (channel == null) 
			return new AsyncResult<Boolean>(false);

		executor.execute(new Runnable() {
			@Override
			public void run() {
		    	try {
		    		channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
		    	} catch (RuntimeException e) {
		    		log.error("{}", e);
				}
			}
		});
    	
    	return new AsyncResult<Boolean>(true);
	}
	
	@Override
	public boolean isConnected() {
		if (channel != null) {
			return channel.isActive();
		} else {
			return false;
		}
	}

	@Override
	public void forceClose() {
		if (channel != null) {
			channel.close();
		}
	}
}
