package pl.anicos.snapshot.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesProvider {

    @Value("${time.for.page.rendered:7000}")
    private int timeForPageRendered;

    public int getTimeForPageRendered() {
        return timeForPageRendered;
    }
}
