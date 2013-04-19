package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.people.Author;
import digimax.services.domain.BookMetaService;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/16/13
 * Time: 7:15 PM
 */
//@Import(library="  https://www.google.com/jsapi")
public class BookUi {
    @Inject
    private Logger logger;

    @Inject
    private BookMetaService bookMetaService;

    @Property
    private Author author;

    @PageActivationContext
    @Property
    private Book book;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("End Diagnostics");
        //populated the Book MetaData from web service
        bookMetaService.populateBookMeta(book);
    }

    public String getLargeImageFileName() {
        Image image = book.images.get(0);
        return image.fileName;
    }

    public String getSmallImageFileName() {
        Image image = book.images.get(1);
        return image.fileName;
    }

    private static final String GOOGLE_BOOKS_VIEWER_SCRIPT =
//            "Tapestry.onDOMLoaded(function() {"+
                    "google.load(\"books\", \"0\");"+
                    "function initialize() {"+
                    "    var viewer = new google.books.DefaultViewer(document.getElementById('viewerCanvas'));"+
                    "    viewer.load('ISBN:0738531367');"+
                    "}"+
                    "google.setOnLoadCallback(initialize);";//+
//                    "}";



    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectContainer
    private ClientElement element;

    @AfterRender
    public void afterRender() {
        javaScriptSupport.addScript(GOOGLE_BOOKS_VIEWER_SCRIPT);
//        javaScriptSupport.addScript("new Confirm('%s', '%s');", element.getClientId(), message);
    }

}
