package com.parkwoodrx.fastrx;

import java.util.concurrent.Executor;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@SpringBootApplication
@EnableAsync
public class FastrxApplication extends SpringBootServletInitializer {

	public static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(FastrxApplication.class);
		builder.headless(false);
		context = builder.run(args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		application.headless(false);
		return application.sources(FastrxApplication.class);
	}
	 @Bean
	    public Executor asyncExecutor() {
	        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
	        executor.setCorePoolSize(3);
	        executor.setMaxPoolSize(3);
	        executor.setQueueCapacity(500);
	        executor.setThreadNamePrefix("FaxSend-");
	        executor.initialize();
	        return executor;
	    }
}
