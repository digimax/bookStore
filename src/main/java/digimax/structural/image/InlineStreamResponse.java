package digimax.structural.image;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/15/13
 * Time: 6:18 PM
 */
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

import java.io.IOException;
import java.io.InputStream;


public class InlineStreamResponse implements StreamResponse {
    private InputStream inputStream = null;

    protected String contentType = "text/plain";// this is the default

    protected String extension = "txt";

    protected String filename = "default";

    public InlineStreamResponse(InputStream inputStream, String... args) {
        this.inputStream = inputStream;
        if (args != null) {
            this.filename = args[0];
        }
    }

    public String getContentType() {
        return contentType;
    }

    public InputStream getStream() throws IOException {
        return inputStream;
    }

    public void prepareResponse(Response response) {
        response.setHeader("Content-Disposition", "inline; filename=" + filename
                + ((extension == null) ? "" : ("." + extension)));
    }

}
