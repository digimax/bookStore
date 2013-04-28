package digimax.components.domain;

import digimax.entities.geo.Address;
import digimax.entities.people.Customer;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/27/13
 * Time: 3:06 PM
 */
public class AddressUi {

    @Inject
    Logger logger;

    @Parameter(required = true, defaultPrefix = BindingConstants.PROP)
    @Property
    private Address address;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("beginRenderer, address :: {}", address);
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
