package digimax.services.domain;

import digimax.entities.geo.Address;
import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.store.Store;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 5:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class LibraryServiceImpl implements LibraryService {

    @Inject
    private Logger logger;

    @Inject
    private Session session;

    @Inject
    private BookService bookService;

    @Inject
    private LocationService locationService;

    @Inject
    private PersonService personService;

    public Library testInstance() {
        Library library = new Library();
        library.name = "MAW Non Fiction";
        library.address = (Address) locationService.testInstance();
        library.shippingAddress =  (Address) locationService.testInstance();
    return library;
    }

    public Book locate(Library library, String title, String subTitle, String authorLastName, String authorFirstName) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Shelf findShelf(Library library, String locationName) {
        Shelf foundShelf = null;
        for (Shelf shelf: library.shelves) {
            if (shelf.name == locationName) {
                foundShelf = shelf;
                break;
            }
        }
        return foundShelf;
    }

    public void purchase(Library library, List<Book> books) {
        //@TODO: simple general ledger
        receive(library, null, books);
    }

    public void receive(Library library, Shelf shelf, List<Book> books) {
        for (Book book : books) {
            book.shelf = shelf;
            logger.debug(String.format("LibraryServiceImpl :: receive %s : %s", book, book.title));
            bookService.save(book);
            logger.debug(String.format("LibraryServiceImpl :: receive %s : %s", book.shelf, book.shelf.name));
        }
        save(library);
    }


    public void save(Library library) {
        session.save(library);
    }

    public void delete(Library library) {
        session.delete(library);
    }
}
