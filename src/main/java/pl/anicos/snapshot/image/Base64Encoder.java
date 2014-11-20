package pl.anicos.snapshot.image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.springframework.stereotype.Component;

import com.sun.org.apache.xml.internal.security.utils.Base64;

@Component
public class Base64Encoder {
	
	public String encodeBufferedImage(BufferedImage bufferedImage) {
		try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
			ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
			byteArrayOutputStream.flush();
			return Base64.encode(byteArrayOutputStream.toByteArray());
		} catch (IOException e) {
			throw new RuntimeException("Can't write WritableImage to BufferedImage", e);
		}
		
	}
}
