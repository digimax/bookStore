package digimax;

import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.people.Person;
import digimax.services.domain.BookService;
import digimax.services.domain.LibraryService;
import digimax.services.domain.LocationService;
import digimax.services.domain.PersonService;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("unchecked")
public class LocationServiceTest extends QaRegistryTest {

    @BeforeMethod
    public void purgeTables() {

        Session session = registry.getService(Session.class);

        // cleanup all persisted Shelves
        List<Shelf> persistedShelfList = session.createCriteria(Shelf.class).list();

        BookService bookService = registry.getService(BookService.class);
        LocationService locationService = registry.getService(LocationService.class);
        for (Shelf shelf: persistedShelfList) {
            locationService.delete(shelf);
            List<Book> shelvedBooks = shelf.books;
            for (Book book : shelvedBooks) {
                book.shelf = null;
                bookService.delete(book);
            }
            locationService.delete(shelf);
        }
        //refetch
        persistedShelfList = session.createCriteria(Shelf.class).list();
        Assert.assertEquals(persistedShelfList.size(),0);

        // cleanup all persisted Books


        List<Book> persistedBookList = session.createCriteria(Book.class).list();

        for (Book book: persistedBookList) {
            book.shelf = null;
            bookService.delete(book);
        }
        //refetch
        persistedBookList = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBookList.size(),0);
    }

//    @Test
    public void testSaveShelf() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        LocationService locationService = registry.getService(LocationService.class);


        Shelf shelf = new Shelf("Medium Shelf");

        Book book1 = bookService.newBook("Fast Times", null, "Dude", "Gnarly", null, null);
        Book book2 = bookService.newBook("Decorative Arts", null, "Vincent", "Millicent", null, null);
        Book book3 = bookService.newBook("Iced Under", null, "Vincent", "Millicent", null, null);

        session.evict(book1);
        session.evict(book2);
        session.evict(book3);
        List<Book> persistedBooks = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBooks.size(), 3);


        shelf.books.add(persistedBooks.get(0));
        shelf.books.add(persistedBooks.get(1));
        shelf.books.add(persistedBooks.get(2));

        Assert.assertEquals(shelf.books.get(0), book1);
        Assert.assertEquals(shelf.books.get(1), book2);
        Assert.assertEquals(shelf.books.get(2), book3);

        Assert.assertNotNull(shelf.books.get(0).id);
        Assert.assertNotNull(shelf.books.get(1).id);
        Assert.assertNotNull(shelf.books.get(2).id);

        locationService.save(shelf);

        session.evict(shelf);

//        Assert.assertNotNull(book1.shelf);
//        Assert.assertNotNull(book2.shelf);
//        Assert.assertNotNull(book3.shelf);
//
//        Long shelfId = shelf.id;
//        Book persistedBook1 = shelf.books.get(0);
//        Assert.assertNotNull(shelfId);
//        Assert.assertNotNull(persistedBook1);
//        Assert.assertNotNull(persistedBook1.shelf);
//        Assert.assertEquals(persistedBook1.shelf.books.size() , 3);
//
//        session.evict(shelf);
//        Shelf persistedShelf = (Shelf) session.get(Shelf.class, shelfId);
//        Assert.assertNotNull(persistedShelf);
//        Assert.assertNotNull(persistedShelf.id);
//
//        persistedBooks = session.createCriteria(Book.class).list();
//        Assert.assertEquals(persistedBooks.size(), 3);
//
//        persistedBook1 = persistedBooks.get(0);
//        Book persistedBook2 = persistedBooks.get(1);
//        Book persistedBook3 = persistedBooks.get(2);


        //TODO: fix this case. when saving a shelf, not cascaded to books. the book.shelf is null
//        Assert.assertNotNull(persistedBook1.shelf);
    }

    @Test
    public void testSaveBook() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        LibraryService libraryService = registry.getService(LibraryService.class);
        LocationService locationService = registry.getService(LocationService.class);
        Library library = libraryService.testInstance();

        Book book = bookService.newBook("Higher and Higher", null, "Shooster", "Simon", null, null);

        Shelf shelf = new Shelf("Small Shelf");
        book.shelf = shelf;

        bookService.save(book);
        Assert.assertNotNull(book.shelf);

        session.evict(book);
        session.evict(shelf);


        Book persistedBook = (Book) session.createCriteria(Book.class).uniqueResult();
        Assert.assertNotNull(persistedBook.shelf);
        Assert.assertTrue(persistedBook.shelf.books.size() == 1);

        Assert.assertEquals(persistedBook, book);
        Assert.assertEquals(persistedBook.shelf, shelf);

    }

}
