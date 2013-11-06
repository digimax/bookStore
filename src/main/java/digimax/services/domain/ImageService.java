package digimax.services.domain;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import java.io.File;
import java.util.List;

public interface ImageService {

    Image testInstance();

    @CommitAfter
    void save(Image image);

    @CommitAfter
    void delete(Image image);

    List<Image> createImages(String appImagesFolderRoot, String fileName, File sourceImageFile);

}
