package digimax.pages;

import digimax.components.domain.BookMetaUi;
import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.Cart;
import digimax.entities.item.LineItem;
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
//@Import(library="https://www.google.com/jsapi")
public class BookUi {
    @Inject
    private Logger logger;

    @Inject
    private BookMetaService bookMetaService;

    private boolean cartExists;
    @SessionState
    private Cart cart;

    @Property
    private Author author;

    @PageActivationContext
    @Property
    private Book book;

    @InjectComponent
    private BookMetaUi bookmetaui;

    @SetupRender
    private void initializeBookMeta() {
        final Book thisBook = book;
        logger.debug("Start Diagnostics");
        assert(thisBook!=null);
        //populate the Book MetaData from web service
        Thread populateMetaDataThread = new Thread() {
            @Override
            public void run() {
                logger.debug("Book :: {}", thisBook);
                bookMetaService.populateBookMeta(thisBook);
            }
        };
        if (thisBook.bookMeta==null || !thisBook.bookMeta.isInitialized()) {
            populateMetaDataThread.start();
        }

        logger.debug("End Diagnostics");
    }

    @BeginRender
    void beginRender(MarkupWriter writer) {

    }

    public Image getLargeImage() {
        Image image = book.images.get(0);
        return image;
    }

    public Image getSmallImage() {
        Image image = book.images.get(1);
        return image;
    }

    public Image getRotatedImage() {
        Image image = book.images.get(2);
        return image;
    }

//    private static final String GOOGLE_BOOKS_VIEWER_SCRIPT =
////            "Tapestry.onDOMLoaded(function() {"+
//                    "alert('testing javascript');"+
//                    "google.load(\"books\", \"0\");"+
//                    "function initialize() {"+
//                    "    var viewer = new google.books.DefaultViewer(document.getElementById('viewerCanvas'));"+
//                    "    viewer.load('ISBN:0738531367');"+
//                    "}"+
//                    "google.setOnLoadCallback(initialize);";//+
////                    "}";



    @Inject
    private JavaScriptSupport javaScriptSupport;

    @InjectContainer
    private ClientElement element;


//    @AfterRender
//    public void afterRender() {
//        javaScriptSupport.addScript(GOOGLE_BOOKS_VIEWER_SCRIPT);
////        javaScriptSupport.addScript("new Confirm('%s', '%s');", element.getClientId(), message);
//    }



}
