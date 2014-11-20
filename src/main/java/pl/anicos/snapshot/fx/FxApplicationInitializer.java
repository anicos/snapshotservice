package pl.anicos.snapshot.fx;

import javafx.application.Application;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
public class FxApplicationInitializer {

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	
	@PostConstruct
	private void init() {
		threadPoolTaskExecutor.execute(new Runnable() {
			
			@Override
			public void run() {
				Application.launch(FxApplication.class);
				
			}
		});		
	}
}
