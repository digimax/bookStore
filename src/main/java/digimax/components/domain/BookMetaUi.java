package digimax.components.domain;

import digimax.entities.item.BookMeta;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/18/13
 * Time: 12:47 PM
 */
public class BookMetaUi {

    @Inject
    private Logger logger;

    @Inject
    private ComponentResources componentResources;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    private BookMeta bookMeta;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }

    @BeforeRenderTemplate
    boolean checkBeforeTemplate(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
        return true;
    }

    @AfterRenderTemplate
    void checkAfterTemplate(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }

}
