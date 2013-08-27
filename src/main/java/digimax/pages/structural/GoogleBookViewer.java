package digimax.pages.structural;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.PageRenderLinkSource;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/19/13
 * Time: 12:37 PM
 */
@Import(stylesheet = {"context:layout/layout.css", "context:layout/ncapsuld.css"})
public class GoogleBookViewer {

    @Inject
    private Logger logger;

    @Inject
    private PageRenderLinkSource pageRenderLinkSource;

    @SetupRender
    private void setupRenderer() {
        logger.debug("Google Book Viewer::setupRenderer");
    }

    public String getBookViewerUrl() {
        return pageRenderLinkSource.createPageRenderLink(GoogleBookViewer.class).toAbsoluteURI();
    }
}
