package digimax.services.app.asset;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/12/13
 * Time: 5:41 PM
 */
public interface FileAssetAliasManager {

    String toClientURL(String resourcePath);

    String toResourcePath(String clientURL);
}
