package kr.co.harangi.lmcfs.config;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import kr.co.harangi.lmcfs.netty.handler.UsnServerChannelInitializer;
import kr.co.harangi.lmcfs.netty.handler.UsnServerHandler;

@Configuration
@EnableScheduling
@EnableAsync
public class ServerConfig implements SchedulingConfigurer {

	@Bean(destroyMethod = "shutdown")
	public TaskScheduler scheduler() {
		ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
		scheduler.setPoolSize(10);

		return scheduler;
	}
	
	@Bean(destroyMethod = "shutdown")
	public TaskExecutor executor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(60);
		executor.setMaxPoolSize(100);
		executor.setQueueCapacity(100);
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
		
		return executor;
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setTaskScheduler(scheduler());
	}
	
	@Bean
	@Scope("prototype")
	public UsnServerHandler usnServerHandler() {
		return new UsnServerHandler();
	}
	
	@Bean
	public UsnServerChannelInitializer usnServerChannelInitializer() {
		return new UsnServerChannelInitializer();
	}
}
