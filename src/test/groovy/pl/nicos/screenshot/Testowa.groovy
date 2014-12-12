package pl.nicos.screenshot

import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import pl.anicos.snapshot.Main;
import pl.anicos.snapshot.model.SnapshotResult;
import spock.lang.Specification;

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Main.class)
@WebAppConfiguration
@IntegrationTest
class Testowa extends Specification {
	
	RestTemplate template = new TestRestTemplate()
	
	def "adder-test"() {
		given: "should create thumb"
		def urlToTestPage = getClass().getResource("testPage.html")
		
		def templateGetForEntity = template.getForObject("http://localhost:8080/thumb?twidth=400&theight=281&wwidth=1024&wheight=768&url="+getClassGetResource, SnapshotResult.class)
		
		println templateGetForEntity.getThumb()
		expect: false
	}
}
