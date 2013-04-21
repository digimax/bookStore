package digimax.pages;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.sql.Insert;
import org.slf4j.Logger;
/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:17 PM
 */
public class Search {

    @Inject
    Logger logger;


    @Property
    private String author;

    @Property
    private String title;

    void onSelectedFromSearchByAuthor() {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }

    void onSelectedFromSearchByTitle() {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }

}
