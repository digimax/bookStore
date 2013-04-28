package digimax;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.net.URL;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 12:31 PM
 */
@SuppressWarnings("unchecked")
public class ClassPathResourcesTest {

    private static final String CLASS_PATH_FILE_ROOT="/Users/jonwilliams/wrk/digimaxComOpenSource/apps/bookStore/";

    @Test
    public void testBuildIdeaProjectCopiedTestXmlFilesToTarget () {
        ClassLoader loader = ClassPathResourcesTest.class.getClassLoader();
        URL resourceURL = loader.getResource("digimax/ClassPathResourcesTest.class");
        Assert.assertEquals(resourceURL.getPath(),
                CLASS_PATH_FILE_ROOT+"target/test-classes/digimax/ClassPathResourcesTest.class");

        resourceURL = loader.getResource("digimax/BootupServiceTest.class");
        Assert.assertEquals(resourceURL.getPath(),
                CLASS_PATH_FILE_ROOT+"target/test-classes/digimax/BootupServiceTest.class");

        resourceURL = getClass().getProtectionDomain().getCodeSource().getLocation();
        Assert.assertEquals(resourceURL.getPath(),
                CLASS_PATH_FILE_ROOT+"target/test-classes/");


        resourceURL = loader.getResource("test-hibernate.xml");
        Assert.assertEquals(resourceURL.getPath(),
                CLASS_PATH_FILE_ROOT+"target/test-classes/test-hibernate.xml");
// FAILS USING IDEA BACKGROUND COMPILATION -->       Assert.assertEquals(resourceURL.getPath(), "/Users/jonwilliams/wrk/digimax/nc/ncapsuld/target/test-classes/test-hibernate.xml");

    }
}
