package pl.anicos.snapshot.fx;

import javafx.application.Application;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Component
class FxApplicationInitializer {

	@Autowired
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;

	@PostConstruct
	private void init() {

		threadPoolTaskExecutor.execute(() -> Application.launch(FxApplication.class));
	}
}
