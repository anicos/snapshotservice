package pl.anicos.snapshot.rest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.anicos.snapshot.fx.ArgsCreator;
import pl.anicos.snapshot.fx.JavaFxProcessExecutor;
import pl.anicos.snapshot.fx.MainFx;
import pl.anicos.snapshot.model.SnapshotResult;
import pl.anicos.snapshot.spring.PropertiesProvider;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

@RestController
class SnapshotService {

    @Autowired
    private PropertiesProvider propertiesProvider;
    @Autowired
    private JavaFxProcessExecutor javaFxProcessExecutor;
    @Autowired
    private ArgsCreator argsCreator;

    private final Log log = LogFactory.getLog(getClass());

    @RequestMapping(value = "/thumb", method = RequestMethod.GET)
    @ResponseBody
    private SnapshotResult home(@RequestParam("url") String url, @RequestParam("twidth") int thumbnailWidth, @RequestParam("theight") int thumbnailHeight,
                                @RequestParam("wwidth") int windowWidth, @RequestParam("wheight") int windowHeight) throws URISyntaxException, IOException, InterruptedException, ExecutionException {

        String args = argsCreator.create(url, thumbnailWidth, thumbnailHeight, windowWidth, windowHeight);

        String base64Result = javaFxProcessExecutor.exec(MainFx.class, args);

        return new SnapshotResult(base64Result);

    }
}
