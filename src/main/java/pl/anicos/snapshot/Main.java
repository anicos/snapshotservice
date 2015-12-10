package pl.anicos.snapshot;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class Main {
	
	public static void main(String[] args) throws IOException, InterruptedException {		
		 SpringApplication.run(Main.class, args);
	}
	
	@Bean
	public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
		ThreadPoolTaskExecutor poolTaskExecutor = new ThreadPoolTaskExecutor();
		poolTaskExecutor.setMaxPoolSize(1);
		poolTaskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		return poolTaskExecutor;
	}
	
}
