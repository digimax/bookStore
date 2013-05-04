package digimax;

import digimax.entities.app.Image;
import digimax.entities.geo.Address;
import digimax.entities.item.Book;
import digimax.entities.people.Author;
import digimax.entities.people.Person;
import digimax.services.domain.BookService;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.test.TapestryTestCase;
import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/7/13
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
public class BookServiceTest extends QaRegistryTest {

    @Test
    public void testSave() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        Book book = new Book();
        book.title = "Crypto Astronomy";
        book.subTitle = "Alone in the Void";

        Author author = new Author();
        author.firstName = "Peter";
        author.lastName = "Townsend";

        Author illustrator = new Author();
        illustrator.firstName = "Pamela";
        illustrator.lastName = "Anderson";

        book.authors.add(author);
        book.authors.add(illustrator);


        bookService.save(book);
        Assert.assertNotNull(book.id);
        Long bookId = book.id;
        Assert.assertEquals(session.getCacheMode(), CacheMode.NORMAL);
        session.evict(book);

        Book persistedBook = (Book) session.createCriteria(Book.class).add(Restrictions.eq("id", bookId)).uniqueResult();
        Assert.assertEquals(persistedBook, book);
        Assert.assertEquals(persistedBook.id, bookId);
        Assert.assertEquals(persistedBook.title,"Crypto Astronomy");
        Assert.assertEquals(persistedBook.subTitle, "Alone in the Void");
        Author persistedAuthor = (Author) persistedBook.authors.toArray()[0];
//        Long persistedAuthorId = persistedAuthor.id;
        Assert.assertEquals(persistedAuthor, author);
        logger.debug("persistedAuthor.id::"+persistedAuthor.id);
        Assert.assertEquals(persistedAuthor.id,author.id);
        Assert.assertEquals(persistedAuthor.firstName, author.firstName);
        Assert.assertEquals(persistedAuthor.lastName,author.lastName);

