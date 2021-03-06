package digimax.components;

import digimax.services.app.BootupServiceImpl;
import digimax.structural.ApplicationRuntimeException;
import digimax.structural.image.PNGInline;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.internal.structure.Page;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Response;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/16/13
 * Time: 1:13 PM
 */
@SupportsInformalParameters //TODO: find a way to get this working, currently seems to do nothing
public class Png {

    private static final String FILE_EXTENSION = ".png";
    @Inject
    private Logger logger;

    @Inject
    private ComponentResources componentResources;

    @Inject
    private Response response;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.VAR)
    private int width;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.VAR)
    private int height;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String altMessage;

    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private String fileName;


    public Link getStreamLink() {
        Link streamLink = componentResources.createEventLink("images", new Object[] {fileName});
        return streamLink;
    }

//

    public StreamResponse onImages(final String imageFileName) {
        logger.debug("onImages called with imageFileName :: {}",imageFileName);
        //TODO: rework this condition to use Regex
        if (imageFileName==null || !imageFileName.toLowerCase().contains(FILE_EXTENSION))  {
            return null;
        }
        String fullyQualifiedFileName = BootupServiceImpl.APP_IMAGE_FOLDER+imageFileName;

        File file = new File(fullyQualifiedFileName);
        boolean isExisting = file.exists();
        logger.debug("onImages. file exists? :: {}", isExisting);
        if (!isExisting) {
            return null;
        }

        InputStream inputStream;
        try {
            inputStream =
                    new BufferedInputStream(file.toURI().toURL().openStream());
        } catch (IOException e) {
            throw new ApplicationRuntimeException("onImages. Failed to open input stream, fileName :: "+fullyQualifiedFileName, e);
        }
        return new PNGInline(inputStream, imageFileName);
    }
}
