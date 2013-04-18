package digimax;

import digimax.entities.item.Book;
import digimax.entities.item.BookMeta;
import digimax.entities.people.Person;
import digimax.services.domain.BookMetaService;
import digimax.services.domain.BookService;
import digimax.services.domain.PersonService;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/18/13
 * Time: 12:38 PM
 */
@SuppressWarnings("unchecked")
public class BookMetaServiceTest extends QaRegistryTest {

    //https://isbndb.com/api/books.xml?access_key=IP3U2HMG&index1=combined&value1=jesus_incident
    //title: JESUS INCIDENT
    //author: FRANK HERBERT


    @Test
    public void testPopulateBookMeta() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        BookMetaService bookMetaService = registry.getService(BookMetaService.class);

        Book book = bookService.newBook("JESUS INCIDENT", null, "FRANK", "HERBERT", null, null);

        Assert.assertNotNull(book);
        Assert.assertNotNull(book.id);
        Long bookId = book.id;

        bookMetaService.populateBookMeta(book);
        Assert.assertNotNull(book.bookMeta);
        Assert.assertNotNull(book.bookMeta.id);

        Assert.assertNotNull(book.bookMeta.book);
        Assert.assertNotNull(book.bookMeta.book.id);

        session.evict(book.bookMeta);
        session.evict(book);

        Book persistedBook = (Book) session.get(Book.class, bookId);
        Assert.assertNotNull(persistedBook);
        Assert.assertNotNull(persistedBook.id);
        Assert.assertNotNull(persistedBook.bookMeta);
        Assert.assertNotNull(persistedBook.bookMeta.id);

        Assert.assertNotNull(persistedBook.bookMeta.book);
        Assert.assertNotNull(persistedBook.bookMeta.book.id);
    }

    @BeforeMethod
    public void purgeTables() {
        // cleanup all persisted Books
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        BookMetaService bookMetaService = registry.getService(BookMetaService.class);

        List<Book> persistedBookList = session.createCriteria(Book.class).list();

        for (Book book: persistedBookList) {
            bookService.delete(book);
        }
        //refetch
        persistedBookList = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBookList.size(), 0);

        // cleanup all persisted Authors
        List<BookMeta> persistedBookMetaList = session.createCriteria(BookMeta.class).list();

        for (BookMeta bookMeta: persistedBookMetaList) {
            bookMetaService.delete(bookMeta);
        }
        //refetch
        persistedBookMetaList = session.createCriteria(BookMeta.class).list();
        Assert.assertEquals(persistedBookMetaList.size(),0);
    }    
}
