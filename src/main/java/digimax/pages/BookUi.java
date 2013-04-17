package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.people.Author;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/16/13
 * Time: 7:15 PM
 */
public class BookUi {

    @Inject
    private Logger logger;

    @Property
    private Author author;

    @PageActivationContext
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
