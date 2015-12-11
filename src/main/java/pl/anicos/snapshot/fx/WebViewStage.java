package pl.anicos.snapshot.fx;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.request.async.DeferredResult;
import pl.anicos.snapshot.exception.PageNotFoundException;
import pl.anicos.snapshot.image.ImageProcessor;
import pl.anicos.snapshot.model.SnapshotDetail;
import pl.anicos.snapshot.model.SnapshotResult;
import pl.anicos.snapshot.spring.SpringBeanProvider;

import java.awt.image.BufferedImage;
import java.util.logging.Logger;

class WebViewStage extends Stage {

	private static final String WEB_VIEW_STYLE_CSS = "webViewStyle.css";
	private static final int TIME_FOR_PAGE_RENDERER = 7;

	private final String styleSheetForWebView = getClass().getResource(WEB_VIEW_STYLE_CSS).toExternalForm();
	private final ImageProcessor imageProcessor;
	private final WebView webView;
	private final SnapshotDetail snapshotDetail;
	private final Log log = LogFactory.getLog(getClass());

	public WebViewStage(SnapshotDetail snapshotDetail) {
		this.snapshotDetail = snapshotDetail;
		this.imageProcessor = SpringBeanProvider.getBean(ImageProcessor.class);
		this.webView = createWebView();
	}

	public void loadPage(DeferredResult<SnapshotResult> deferredResult) {
		WebEngine engine = webView.getEngine();
		addOnWebViewChangeListener(engine, deferredResult);
		log.info("Load url: "+ snapshotDetail.getUrl());
		engine.load(snapshotDetail.getUrl());
	}

	private WebView createWebView() {
		WebView webView = new WebView();
		WebEngine engine = webView.getEngine();

		removeScrollBars(engine);

		addToScene(webView);
		return webView;
	}

	private void removeScrollBars(WebEngine engine) {
		engine.setUserStyleSheetLocation(styleSheetForWebView);
	}

	private void addToScene(WebView webView) {
		Scene scene = new Scene(webView, snapshotDetail.getWindowWidth(), snapshotDetail.getWindowHeight());
		setScene(scene);
		show();
	}

	private void addOnWebViewChangeListener(WebEngine engine, DeferredResult<SnapshotResult> deferredResult) {
		Worker<Void> loadWorker = engine.getLoadWorker();
		ReadOnlyObjectProperty<State> stateProperty = loadWorker.stateProperty();

		stateProperty.addListener((observableValue, oldState, newState) -> runFutureTaskOnStateChange(newState, deferredResult));
	}

	private void runFutureTaskOnStateChange(State newState, DeferredResult<SnapshotResult> deferredResult) {
		Platform.runLater(() -> onStateChange(newState, deferredResult));
	}

	private void onStateChange(State newState, DeferredResult<SnapshotResult> deferredResult) {
		switch (newState) {
		case SUCCEEDED:
			PauseTransition pause = new PauseTransition(Duration.seconds(TIME_FOR_PAGE_RENDERER));
			pause.setOnFinished((e) -> decodeScreenShotAndSetResult(deferredResult));
			pause.play();

			break;
		case FAILED:
			deferredResult.setErrorResult(new PageNotFoundException());
			close();
			break;
		default:
		}
	}
	
	private void decodeScreenShotAndSetResult(DeferredResult<SnapshotResult> deferredResult) {
		String encodedImage = createSnapshotInBase64();
		deferredResult.setResult(new SnapshotResult(encodedImage));
		close();
	}

	private String createSnapshotInBase64() {
		WritableImage snapshot = webView.snapshot(null, null);
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
		return imageProcessor.resizeAndEncode(bufferedImage, snapshotDetail);
	}
}