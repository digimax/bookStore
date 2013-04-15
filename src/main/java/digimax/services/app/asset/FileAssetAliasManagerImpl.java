package digimax.services.app.asset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/12/13
 * Time: 5:43 PM
 */
public class FileAssetAliasManagerImpl implements FileAssetAliasManager {

    private Logger logger = LoggerFactory.getLogger(FileAssetAliasManagerImpl.class);

    private final AssetPathPrefixAliasMap pathPrefixAliasMap;

    /**
     * @param configuration For provision of filesystem root mappings (e.g. fs/1/xxx -> /srv/img1/, fs/2/xxx -> /tmp/img2)
     */
    public FileAssetAliasManagerImpl(Map<String, String> configuration)
    {
        checkRootPathsContainUriFilesystemSyntax(configuration.values());
        pathPrefixAliasMap = new AssetPathPrefixAliasMap(configuration);
    }

    private void checkRootPathsContainUriFilesystemSyntax(Collection<String> rootFilepaths)
    {
        for (String filepath : rootFilepaths)
        {
            check(filepath.matches("^((file:///\\w)|(file:/\\w)).*?"),
                    "Wrong filepath syntax, does not match filesystem root URI schema (e.g. file:/srv or file:///d:/tmp");
        }
    }

    /**
     * @param resourcePath Resource path of asset. It must start with a filesystem root resource (defined inside
     *                     configurations). It is the path xxx, passed inside the @Asset (filesystem:xxx)
     * @return Client url representation of resource path.
     */
    public String toClientURL(String resourcePath)
    {

        checkFilesystemAliasGotDefined(resourcePath);

        return resourcePath;
    }

    private void checkFilesystemAliasGotDefined(String resourcePath)
    {
        String filesystemAlias = getFilesystemAlias(resourcePath);

        check(filesystemAlias != null,
                String.format("Filesystem alias (first snippet of resource path) is unknown %s. This " +
                        "was resource cannot be mapped to a filepath", resourcePath));
    }

    /**
     *
     */
    public String toResourcePath(String clientURL)
    {
        String filesystemAlias = getFilesystemAlias(clientURL); //TODO : WORKIN

        check(filesystemAlias != null,
                String.format("Filesystem resource cannot be retrieved, because client-url %s does not contain " +
                        "a registered filesystem alias, which could be mapped to filesystem.", clientURL));

        String filesystemRoot = pathPrefixAliasMap.getAliasToPathPrefix().get(filesystemAlias);

        logger.debug(clientURL.replaceFirst(filesystemAlias, filesystemRoot));
        return clientURL.replaceFirst(filesystemAlias, filesystemRoot);
    }

    private String getFilesystemAlias(String clientURL)
    {
        for (String alias : pathPrefixAliasMap.getSortedAliases())
        {
            if (clientURL.contains(alias))
                return alias;
        }
        return null;
    }

    public static boolean check(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }

        return expression;

    }
}
