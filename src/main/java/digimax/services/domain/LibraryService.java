package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.people.Person;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LibraryService {

    Library testInstance();
    Book locate(Library library, String title, String subTitle, String authorLastName, String authorFirstName );

    Shelf findShelf(Library library, String locationName);

    @CommitAfter
    void purchase(Library library, List<Book> books);

    @CommitAfter
    void receive(Library library, List<Book> books);

    @CommitAfter
    void save(Library library);

    @CommitAfter
    void delete(Library library);
}
