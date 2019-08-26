package kr.co.harangi.lmcfs.netty.handler.common;

import java.nio.ByteOrder;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import kr.co.harangi.lmcfs.netty.exception.InvalidChecksumException;
import kr.co.harangi.lmcfs.netty.msg.common.UsnIncomingMessage;
import kr.co.harangi.lmcfs.netty.msg.common.UsnMessageHeader;
import kr.co.harangi.lmcfs.utils.PrintUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractServerDecoder extends ByteToMessageDecoder {
	
	private UsnMessageHeader header;
	
	private String inStr;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {

		inStr = PrintUtil.printReceivedChannelBuffer("in", buffer);
		log.debug("{} {}", ctx.channel(), inStr);
		
		decode(buffer.order(ByteOrder.LITTLE_ENDIAN), out);
		
		log.debug(PrintUtil.printReceivedChannelBuffer("After decoding", buffer));		
	}

	private void decode(ByteBuf buffer, List<Object> out) {

		while (buffer.isReadable()) {
			// 이전 수신 처리에서 Header 까지 완료 되었는지 확인
			if (header == null) {
				// Header를 처리하기 위한 데이터가 있는지 확인
				if (buffer.readableBytes() < requireHeaderSize()) {
					return;
				}
				
				try {
					header = makeMessageHeader(buffer);
				} catch (RuntimeException e) {
					discardBufferByFailHeader(buffer);
					log.error("{}", inStr, e);
					continue;
				}
			}
			
			// Body 를 처리하기 위한 데이터가 있는지 확인
			if (buffer.readableBytes() < header.getRequiredBodySize() + 1) {
				return;
			}
	
			try {
				out.add(makeMessageBody(buffer));
			} catch (InvalidChecksumException | ReflectiveOperationException e) {
				log.error("{}", inStr, e);
			} catch (RuntimeException e) {
				header = null;
				throw e;
			}
			// 패킷이 완성이 되면 리스트에 저장하고, 반드시 Header는 null로 초기화 해야 한다.
			header = null;
		}
	}

	public abstract int requireHeaderSize();

	/**
	 * 메시지 해더를 생성하고, 데이터를 입력한다.
	 * ex1) 현재 수신한 모든 버퍼를 삭제한다.
	 * ex2) 예외를 발생시켜 채널을 종료한다.
	 * 
	 * @param buffer
	 */
	public abstract UsnMessageHeader makeMessageHeader(ByteBuf buffer);
 
	/**
	 * 버퍼를 이용해 IncomingMessage 생성한다.
	 * 예외가 발생할 경우 size만큼 데이터를 읽어서 버린다.
	 * Checksum 비교
	 *  
	 * @param buffer
	 */
	private UsnIncomingMessage makeMessageBody(ByteBuf buffer) throws InvalidChecksumException, ReflectiveOperationException {
		UsnIncomingMessage incomingMessage;
		
		try {
			incomingMessage = header.getMessageType().getIncomingClass().getConstructor(header.getClass()).newInstance(header);
		} catch (ReflectiveOperationException e) {
			discardBufferByFailBody(buffer, header);
			throw e;
		}
		incomingMessage.decode(buffer);
		
		// ETX를 읽는다.
		buffer.readUnsignedByte();
		
		if (!processChecksum(buffer, incomingMessage)) {
			throw new InvalidChecksumException(incomingMessage.checksum());
		}
		return incomingMessage;
	}
	
	/**
	 * Header 처리시 예외가 발생 했을 경우 buffer 처리
	 * 
	 * @param buffer
	 */
	public abstract void discardBufferByFailHeader(ByteBuf buffer);
	
	/**
	 * Body 처리시 예외가 발생 했을 경우 buffer 처리
	 * ex1) 현재 수신한 모든 버퍼를 삭제한다.
	 * ex2) 예외를 발생시켜 채널을 종료한다.
	 * ex3) BodySize 를 버퍼에서 삭제한다. (뒤에 메시지를 보존하기 위해)
	 * 
	 * @param buffer
	 * @param header
	 */
	public abstract void discardBufferByFailBody(ByteBuf buffer, UsnMessageHeader header);

	/**
	 * USN 메시지가 checksum 이 존재 할경우 해당 패킷을 처리한다.
	 * 
	 * @param buffer
	 * @param incomingMessage
	 * @return
	 */
	public abstract boolean processChecksum(ByteBuf buffer, UsnIncomingMessage incomingMessage);
	
}

