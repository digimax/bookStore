package digimax.services.app.asset;

import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.AbstractResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/12/13
 * Time: 5:39 PM
 */
public class FileResource extends AbstractResource {

    private final FileAssetAliasManager aliasManager;

    /**
     * @param path         Path of filesystem resource, it contains filesystem alias.
     * @param aliasManager Alias manager so filesystem symbol in asset path can be mapped.
     *
     * @throws IllegalArgumentException rootDirectory is null, rootDirectory does not exist.
     */
    //using URI, so some warranty of string-pattern is given
    public FileResource(FileAssetAliasManager aliasManager, String path) {
        super(path);

        this.aliasManager = aliasManager;

    }

    /**
     * {@inheritDoc}
     *
     * @throws RuntimeException Bad filesystem-path pattern was passed in method newResource().
     */
    public URL toURL() {
        URI url = transformPathToUrl();
        try {
            if (new File(url).exists()) {
                return url.toURL();
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException(
                    String.format("Filesystem resource URL cannot be created, given relative "
                            + "path above filesystem root (%s) has bad pattern", getPath()), e);
        }

    }

    private URI transformPathToUrl() {
        URI url;
        try {
            String resourcePath = aliasManager.toResourcePath(getPath());
            url = new URI(resourcePath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(
                    String.format("Filesystem resource URL cannot be created, given relative "
                            + "path above filesystem root (%s) has bad pattern", getPath()), e);
        }
        return url;
    }

    @Override
    public InputStream openStream() throws IOException {
        return super.openStream();
    }

    @Override
    protected Resource newResource(String path) {
        return new FileResource(aliasManager, path);
    }

    @Override
    public String toString() {
        return "filesystem:" + getPath();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FileResource)) {
            return false;
        }

        FileResource that = (FileResource) o;

        if (aliasManager != null ? !aliasManager.equals(that.aliasManager) : that.aliasManager != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return aliasManager != null ? aliasManager.hashCode()+getFile().hashCode() : 0;
    }
}