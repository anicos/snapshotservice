package pl.nicos.screenshot

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import pl.anicos.snapshot.Main;
import pl.anicos.snapshot.image.Base64Encoder;
import pl.anicos.snapshot.model.SnapshotResult;
import spock.lang.Specification;

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Main.class)
@WebAppConfiguration
@IntegrationTest
class SnapshootServiceSpec extends Specification {

	@Autowired
	Base64Encoder base64Encoder;

	RestTemplate template = new TestRestTemplate()

	def "shouldCreateThumbnailAndResizeTo400x281"() {
		given: "createExpectedResultAndUrlToTestPage"

		def pathToExpectedFile = getClass().getResource("expectedImage_400x281.png").getFile()
		BufferedImage expectedBufferedImage = ImageIO.read(new File(pathToExpectedFile));
		def expectedResult = base64Encoder.encodeBufferedImage(expectedBufferedImage)

		def urlToTestPage = getClass().getResource("testPage.html")
		when:
		def templateGetForEntity = template.getForObject("http://localhost:8080/thumb?twidth=400&theight=281&wwidth=1024&wheight=768&url="+urlToTestPage, SnapshotResult.class)
		System.out.println(templateGetForEntity.getThumb());
		then:
		expectedResult.equals(templateGetForEntity.getThumb());
	}

	def "shouldCreateThumbnailWithoutResize"() {
		given: "createExpectedResultAndUrlToTestPage"

		def pathToExpectedFile = getClass().getResource("expectedImage_1024x768.png").getFile()
		BufferedImage expectedBufferedImage = ImageIO.read(new File(pathToExpectedFile));
		def expectedResult = base64Encoder.encodeBufferedImage(expectedBufferedImage)

		def urlToTestPage = getClass().getResource("testPage.html")
		when:
		def templateGetForEntity = template.getForObject("http://localhost:8080/thumb?twidth=1024&theight=768&wwidth=1024&wheight=768&url="+urlToTestPage, SnapshotResult.class)
		System.out.println(templateGetForEntity.getThumb());
		then:
		expectedResult.equals(templateGetForEntity.getThumb());
	}


	def "shouldThrowErrorWithWrongUrl"() {

		when:
		def responseEntity = template.getForEntity("http://localhost:8080/thumb?twidth=1024&theight=768&wwidth=1024&wheight=768&url=wp.pl", String.class)

		then:
		responseEntity.getStatusCode()==HttpStatus.BAD_REQUEST

	}
}
