package pl.anicos.snapshot.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringBeanProvider {

	private static ApplicationContext APPLICATION_CONTEXT;

	@Autowired
	public SpringBeanProvider(ApplicationContext applicationContext) {
		APPLICATION_CONTEXT = applicationContext;
	}

	public static <T> T getBean(Class<T> requiredType){
		return APPLICATION_CONTEXT.getBean(requiredType);
	}
}
