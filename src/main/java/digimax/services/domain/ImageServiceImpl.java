package digimax.services.domain;

import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.ResampleOp;
import digimax.entities.app.Image;
import digimax.structural.ApplicationRuntimeException;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.internal.util.TapestryException;
import org.hibernate.Session;
import sun.security.provider.MD5;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageServiceImpl implements ImageService {

    private static final String FILE_EXTENSION = ".png";
    private static final String SCALED_IMAGE_PREFIX = "small";
    private static final String ROTATED_IMAGE_PREFIX = "horizontal";
    private static final double ROTATED_ANGLE_RADIANS = -1.57079633d;

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
        String tempOldFileName = fileName;
        fileName = DigestUtils.md5Hex(fileName)+FILE_EXTENSION;

        Image fullScaleImage = null;
        File destinationImageFile = new File(appImagesFolderRoot+fileName);
        File tempOldNameImageFile = new File(appImagesFolderRoot+tempOldFileName);
         //TODO: rework TestImages page to use HashedFileName
        try {
            FileUtils.copyFile(sourceImageFile, destinationImageFile);
            FileUtils.copyFile(sourceImageFile, tempOldNameImageFile);
        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format(
                    "ImageServiceImpl IO failure. Failed to create new image file named :: %s", fileName) , e);
        }
        //create scaled image
        String scaledImageFileName = SCALED_IMAGE_PREFIX+fileName;
        Image scaledImage;
        try {
            BufferedImage src = ImageIO.read(destinationImageFile);
            int imageHeight = src.getHeight();
            int imageWidth = src.getWidth();
            fullScaleImage = new Image(fileName, imageWidth, imageHeight);
            float widthToHeightRatio = Float.intBitsToFloat(imageWidth)/Float.intBitsToFloat(imageHeight);
            int scaledWidth = (int) (widthToHeightRatio*SMALL_IMAGE_HEIGHT);
            scaledImage = new Image(scaledImageFileName, scaledWidth, SMALL_IMAGE_HEIGHT);

            ResampleOp  resampleOp = new ResampleOp(scaledWidth , SMALL_IMAGE_HEIGHT);
            resampleOp.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.VerySharp);
            BufferedImage rescaledImage = resampleOp.filter(src, null);
            ImageIO.write(rescaledImage, "PNG",
                    new File(appImagesFolderRoot+scaledImageFileName));
            scaledImage.fileName=scaledImageFileName;

        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format(
                    "ImageServiceImpl IO failure. Failed to createScaledImage :: %s", scaledImageFileName) , e);
        }

        //create rotated image
        String rotatedImageFileName = ROTATED_IMAGE_PREFIX+fileName;
        Image rotatedImage = new Image(rotatedImageFileName, fullScaleImage.height, fullScaleImage.width);
        try {
            BufferedImage sourceBufferedImage = ImageIO.read(sourceImageFile);
            BufferedImage rotatedBufferedImage = rotate(sourceBufferedImage, ROTATED_ANGLE_RADIANS);
            ImageIO.write(rotatedBufferedImage, "PNG",
                    new File(appImagesFolderRoot+rotatedImageFileName));
            rotatedImage.fileName=rotatedImageFileName;
        } catch (IOException e) {
            throw new ApplicationRuntimeException(String.format(
                    "ImageServiceImpl IO failure. Failed to createRotatedImage :: %s", rotatedImageFileName) , e);
        }

        List<Image> createdImages = new ArrayList<Image>();
        createdImages.add(fullScaleImage);
        createdImages.add(scaledImage);
        createdImages.add(rotatedImage);

        return createdImages;

    }

    public static BufferedImage rotate(BufferedImage image, double angle) {
        double sin = Math.abs(Math.sin(angle)), cos = Math.abs(Math.cos(angle));
        int w = image.getWidth(), h = image.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
        GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        GraphicsConfiguration gc = graphicsDevice.getDefaultConfiguration();
//        GraphicsConfiguration gc = getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(angle, w/2, h/2);
        g.drawRenderedImage(image, null);
        g.dispose();
        return result;
    }
}
