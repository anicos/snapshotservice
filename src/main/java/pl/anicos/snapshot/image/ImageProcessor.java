package pl.anicos.snapshot.image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
public class ImageProcessor {

    @Autowired
    private BufferedImageResizer bufferedImageResizer = new BufferedImageResizer();
    @Autowired
    private Base64Encoder base64Encoder = new Base64Encoder();

    public String resizeAndEncode(BufferedImage bufferedImage, int thumbnailWidth, int thumbnailHeight) {
        BufferedImage afterResizeBufferedImage = bufferedImageResizer.resize(bufferedImage, thumbnailWidth, thumbnailHeight);
        return base64Encoder.encodeBufferedImage(afterResizeBufferedImage);
    }

}
