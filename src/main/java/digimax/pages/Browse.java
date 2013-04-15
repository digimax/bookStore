package digimax.pages;

import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:21 PM
 */
public class Browse {

   @Inject
   @Path("context:assets/technology_en.jpg")
   @Property
   private org.apache.tapestry5.Asset technologyImage;
}
