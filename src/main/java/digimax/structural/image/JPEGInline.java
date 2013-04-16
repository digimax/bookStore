package digimax.structural.image;

import java.io.InputStream;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/15/13
 * Time: 6:22 PM
 */
public class JPEGInline extends InlineStreamResponse {
    public JPEGInline(InputStream is, String... args) {
        super(is, args);
        this.contentType = "image/jpeg";
        this.extension = "jpg";
    }
}
