package pl.anicos.snapshot.fx;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import org.springframework.web.context.request.async.DeferredResult;

import pl.anicos.snapshot.exception.PageNotFoundException;
import pl.anicos.snapshot.image.ImageProcessor;
import pl.anicos.snapshot.model.SnapshotDetail;
import pl.anicos.snapshot.model.SnapshotResult;
import pl.anicos.snapshot.spring.SpringBeanProvider;

public class WebViewStage extends Stage {

	private static final String WEB_VIEW_STYLE_CSS = "webViewStyle.css";

	private final String styleSheetForWebView = getClass().getResource(WEB_VIEW_STYLE_CSS).toExternalForm();
	private final ImageProcessor imageProcessor;
	private final WebView webView;
	private final SnapshotDetail snapshotDetail;

	public WebViewStage(SnapshotDetail snapshotDetail) {
		this.snapshotDetail = snapshotDetail;
		this.imageProcessor = SpringBeanProvider.getBean(ImageProcessor.class);
		this.webView = createWebView();
	}

	public void loadPage(DeferredResult<SnapshotResult> deferredResult) {
		WebEngine engine = webView.getEngine();
		addOnWebViewChangeListner(engine, deferredResult);
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

	private void addOnWebViewChangeListner(WebEngine engine, DeferredResult<SnapshotResult> deferredResult) {
		Worker<Void> loadWorker = engine.getLoadWorker();
		ReadOnlyObjectProperty<State> stateProperty = loadWorker.stateProperty();

		stateProperty.addListener((observableValue, oldState, newState) -> runFutureTaskOnStateChange(newState, deferredResult));
	}

	private void runFutureTaskOnStateChange(State newState, DeferredResult<SnapshotResult> deferredResult) {
		final FutureTask<Void> futureTask = new FutureTask<Void>(() -> onStateChange(newState, deferredResult));
		Platform.runLater(futureTask);
	}

	private Void onStateChange(State newState, DeferredResult<SnapshotResult> deferredResult) {

		switch (newState) {
		case SUCCEEDED:

			Platform.runLater(new FutureTask<Void>(new Callable<Void>() {

				@Override
				public Void call() throws Exception {
					String encodedImage = createSnapshotInBase64();
					deferredResult.setResult(new SnapshotResult(encodedImage));
					close();
					return null;
				}
			}));

			break;
		case FAILED:
			deferredResult.setErrorResult(new PageNotFoundException("Can't open the page, wrong url"));
			close();
			break;
		default:
		}
		return null;
	}

	private String createSnapshotInBase64() {
		WritableImage snapshot = webView.snapshot(null, null);
		BufferedImage bufferedImage = SwingFXUtils.fromFXImage(snapshot, null);
		String encodedImage = imageProcessor.resizeAndEncode(bufferedImage, snapshotDetail);
		return encodedImage;
	}
}