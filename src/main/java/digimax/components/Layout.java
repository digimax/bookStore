package digimax.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 1:48 PM
 */
@Import(stylesheet = "context:layout/layout.css")
public class Layout {
    @Inject
    private Logger logger;

    @Inject
    private org.apache.tapestry5.ComponentResources resources;

    /**
     * The page title, for the <title> element and the <h1> element.
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String title;


    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String sidebarTitle;

    @Property
    private String pageName;

    public String getClassForPageName()
    {
        return resources.getPageName().equalsIgnoreCase(pageName)
                ? "current_page_item"
                : null;
    }

    @Property
    @Inject
    @Symbol(SymbolConstants.APPLICATION_VERSION)
    private String appVersion;

    public String[] getPageNames()
    {
        return new String[]{"Index", "Browse", "Search", "BookCart"};
    }

//    void beforeRenderBody(MarkupWriter writer) {
//        logger.debug("Start Diagnostics");
//        logger.debug("End Diagnostics");
//    }
}
