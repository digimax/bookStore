package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.people.Author;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

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

    public Image getSmallImage() {
        Image image = book.images.get(1);
        return image;
    }

    public String getAuthorName() {
        return ((Author)book.authors.toArray()[0]).getFullName();
    }

    public List<Book> getSortedBooks() {
        List<Book> sortedBooks = shelf.books;
        Collections.sort(sortedBooks, new Book.Compare());
        return sortedBooks;
    }

}
