package digimax;

import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.people.Author;
import digimax.services.domain.BookService;
import digimax.services.domain.LibraryService;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.test.TapestryTestCase;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/7/13
 * Time: 6:27 PM
 * To change this template use File | Settings | File Templates.
 */

@SuppressWarnings("unchecked")
public class LibraryServiceTest extends QaRegistryTest {

    private ApplicationStateManager applicationStateManager;

    @BeforeMethod
    public void setUp() {
        applicationStateManager =
                mockApplicationStateManager();
//        authenticator = new AuthenticatorImpl(
//                userDao, applicationStateManager);
        logger.info("LibraryServiceTest@BeforeMethod setUp()");
    }

    @Test
    public void testSave() {
        Assert.assertTrue(true);
    }


    @Test
    public void testDelete() {
        Assert.assertTrue(true);
    }

    @Test
    void testReceive() {
        Session session = registry.getService(Session.class);
        BookService bookService = registry.getService(BookService.class);
        LibraryService libraryService = registry.getService(LibraryService.class);

        Book searchBook = new Book();
        searchBook.title = "East Malaysia";
        Author searchAuthor = new Author("Rotten","Jonny");
        searchBook.authors.add(searchAuthor);

        Book book1 = bookService.findOrCreateBook(searchBook);
        Assert.assertNotNull(book1);
        Assert.assertNotNull(book1.id);
        Assert.assertEquals(book1.title, "East Malaysia");
        Author book1Author = book1.authors.get(0);
        Assert.assertEquals(book1Author.firstName, "Jonny");
        Assert.assertEquals(book1Author.lastName, "Rotten");

        searchBook.title = "Life in the Whole";
        Book book2 = bookService.findOrCreateBook(searchBook);
        Assert.assertNotNull(book2);
        Assert.assertNotNull(book2.id);
        Assert.assertEquals(book2.title, "Life in the Whole");
        Author book2Author = book2.authors.get(0);
        Assert.assertEquals(book2Author.firstName, "Jonny");
        Assert.assertEquals(book2Author.lastName, "Rotten");

        Assert.assertEquals(book2Author, book1Author);
        Assert.assertEquals(book2Author.id, book1Author.id);

        List<Book> persistedBooks = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBooks.size(), 2);

        List<Book> newBooks = new ArrayList<Book>();
        newBooks.add(book1);
        newBooks.add(book2);

        Library library =  libraryService.testInstance();
        libraryService.receive(library, newBooks);
        Long libraryId = library.id;
        Assert.assertNotNull(libraryId);

        session.evict(book1);
        session.evict(book2);
        session.evict(library);

        library = (Library) session.get(Library.class, libraryId);

        List<Book> booksOnShelves = new ArrayList<Book>();
        for (Shelf shelf : library.shelves) {
            logger.debug(String.format("LibraryService received Book::%s : %s",shelf, shelf.name));
            for (Book book : shelf.books) {
                booksOnShelves.add(book);
                logger.debug(String.format("LibraryService received Book::%s : %s",book, book.title));
            }
        }

        Assert.assertEquals(booksOnShelves.size(), 2);
        Assert.assertTrue(booksOnShelves.contains(book1));
        Assert.assertTrue(booksOnShelves.contains(book2));

        Book aBook =  booksOnShelves.get(0);
        Assert.assertNotNull(aBook);

        Book bBook =  booksOnShelves.get(1);
        Assert.assertNotNull(bBook);

        Shelf shelf = aBook.shelf;
        Assert.assertNotNull(shelf);
        Assert.assertEquals(shelf.books.size(), 2);

    }
}
