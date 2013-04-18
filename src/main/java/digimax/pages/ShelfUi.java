package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/17/13
 * Time: 6:45 PM
 */
public class ShelfUi {

    @PageActivationContext
    @Property
    private Shelf shelf;

    @Property
    private Book book;

    public String getLargeImageFileName() {
        Image image = book.images.get(0);
        return image.fileName;
    }

    public String getSmallImageFileName() {
        Image image = book.images.get(1);
        return image.fileName;
    }
}
