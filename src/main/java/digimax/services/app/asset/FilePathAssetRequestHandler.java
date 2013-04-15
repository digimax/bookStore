package digimax.services.app.asset;

import org.apache.tapestry5.internal.services.ResourceStreamer;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.Response;
import org.apache.tapestry5.services.assets.AssetRequestHandler;

import java.io.IOException;
import java.util.regex.Pattern;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 10:51 PM
 */
public class FilePathAssetRequestHandler implements AssetRequestHandler {

    private final ResourceStreamer resourceStreamer;

    private final Resource rootContextResource;

    private final Pattern illegal = Pattern.compile("^(((web|meta)-inf.*)|(.*\\.tml$))", Pattern.CASE_INSENSITIVE);

    public FilePathAssetRequestHandler(ResourceStreamer resourceStreamer, Resource rootContextResource)
    {
        this.resourceStreamer = resourceStreamer;
        this.rootContextResource = rootContextResource;
    }

    public boolean handleAssetRequest(Request request, Response response, String extraPath) throws IOException
    {
        if (illegal.matcher(extraPath).matches())
            return false;

        Resource resource = rootContextResource.forFile(extraPath);

        resourceStreamer.streamResource(resource);

        return true;
    }
}
