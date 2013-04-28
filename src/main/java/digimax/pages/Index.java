package digimax.pages;

import java.util.Date;

import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.services.domain.LibraryService;
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.*;
import org.apache.tapestry5.corelib.components.*;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.hibernate.Session;
import org.slf4j.Logger;

/**
 * Start page of app ncapsuld.
 */
public class Index
{
    @Inject
    private Logger logger;

    @Inject
    private Session session;

    @Inject
    private LibraryService libraryService;

    @Property
    @Inject
    @Symbol(SymbolConstants.TAPESTRY_VERSION)
    private String tapestryVersion;

    @Property
    private Shelf shelf;

    @Inject
    @Path("context:imagez/books.jpg")
    @Property
    private Asset bannerImage;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
    }

    public Library getLibrary() {
        return (Library) session.createCriteria(Library.class).uniqueResult();
    }

//    @InjectComponent
//    private Zone zone;
//
//    @Persist
//    @Property
//    private int clickCount;
//
//    @Inject
//    private AlertManager alertManager;
//
//    public Date getCurrentTime()
//    {
//        return new Date();
//    }
//
//    void onActionFromIncrement()
//    {
//        alertManager.info("Increment clicked");
//
//        clickCount++;
//    }
//
//    Object onActionFromIncrementAjax()
//    {
//        clickCount++;
//
//        alertManager.info("Increment (via Ajax) clicked");
//
//        return zone;
//    }
}
