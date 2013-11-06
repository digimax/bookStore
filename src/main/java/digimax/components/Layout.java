package digimax.components;

import digimax.entities.people.Visit;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 1:48 PM
 */
@Import(stylesheet = {"context:layout/layout.css", "context:layout/ncapsuld.css"})
public class Layout {
    @Inject
    private Logger logger;

    @Inject
    private org.apache.tapestry5.ComponentResources resources;

    private boolean visitExists;
    @SessionState
    private Visit visit;

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

    public Object[] getPageNames()
    {
        List<String> pageNames = new ArrayList<>();
        pageNames.add("Index");
//        pageNames.add("About");
        pageNames.add("Browse");
        pageNames.add("Search");
        pageNames.add("BookCart");
        pageNames.add("Help");

        if (!(visitExists && visit.isValid())) {
            pageNames.add("Login");
        } else {
            pageNames.add("User");
            pageNames.add("Logout");
        }
        return pageNames.toArray();
    }

//    void beforeRenderBody(MarkupWriter writer) {
//        logger.debug("Start Diagnostics");
//        logger.debug("End Diagnostics");
//    }
}
