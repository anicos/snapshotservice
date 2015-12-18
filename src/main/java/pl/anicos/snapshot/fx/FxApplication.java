package pl.anicos.snapshot.fx;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import pl.anicos.snapshot.image.ImageProcessor;
import pl.anicos.snapshot.model.ParametersNames;
import pl.anicos.snapshot.model.ResultStatus;

import java.awt.image.BufferedImage;
import java.util.Map;

public class FxApplication extends Application {

    private static final String WEB_VIEW_STYLE_CSS = "webViewStyle.css";
    private final String styleSheetForWebView = getClass().getResource(WEB_VIEW_STYLE_CSS).toExternalForm();
    private WebView webView;
    private final ImageProcessor imageProcessor = new ImageProcessor();

    @Override
    public void start(Stage primaryStage) {
        webView = new WebView();
        WebEngine engine = webView.getEngine();
        removeScrollBars(engine);
        Scene scene = new Scene(webView, getIntParameter(ParametersNames.WINDOW_WIDTH), getIntParameter(ParametersNames.WINDOW_HEIGHT));
        primaryStage.setScene(scene);
        primaryStage.show();

        addOnWebViewChangeListener(engine);
        engine.load(getStringParameter(ParametersNames.URL));
    }

    private void addOnWebViewChangeListener(WebEngine engine) {
        Worker<Void> loadWorker = engine.getLoadWorker();
        ReadOnlyObjectProperty<Worker.State> stateProperty = loadWorker.stateProperty();
        stateProperty.addListener((observableValue, oldState, newState) -> onStateChange(newState));
    }

    private void onStateChange(Worker.State newState) {
        switch (newState) {
            case SUCCEEDED:
                int timeForRenderingPage = getIntParameter(ParametersNames.TIME_FOR_RENDERING_PAGE);
                PauseTransition pause = new PauseTransition(Duration.millis(timeForRenderingPage));
                pause.setOnFinished((e) -> decodeScreenShotAndSetResult());
                pause.play();
                break;
            case FAILED:
                System.out.println(ResultStatus.FAILURE.name());
                break;
            default:
        }
    }

    private void decodeScreenShotAndSetResult() {
        String encodedImage = createSnapshotInBase64();
        System.out.println(ResultStatus.SUCCESS.name() + encodedImage);
    }

    private void removeScrollBars(WebEngine engine) {
        engine.setUserStyleSheetLocation(styleSheetForWebView);
    }

    private String createSnapshotInBase64() {
        WritableImage snapshot = webView.snapshot(null, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
        return imageProcessor.resizeAndEncode(bufferedImage, getIntParameter(ParametersNames.THUMBNAIL_WIDTH), getIntParameter(ParametersNames.THUMBNAIL_HEIGHT));
    }

    private String getStringParameter(String name) {
        Parameters parameters = getParameters();
        Map<String, String> named = parameters.getNamed();
        return named.get(name);
    }

    private int getIntParameter(String name) {
        return Integer.valueOf(getStringParameter(name));
    }
}