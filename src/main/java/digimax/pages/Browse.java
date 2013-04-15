package digimax.pages;

import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.BeginRender;
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

    @Inject
    @Path("file:images/A_STAINLESS_STEEL_RAT_IS_BORN~HARRY_HARRISON.png")
    @Property
    private org.apache.tapestry5.Asset bookImage;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }
}
