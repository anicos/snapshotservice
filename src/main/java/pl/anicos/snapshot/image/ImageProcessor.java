package pl.anicos.snapshot.image;

import java.awt.image.BufferedImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.anicos.snapshot.model.SnapshotDetail;

@Component
public class ImageProcessor {

	@Autowired
	private BufferedImageResizer bufferedImageResizer;
	@Autowired
	private Base64Encoder base64Encoder;
	
	public String resizeAndEncode(BufferedImage bufferedImage, SnapshotDetail snapshotDetail){
		BufferedImage afterResizeBufferedImage = bufferedImageResizer.resize(bufferedImage, snapshotDetail.getThumbnailWidth(), snapshotDetail.getThumbnailHeight());
		return base64Encoder.encodeBufferedImage(afterResizeBufferedImage);
	}
	
}
