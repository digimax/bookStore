package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.people.Author;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/17/13
 * Time: 6:30 PM
 */
public class AuthorUi {

    @PageActivationContext
    @Property
    private Author author;

    @Property
    Book book;

    public Image getSmallImage() {
        Image image = book.images.get(1);
        return image;
    }
}
