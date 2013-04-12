package digimax.services.domain;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import digimax.entities.app.Image;
import digimax.structural.ApplicationRuntimeException;
import org.apache.commons.io.FileUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.hibernate.Session;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageServiceImpl implements ImageService {

    private static int SMALL_IMAGE_HEIGHT = 300;

    @Inject
    private Session session;


    public Image testInstance() {
        Image image = new Image();
        return image;
    }

    public void save(Image image) {
        session.save(image);
    }

    public void delete(Image image) {
        session.delete(image);
    }

    public List<Image>createImages(String appImagesFolderRoot, String fileName, File sourceImageFile) {
        Image fullScaleImage = new Image(fileName);
        File destinationImageFile = new File(appImagesFolderRoot+fileName);
        try {
            FileUtils.copyFile(sourceImageFile, destinationImageFile);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format("ImageServiceImpl IO failure. Failed to create new image file named :: %s", fileName) , e);
        }
        //create scaled image
        String scaledImageFileName = "small{"+File.pathSeparatorChar+fileName;
        Image scaledImage = new Image(scaledImageFileName);
        try {
            BufferedImage src = ImageIO.read(destinationImageFile);
            int imageHeight = src.getHeight();
            int imageWidth = src.getWidth();
            float widthToHeightRatio = Float.intBitsToFloat(imageWidth)/Float.intBitsToFloat(imageHeight);
            int scaledWidth = (int) (widthToHeightRatio*SMALL_IMAGE_HEIGHT);
            ResampleOp  resampleOp = new ResampleOp(scaledWidth , SMALL_IMAGE_HEIGHT);
            resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
            BufferedImage rescaledImage = resampleOp.filter(src, null);
            ImageIO.write(rescaledImage, "PNG",
                    new File(appImagesFolderRoot+scaledImageFileName));
            scaledImage.fileName=scaledImageFileName;

        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format("ImageServiceImpl IO failure. Failed to createScaledImage :: %s", scaledImageFileName) , e);
        }

        List<Image> createdImages = new ArrayList<Image>();
        createdImages.add(fullScaleImage);
        createdImages.add(scaledImage);

        return createdImages;

    }
}
