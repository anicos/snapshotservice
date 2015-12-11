package pl.anicos.snapshot.rest;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import pl.anicos.snapshot.fx.FxApplication;
import pl.anicos.snapshot.model.SnapshotDetail;
import pl.anicos.snapshot.model.SnapshotResult;
import pl.anicos.snapshot.spring.PropertiesProvider;

@RestController
class SnapshotService {

	@Autowired
	private PropertiesProvider propertiesProvider;

	private final Log log = LogFactory.getLog(getClass());

	@RequestMapping(value="/thumb", method = RequestMethod.GET)
	@ResponseBody
	private DeferredResult<SnapshotResult> home(@RequestParam("url") String url, @RequestParam("twidth") int thumbnailWidth, @RequestParam("theight") int thumbnailHeight,
			@RequestParam("wwidth") int windowWidth, @RequestParam("wheight") int windowHeight) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
		
		SnapshotDetail snapshotDetail = new SnapshotDetail(url, thumbnailWidth, thumbnailHeight, windowWidth, windowHeight);
		
		final DeferredResult<SnapshotResult> deferredResult = new DeferredResult<>(propertiesProvider.getDeferredResultTimeout());
		deferredResult.onTimeout(() -> log.info("Timeout on url: " +url));

		FxApplication.getInstance().createSnapshot(deferredResult, snapshotDetail);
		
		return deferredResult;

	}
}
