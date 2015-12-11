package pl.nicos.screenshot

import org.springframework.boot.test.IntegrationTest
import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.web.client.RestTemplate
import pl.anicos.snapshot.Main
import pl.anicos.snapshot.model.SnapshotResult
import spock.lang.Specification

import javax.imageio.ImageIO
import java.awt.*
import java.awt.image.BufferedImage

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Main.class)
@WebAppConfiguration
@IntegrationTest
class SnapshootServiceSpec extends Specification {

	RestTemplate template = new TestRestTemplate()

	final RESIZED_WIDTH =400;
	final RESIZED_HEIGHT=281;

	final NORMAL_WIDTH =1024;
	final NORMAL_HEIGHT=768;

	final TEST_PAGE_HTML = 'testPage.html';


	def 'shouldCreateThumbnailAndResizeTo400x281'() {
		given: 'createExpectedResultAndUrlToTestPage'

		def urlToTestPage = getClass().getResource(TEST_PAGE_HTML)
		when:
		def templateGetForEntity = template.getForObject("http://localhost:8080/thumb?twidth=${RESIZED_WIDTH}&theight=${RESIZED_HEIGHT}&wwidth=1024&wheight=768&url="+urlToTestPage, SnapshotResult.class)
		BufferedImage result = createBufferedImageFromBase64(templateGetForEntity.getThumb())

		then:
		new Color(result.getRGB(0,0)).equals(Color.RED)
		result.getHeight().equals(RESIZED_HEIGHT);
		result.getWidth().equals(RESIZED_WIDTH)
	}

	def 'shouldCreateThumbnailWithoutResize'() {
		given: 'createExpectedResultAndUrlToTestPage'

		def urlToTestPage = getClass().getResource(TEST_PAGE_HTML)
		when:
		def templateGetForEntity = template.getForObject("http://localhost:8080/thumb?twidth=1024&theight=768&wwidth=1024&wheight=768&url="+urlToTestPage, SnapshotResult.class)
		BufferedImage result = createBufferedImageFromBase64(templateGetForEntity.getThumb())

		then:
		new Color(result.getRGB(0,0)).equals(Color.RED)
		result.getHeight().equals(NORMAL_HEIGHT);
		result.getWidth().equals(NORMAL_WIDTH)
	}


	def 'shouldThrowErrorWithWrongUrl'() {

		when:
		def responseEntity = template.getForEntity("http://localhost:8080/thumb?twidth=1024&theight=768&wwidth=1024&wheight=768&url=wp.pl", String.class)

		then:
		responseEntity.getStatusCode()==HttpStatus.BAD_REQUEST

	}

	private BufferedImage createBufferedImageFromBase64(thumb) {
		byte[] arrayOfBytes = Base64.getDecoder().decode(thumb)
		return  ImageIO.read(new ByteArrayInputStream(arrayOfBytes));
	}
}