        //check the bidirectional many-to-many container books for content
//TODO:       Assert.assertEquals(persistedAuthor.books.size(), 1); //this is a required test.
        Assert.assertEquals(session.getFlushMode(), FlushMode.AUTO);
//        session.setFlushMode(FlushMode.ALWAYS);
        int oldAuthorUID = persistedAuthor.hashCode();
        session.evict(persistedAuthor);
        session.evict(book);
        persistedAuthor = (Author) session.get(Author.class, persistedAuthor.id);
//        persistedAuthor = (Author) session.createCriteria(Author.class).add(Restrictions.eq("id", persistedAuthorId)).uniqueResult();
        Assert.assertTrue(persistedAuthor.hashCode() != oldAuthorUID);
        Assert.assertEquals(persistedAuthor.books.size(), 1);

    }

    @Test
    @SuppressWarnings("unchecked")
    public void testDelete() {
    Session session = registry.getService(Session.class);

    BookService bookService = registry.getService(BookService.class);
    Book book = new Book();
    book.title = "Crypto Astronomy";
    book.subTitle = "Alone in the Void";

    Author author = new Author();
    author.firstName = "Peter";
    author.lastName = "Townsend";

    book.authors.add(author);

    bookService.save(book);
    Assert.assertFalse(book.id == null);
    Long bookId = book.id;
    Assert.assertEquals(session.getCacheMode(), CacheMode.NORMAL);
    session.evict(book);

        Book persistedBook = (Book) session.get(Book.class, bookId);

//    Book persistedBook = (Book) session.createCriteria(Book.class).add(Restrictions.eq("id", bookId)).uniqueResult();
    Assert.assertEquals(persistedBook, book);
    Assert.assertEquals(persistedBook.id, bookId);
    Assert.assertEquals(persistedBook.title, "Crypto Astronomy");
    Assert.assertEquals(persistedBook.subTitle, "Alone in the Void");
    Author persistedAuthor = (Author) persistedBook.authors.toArray()[0];
    Assert.assertEquals(persistedAuthor, author);
    logger.debug("persistedAuthor.id::" + persistedAuthor.id);
    Assert.assertEquals(persistedAuthor.id, author.id);
    Assert.assertEquals(persistedAuthor.lastName, author.lastName);
    Assert.assertEquals(persistedAuthor.firstName, author.firstName);
    Assert.assertNotNull(persistedAuthor.books);

    //try deleting book
    bookService.delete(persistedBook);
    List<Book> books = session.createCriteria(Book.class).list();
    Assert.assertEquals(books.size(), 0);
    //ensure the author was not deleted
    List<Author> authors = session.createCriteria(Author.class).list();
    Assert.assertEquals(authors.size(), 1);

    }

    @Test
    public void testFindBook() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);

        Book searchBook = new Book();
        searchBook.title = "How the West Was Won";
        searchBook.authors.add(new Author("Baggins", "Frodo"));

        //try non existing book
        Book nonexistantBook = bookService.findBook(searchBook);
        Assert.assertNull(nonexistantBook);

        Book book1 = bookService.newBook("Rock Wall", null, "Mason", "Master", null, null );
        Assert.assertEquals(book1.authors.size(), 1);

        searchBook = new Book();
        searchBook.title = "Rock Wall";
        searchBook.authors.add(new Author("Mason", "Master"));

        Book persistedBook = bookService.findBook(searchBook);
        Assert.assertNotNull(persistedBook);
        Assert.assertNotNull(persistedBook.id);

        bookService.delete(book1); //shouldn't have to do this on a per test method basis???

    }

    @Test
    public void testFindBooks() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);

        Book book1 = new Book();
        book1.title = "How the West was Won";
        Image book1Image1 = new Image();
        book1Image1.fileName="book1Image1.png";
        book1Image1.item = book1;
        Image book1Image2 = new Image();
        book1Image2.fileName="book1Image2.png";
        book1Image2.item = book1;
        Image book1Image3 = new Image();
        book1Image3.fileName="book1Image3.png";
        book1Image3.item = book1;
        book1.images.add(book1Image1);
        book1.images.add(book1Image2);
        book1.images.add(book1Image3);

        bookService.save(book1);
        Long book1Id = book1.id;
        Assert.assertNotNull(book1Id);

        Book book2 = new Book();
        book2.title = "All about the Roundabout";
        bookService.save(book2);
        Long book2Id = book2.id;
        Assert.assertNotNull(book2Id);

        session.evict(book1);
        session.evict(book2);

        Book persistedBook1 = (Book) session.get(Book.class, book1Id);
        Book persistedBook2 = (Book) session.get(Book.class, book2Id);

        Assert.assertNotNull(persistedBook1);
        Assert.assertNotNull(persistedBook1.id);

        Assert.assertNotNull(persistedBook2);
        Assert.assertNotNull(persistedBook2.id);

        List<Book> persistedBooks = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBooks.size(), 2);

        //Test find by complete title
        List<Book> foundBooks = bookService.findBooks("How the West was Won");
        Assert.assertNotNull(foundBooks);
        Assert.assertEquals(foundBooks.size(), 1);

        //Test find by partial title
        foundBooks = bookService.findBooks("Roundabout");
        Assert.assertNotNull(foundBooks);
        Assert.assertEquals(foundBooks.size(), 1);

        //Test lazy get of Images
        assertEquals(persistedBook1.images.get(0), book1Image1);
        assertEquals(persistedBook1.images.get(1), book1Image2);
        assertEquals(persistedBook1.images.get(2), book1Image3);

    }

    @Test
    public void testNewBook() {
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);

        List<Book> emptyBooks = session.createCriteria(Book.class).list();

        Assert.assertEquals(emptyBooks.size(), 0);

        Book book1 = bookService.newBook("High Tide", null, "Futura", "Peter", null, null );
        Assert.assertNotNull(book1.id);
        Assert.assertEquals(book1.title, "High Tide");
        Author author1 = (Author) book1.authors.toArray()[0];
        Assert.assertEquals(author1.firstName, "Peter");
        Assert.assertEquals(author1.lastName, "Futura");
        Assert.assertEquals(book1.authors.size(), 1);

        Book book2 = bookService.newBook("Far Tide Horizons", null, "Smith", "Sebastian", null, null );
        Assert.assertNotNull(book2.id);
        Assert.assertEquals(book2.title, "Far Tide Horizons");
        Author author2 = (Author) book2.authors.toArray()[0];
        Assert.assertEquals(author2.firstName, "Sebastian");
        Assert.assertEquals(author2.lastName, "Smith");
        Assert.assertEquals(book2.authors.size(), 1);
        
        session.evict(book1);
        session.evict(book2);
        
        List<Book> books = session.createCriteria(Book.class).list();
        
        Book persistedBook1 = books.get(0);
        Book persistedBook2 = books.get(1);
        
        Assert.assertEquals(persistedBook1, book1);
        Assert.assertEquals(persistedBook1.id, book1.id);
        Assert.assertEquals(persistedBook1.title, book1.title);
        Author persistedAuthor1 = (Author) persistedBook1.authors.toArray()[0];
        Assert.assertEquals(persistedAuthor1.lastName, "Futura");
        Assert.assertEquals(persistedBook1.authors.size(), 1);

        Assert.assertEquals(persistedBook2, book2);
        Assert.assertEquals(persistedBook2.id, book2.id);
        Assert.assertEquals(persistedBook2.title, book2.title);
        Author persistedAuthor2 = (Author) persistedBook2.authors.toArray()[0];
        Assert.assertEquals(persistedAuthor2.firstName, "Sebastian");
        Assert.assertEquals(persistedAuthor2.lastName, "Smith");
        Assert.assertEquals(persistedBook2.authors.size(), 1);
    }


    @Test
    @SuppressWarnings("unchecked")
    public void testFindOrCreateBook() {

        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);

        Book book1 = bookService.newBook("Fantasy League",null,"Drew", "Nancy", null, null);
        Book book2 = bookService.newBook("Forever Fantasy",null,"Smithsonia", "Angel", null, null);

        Book searchBook = new Book();
        searchBook.title = "Fantasy League";
        Book unknownBook = bookService.findBook(searchBook);
        Assert.assertNull(unknownBook);

        //setup search criteria for existing book
        Author searchAuthor = new Author("Drew","Nancy");
        searchBook.authors.add(searchAuthor);

