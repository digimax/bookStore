package digimax.pages;

import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:21 PM
 */
public class Browse {

    @Inject
    private Logger logger;

   @Inject
   @Path("context:imagez/technology_en.jpg")
   @Property
   private org.apache.tapestry5.Asset technologyImage;


    @Inject
//    @Path("file:images/nonexistant.jpg")
    @Path("file:images/digimaxCom.jpg")
    @Property
    private org.apache.tapestry5.Asset digimaxImage;


    @BeforeRenderBody
    private void diagnostics() {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }
}
