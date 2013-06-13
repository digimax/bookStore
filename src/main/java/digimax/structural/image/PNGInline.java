package digimax.structural.image;

import org.apache.tapestry5.services.Response;

import java.io.InputStream;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/15/13
 * Time: 6:23 PM
 */
public class PNGInline extends InlineStreamResponse {
        public PNGInline(InputStream is, String... args) {
            super(is, args);
            this.contentType = "image/png";
            this.extension = "png";
        }

    @Override
    public void prepareResponse(Response response) {
        super.prepareResponse(response);
        response.setHeader("Cache-Control", "public, max-age=60000");
        response.setDateHeader("Expires", 60000);
    }
}
