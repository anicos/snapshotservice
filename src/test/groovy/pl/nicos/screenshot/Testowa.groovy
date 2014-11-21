package pl.nicos.screenshot

import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.web.WebAppConfiguration;

import pl.anicos.snapshot.Main;
import spock.lang.Specification;

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Main.class)
@WebAppConfiguration
@IntegrationTest
class Testowa extends Specification {
   
}
