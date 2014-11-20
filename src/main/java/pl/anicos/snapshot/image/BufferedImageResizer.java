package pl.anicos.snapshot.image;

import java.awt.image.BufferedImage;

import org.springframework.stereotype.Component;

import com.mortennobel.imagescaling.ResampleOp;

@Component
public class BufferedImageResizer {
	
	
	public BufferedImage resize(BufferedImage originalImage, int width, int height) {
		ResampleOp resampleOp = new ResampleOp(width, height);
		return resampleOp.filter(originalImage, null);
	}
}
