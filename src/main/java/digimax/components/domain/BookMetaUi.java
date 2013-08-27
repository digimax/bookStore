package digimax.components.domain;

import digimax.entities.item.BookMeta;
import digimax.pages.structural.GoogleBookViewer;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
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
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private PageRenderLinkSource pageRenderLinkSource;

    @InjectComponent
    private Zone asyncinfozone;

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

    public boolean isInitialized() {
        return bookMeta!=null && bookMeta.isInitialized();
    }

    public Long getAsyncRefreshPeriod() {
        return 4l;
    }

    public void afterRender(){
//        javaScriptSupport.addScript("$(\"#stop\").bind(\"click\", function(){$(\"#asyncinfozone\").trigger(\"stopRefresh\");});");
//        javaScriptSupport.addScript("$(\"#start\").bind(\"click\", function(){$(\"#clickZone\").trigger(\"startRefresh\");});");
    }


    public String getBookViewerUrl() {
        return pageRenderLinkSource.createPageRenderLink(GoogleBookViewer.class).toAbsoluteURI();
    }


}
