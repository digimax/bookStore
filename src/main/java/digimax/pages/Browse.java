package digimax.pages;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeforeRenderBody;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:21 PM
 */
public class Browse {

    @Inject
    private Logger logger;

    @Property
    private String string;

    @Property
    private AssetWrapper assetWrapper;

   @Inject
   @Path("context:imagez/technology_en.jpg")
   @Property
   private org.apache.tapestry5.Asset technologyImage;


    @Inject
//    @Path("file:images/nonexistant.jpg")
    @Path("file:images/digimaxCom.jpg")
    @Property
    private org.apache.tapestry5.Asset digimaxImage;

    @Inject
    @Path("file:images/A_STAINLESS_STEEL_RAT_IS_BORN~HARRY_HARRISON.png")
    @Property
    private org.apache.tapestry5.Asset bookImage;


    @Inject
    @Path("file:images/A_CONSPIRACY_OF_PAPER~DAVID_LISS.png")
    @Property
    private org.apache.tapestry5.Asset bookImage2;


    @Inject
    @Path("file:images/BALD~KEVIN_BALDWIN.png")
    @Property
    private org.apache.tapestry5.Asset bookImage3;

    public Iterable<Asset> getAssets() {
        List<Asset>  assets = new ArrayList<Asset>();
            assets.add(digimaxImage);
            assets.add(bookImage);
            assets.add(bookImage2);
            assets.add(bookImage3);
        return assets;
    }

    public Iterable<AssetWrapper> getAssetWrappers() {
        List<AssetWrapper>  assetWrappers = new ArrayList<AssetWrapper>();
        assetWrappers.add(new AssetWrapper(digimaxImage));
        assetWrappers.add(new AssetWrapper(bookImage));
        assetWrappers.add(new AssetWrapper(bookImage2));
        assetWrappers.add(new AssetWrapper(bookImage3));
        return assetWrappers;
    }

    public Iterable<Integer> getIntegers() {
        List<Integer>  integers = new ArrayList<Integer>();
        integers.add(new Integer(1));
        integers.add(new Integer(2));
        integers.add(new Integer(3));
        integers.add(new Integer(4));
        return integers;
    }

    public Iterable<String> getStrings() {
        List<String>  strings = new ArrayList<String>();
        strings.add("_one");
        strings.add("_two");
        strings.add("_three");
        strings.add("_four");
        return strings;
    }


    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        logger.debug("assets array : {}", toString(getAssets()));
        logger.debug("End Diagnostics");
    }

    private String toString(Iterable<Asset> assets) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append('{');
        for (Asset asset : assets) {
            stringBuilder.append(asset.toString()).append(" ");
        }
        stringBuilder.append('}');
        return stringBuilder.toString();
    }

    public class AssetWrapper {
        private Asset asset;
        public AssetWrapper(Asset asset) {
            this.asset = asset;
        }

        public Asset getAsset() {
            return asset;
        }

        @Override
        public String toString() {
            return super.toString();    //To change body of overridden methods use File | Settings | File Templates.
        }
    }
}
