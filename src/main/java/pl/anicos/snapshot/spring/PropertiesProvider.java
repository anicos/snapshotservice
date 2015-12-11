package pl.anicos.snapshot.spring;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesProvider {

    @Value("${deferred.result.timeout:15000}")
    private Long deferredResultTimeout;

    @Value("${time.for.page.rendered:7000}")
    private Double timeForPageRendered;

    public Double getTimeForPageRendered() {
        return timeForPageRendered;
    }

    public Long getDeferredResultTimeout() {
        return deferredResultTimeout;
    }
}
