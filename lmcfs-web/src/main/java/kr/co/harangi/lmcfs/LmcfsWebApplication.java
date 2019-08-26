package kr.co.harangi.lmcfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import kr.co.harangi.lmcfs.network.NettyServer;

@SpringBootApplication
public class LmcfsWebApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(LmcfsWebApplication.class, args);
		NettyServer nettyServer = context.getBean(NettyServer.class);
        nettyServer.start();
	}

}