//        searchBook.authors.add(author);
        Assert.assertEquals(bookService.findBook(searchBook), book1);

        Book aBook = bookService.findOrCreateBook(searchBook);
        Assert.assertEquals(aBook, book1);
        Assert.assertEquals(aBook.id, book1.id);
        Assert.assertEquals(aBook.title, "Fantasy League");
        Author aBookAuthor = (Author) aBook.authors.toArray()[0];
        Assert.assertEquals(aBookAuthor.lastName, "Drew");
        Assert.assertEquals(aBookAuthor.firstName, "Nancy");

        //set search criteria for unknown new book
        searchBook.title = "Sailing For Boat-Tards";
        aBook = bookService.findOrCreateBook(searchBook);
        //ensure new book was created properly
        Assert.assertNotNull(aBook);
        Assert.assertNotNull(aBook.id);
        aBookAuthor = (Author) aBook.authors.toArray()[0];
        Assert.assertEquals(aBookAuthor.lastName, "Drew");
        Assert.assertEquals(aBookAuthor.firstName, "Nancy");


        Assert.assertFalse(aBook.equals(book1));
        session.evict(book1);
        session.evict(book2);
        session.evict(aBook);
        List<Book> persistedBooks = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBooks.size(), 3);

        Book aPerisitedBook = bookService.findBook(searchBook);
        Assert.assertEquals(aPerisitedBook, aBook);
        Assert.assertEquals(aPerisitedBook.id, aBook.id);

    }

    @BeforeMethod
    public void purgeTables() {
        // cleanup all persisted Books
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        PersonService personService = registry.getService(PersonService.class);

        List<Book> persistedBookList = session.createCriteria(Book.class).list();

        for (Book book: persistedBookList) {
            bookService.delete(book);
        }
        //refetch
        persistedBookList = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBookList.size(),0);

        // cleanup all persisted Authors
        List<Person> persistedPersonList = session.createCriteria(Person.class).list();

        for (Person person: persistedPersonList) {
            personService.delete(person);
        }
        //refetch
        persistedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(persistedPersonList.size(),0);
    }

}
