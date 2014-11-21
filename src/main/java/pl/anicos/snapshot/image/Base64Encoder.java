package pl.anicos.snapshot.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;



@Component
public class Base64Encoder {
	
	public String encodeBufferedImage(BufferedImage bufferedImage) {
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			byteArrayOutputStream.flush();
			return Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Can't write WritableImage to BufferedImage", e);
		}
		
	}
}
