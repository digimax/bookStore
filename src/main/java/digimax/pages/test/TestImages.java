package digimax.pages.test;

import digimax.services.app.BootupServiceImpl;
import digimax.structural.ApplicationRuntimeException;
import digimax.structural.image.PNGInline;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Path;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/16/13
 * Time: 3:44 PM
 */
public class TestImages {

    @Inject
    ComponentResources componentResources;

    @Inject
    private LinkSource linkSource;

    @Inject
    private Logger logger;

    @Property
    private String string;

    public String getFilePageImageName() {
        return null;//        return uploadStore.getUploadedFile("SOMEUUID");
    }

    @Property
//    @SuppressWarnings("unused")
    private AssetWrapper assetWrapper;

    @Inject
    @Path("context:imagez/technology_en.jpg")
    @Property
    private org.apache.tapestry5.Asset technologyImage;


    @Inject
//    @Path("file:images/nonexistant.jpg")
    @Path("file:images/THE_RELIGION~TIM_WILLOCKS.png")
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

    public Link getImageStreamLink() {
        Link streamLink = linkSource.createPageRenderLink(
                "test/"+TestImages.class.getSimpleName(), false, new Object[]{"THE_QUEEN_OF_THE_DAMNED~ANNE_RICE.png"});
        return streamLink;
    }

    public StreamResponse onActivate(final String imageFileName) {
        logger.debug("onActivate called with imageFileName :: {}",imageFileName);
//        String fileName = "A_COSMIC_CORNUCOPIA~JOSH_KIRBY.png";
        String fullyQualifiedFileName = /*"file:"+*/BootupServiceImpl.APP_IMAGE_FOLDER+imageFileName;

        File file = new File(fullyQualifiedFileName);
        logger.debug("onActivate. file exists? :: {}",file.exists());

        InputStream inputStream;
        try {
            inputStream =
                    new BufferedInputStream(file.toURI().toURL().openStream());
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Failed to open input stream, fileName :: "+fullyQualifiedFileName, e);
        }
        return new PNGInline(inputStream, imageFileName);
    }

    public Iterable<Asset> getAssets() {
        List<Asset> assets = new ArrayList<Asset>();
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

    public StreamResponse getImageStream() {
        return onSubmit();
    }

    public StreamResponse onSubmit() {
        //Note that you could provide an absolute path here, like H:\\LOLCATZ.MP3
        String fileName = "A_COSMIC_CORNUCOPIA~JOSH_KIRBY.png";
        String fullyQualifiedFileName = /*"file:"+*/BootupServiceImpl.APP_IMAGE_FOLDER+fileName;

        File file = new File(fullyQualifiedFileName);
        logger.debug("onSubmit. file exists? :: {}",file.exists());

        InputStream inputStream;
        try {
            inputStream =
                    new BufferedInputStream(file.toURL().openStream());
        } catch (IOException e) {
            throw new ApplicationRuntimeException("Failed to open input stream, fileName :: "+fullyQualifiedFileName, e);
        }
        return new PNGInline(inputStream, fileName);
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
