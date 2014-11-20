package pl.anicos.snapshot.fx;

import java.util.concurrent.FutureTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import org.springframework.web.context.request.async.DeferredResult;

import pl.anicos.snapshot.model.SnapshotDetail;
import pl.anicos.snapshot.model.SnapshotResult;

public class FxApplication extends Application {

	private static final int SCENE_HEIGHT = 10;
	private static final int SCENE_WIDTH = 10;
	private static FxApplication INSTANCE;

	public static FxApplication getInstance() {
		return INSTANCE;
	}

	public FxApplication() {
		INSTANCE = this;
	}

	@Override
	public void start(Stage primaryStage) {
		StackPane root = new StackPane();
		Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
		primaryStage.setScene(scene);
		primaryStage.show();		
	}

	public void createSnapshot(final DeferredResult<SnapshotResult> deferredResult, SnapshotDetail snapshotDetail) {

		final FutureTask<Void> futureTask = new FutureTask<Void>(() -> {
			WebViewStage stage = new WebViewStage(snapshotDetail);
			stage.loadPage(deferredResult);
			return null;
		});

		Platform.runLater(futureTask);
	}
}