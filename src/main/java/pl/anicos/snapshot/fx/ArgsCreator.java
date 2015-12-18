package pl.anicos.snapshot.fx;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.anicos.snapshot.model.ParametersNames;
import pl.anicos.snapshot.spring.PropertiesProvider;

@Component
public class ArgsCreator {


    private final PropertiesProvider propertiesProvider;

    @Autowired
    public ArgsCreator(PropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }


    public String create(String url, int thumbnailWidth, int thumbnailHeight, int windowWidth, int windowHeight) {
        return createParam(ParametersNames.URL, url) +
                createParam(ParametersNames.THUMBNAIL_WIDTH, String.valueOf(thumbnailWidth)) +
                createParam(ParametersNames.THUMBNAIL_HEIGHT, String.valueOf(thumbnailHeight)) +
                createParam(ParametersNames.WINDOW_WIDTH, String.valueOf(windowWidth)) +
                createParam(ParametersNames.WINDOW_HEIGHT, String.valueOf(windowHeight)) +
                createParam(ParametersNames.TIME_FOR_RENDERING_PAGE, String.valueOf(propertiesProvider.getTimeForPageRendered()));
    }

    private String createParam(String name, String value) {
        return " --" + name + "=" + value;
    }
}
