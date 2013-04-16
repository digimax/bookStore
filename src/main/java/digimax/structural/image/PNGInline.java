package digimax.structural.image;

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
    }
