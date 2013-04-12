package digimax.services.app.asset;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.internal.bindings.AssetBinding;
import org.apache.tapestry5.ioc.Location;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.BindingFactory;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/12/13
 * Time: 5:24 PM
 */
public class FileBindingFactory implements BindingFactory {

    private final AssetSource source;
    private final Resource resource;

    public FileBindingFactory(AssetSource source, FilePathAssetFactory factory) {
        this.source = source;
        this.resource = factory.getRootResource();
    }

    public Binding newBinding(String description, ComponentResources container, ComponentResources component,
                              String expression, Location location) {
        Asset asset = source.getAsset(resource, expression, container.getLocale());

        return new AssetBinding(location, description, asset);
    }
}
