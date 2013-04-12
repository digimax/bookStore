package digimax.services.app.asset;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.internal.services.AbstractAsset;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetFactory;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/12/13
 * Time: 4:53 PM
 */
public class FilePathAssetFactory implements AssetFactory {


    private final FileAssetAliasManager aliasManager;
    private final String pathPrefix;
    private FileResource rootResource;

    //alias manager necessary for support of different roots
    public FilePathAssetFactory(FileAssetAliasManager aliasManager) {
        //todo (manuel): maybe a resource cache should be used here... look if reusing existing ResourceCacheImpl
        this.aliasManager = aliasManager;

        pathPrefix ="fs/";

        initRootResource();
    }

    private void initRootResource() {
        rootResource = new FileResource(aliasManager, "/");
    }

    public Resource getRootResource() {
        return rootResource;
    }

    public Asset createAsset(final Resource resource) {

        return new AbstractAsset(true) {

            public String toClientURL() {
                return pathPrefix + aliasManager.toClientURL(resource.getPath());
            }

            public Resource getResource() {
                return resource;
            }
        };
    }
}
