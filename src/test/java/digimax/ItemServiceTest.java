package digimax;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.Item;
import digimax.entities.people.Author;
import digimax.entities.people.Person;
import digimax.services.domain.BookService;
import digimax.services.domain.ImageService;
import digimax.services.domain.ItemService;
import digimax.services.domain.PersonService;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

@SuppressWarnings("unchecked")
public class ItemServiceTest extends QaRegistryTest {

    @Test
    public void testInstance() {
        ItemService itemService = registry.getService(ItemService.class);
        Item testItem = itemService.testInstance();
        Assert.assertNotNull(testItem);
    }

    @Test
    public void testSave() {
        Session session = registry.getService(Session.class);
        BookService bookService = registry.getService(BookService.class);
//        ImageService imageService = registry.getService(ImageService.class);
//        ItemService itemService = registry.getService(ItemService.class);
        Book searchBook = new Book();
        Author author = new Author();
        author.firstName = "Jade";
        author.lastName = "Rosebud";
        searchBook.authors.add(author);
        searchBook.title = "Missing at Sea";

        Book book = bookService.findOrCreateBook(searchBook);

        Assert.assertNotNull(book);
        Long bookId = book.id;
        Assert.assertNotNull(bookId);

        Image largeImage = new Image("MISSING_AT_SEA.png", 1000, 500);
        Image smallImage = new Image("smallMISSING_AT_SEA.png", 100, 50);

        largeImage.item = book;
        smallImage.item = book;
        book.images.add(largeImage);
        book.images.add(smallImage);

        bookService.save(book);
        Assert.assertEquals(book.images.size(), 2);
        session.evict(book);
        session.evict(largeImage);
        session.evict(smallImage);
        book = null;
        largeImage = null;
        smallImage = null;

        List<Image> persistedImages = session.createCriteria(Image.class).list();
        Assert.assertEquals(persistedImages.size(), 2);

        Book persistedBook = (Book) session.get(Book.class, bookId);
        Assert.assertNotNull(persistedBook);
        Assert.assertEquals(persistedBook.images.size(), 2);
    }


    @BeforeMethod
    public void purgeTables() {
        // cleanup all persisted Books
        Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);
        ImageService imageService = registry.getService(ImageService.class);

        List<Book> persistedBookList = session.createCriteria(Book.class).list();

        for (Book book: persistedBookList) {
            bookService.delete(book);
        }
        //refetch
        persistedBookList = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBookList.size(),0);

        // cleanup all persisted Images
        List<Image> persistedImageList = session.createCriteria(Image.class).list();

        for (Image image: persistedImageList) {
            imageService.delete(image);
        }
        //refetch
        persistedImageList = session.createCriteria(Image.class).list();
        Assert.assertEquals(persistedImageList.size(),0);
    }    


}
