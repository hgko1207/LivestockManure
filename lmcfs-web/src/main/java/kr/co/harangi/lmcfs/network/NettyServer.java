package kr.co.harangi.lmcfs.network;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import kr.co.harangi.lmcfs.netty.handler.UsnServerChannelInitializer;
import lombok.extern.slf4j.Slf4j;

/**
 * The type Netty server.
 */
@Slf4j
@Component
@PropertySource(value = "classpath:/application.properties")
public class NettyServer {

	@Value("${tcp.ip}")
	private String localIp;
	
    @Value("${tcp.port}")
    private int tcpPort;
 
    @Value("${boss.thread.count}")
    private int bossCount;
    
    private	ChannelFuture channelFuture;
    
    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;
    
    @Autowired
    private UsnServerChannelInitializer usnServerChannelInitializer;
    
    public void start() {
    	
    	/** 클라이언트 연결을 수락하는 부모 스레드 그룹  */
        bossGroup = new NioEventLoopGroup(bossCount);
        
        /** 연결된 클라이언트ㄹ의 소켓으로 부터 데이터 입출력 및 이벤트를 담당하는 자식 스레드  */
        workerGroup = new NioEventLoopGroup();
        
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
    		.childHandler(usnServerChannelInitializer)
    		.childOption(ChannelOption.TCP_NODELAY, true)
    		.childOption(ChannelOption.SO_KEEPALIVE, true);
        
        log.debug("---------- Netty Option ----------");
        log.debug(" tcpNoDelay is true");
        log.debug(" keepAlive is true");

        // Bind and start to accept incoming connections.
        if (localIp == null || localIp.isEmpty()) {
        	channelFuture = bootstrap.bind(new InetSocketAddress(tcpPort));
        } else {
        	channelFuture = bootstrap.bind(new InetSocketAddress(localIp, tcpPort));
        }
    }
    
    public void close() {
    	if (channelFuture != null) {
    		try {
				channelFuture.channel().close().sync();
			} catch (InterruptedException e) {
				log.info("", e.toString());
			}
    		
    		log.info("releaseExternalResources");
    		
    		bossGroup.shutdownGracefully();
    		workerGroup.shutdownGracefully();
    	}
    }
}
