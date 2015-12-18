package pl.anicos.snapshot.image;

import com.mortennobel.imagescaling.ResampleOp;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;

@Component
class BufferedImageResizer {


    public BufferedImage resize(BufferedImage originalImage, int width, int height) {
        ResampleOp resampleOp = new ResampleOp(width, height);
        return resampleOp.filter(originalImage, null);
    }
}
