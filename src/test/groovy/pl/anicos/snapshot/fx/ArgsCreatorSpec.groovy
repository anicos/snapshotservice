package pl.anicos.snapshot.fx

import pl.anicos.snapshot.spring.PropertiesProvider
import spock.lang.Specification


class ArgsCreatorSpec extends Specification {

    def "getArgs"() {
        given: 'crate test object'
        def propertiesProvider = Mock(PropertiesProvider)
        propertiesProvider.timeForPageRendered >> 5000

        def testObj = new ArgsCreator(propertiesProvider);
        def result = testObj.create("url", 1, 2, 3, 4)
        expect:
        ''' --url=url --thumbnailWidth=1 --thumbnailHeight=2 --windowWidth=3 --windowHeight=4 --timeForRenderingPage=5000''' == result
    }
}
