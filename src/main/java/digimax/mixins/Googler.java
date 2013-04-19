package digimax.mixins;

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/19/13
 * Time: 12:07 AM
 */
public class Googler {

//        @Parameter(value = "Are you sure?", defaultPrefix = BindingConstants.LITERAL)
//        private String message;

    private static final String GOOGLE_BOOKS_VIEWER_SCRIPT =
            "Tapestry.onDOMLoaded(function() {"+
                    "google.load(\"books\", \"0\");"+
                    "function initialize() {"+
                    "    var viewer = new google.books.DefaultViewer(document.getElementById('viewerCanvas'));"+
                    "    viewer.load('ISBN:0738531367');"+
                    "}"+
                    "google.setOnLoadCallback(initialize);"+
                    "}";



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

