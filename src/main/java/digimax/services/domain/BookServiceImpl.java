package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.people.Author;
import digimax.entities.people.Person;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:54 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("unchecked")
public class BookServiceImpl implements BookService {

    @Inject
    private Logger logger;

    @Inject
    private Session session;

    @Inject
    private PersonService personService;

    public Book testInstance() {
        Book book = new Book();
        book.title = "Fung Master Sweet";
        return book;
    }

    public void save(Book book) {
        session.save(book);
    }

    public void delete(Book book) {
        session.delete(book);
    }


    public Book findBook(Book searchBook) {
        if (searchBook.authors==null)
            return null;
        Author author = (searchBook.authors.size()>0)?(Author)searchBook.authors.toArray()[0]:null;
        Criterion condition = null;
        if (author==null) {
//            condition = Restrictions.conjunction().add(Restrictions.eq("title", searchBook.title));
            return null;
        } else {
            condition =
                Restrictions.conjunction().add(Restrictions.eq("title", searchBook.title))
//                        .add(Restrictions.eq("subTitle", searchBook.subTitle))
                        .add(Restrictions.eq("authorz.lastName", author.lastName))
                        .add(Restrictions.eq("authorz.firstName", author.firstName));
        }
//        List<Book> foundBooks = session.createCriteria(Book.class).createAlias("authors", "authorz").add(condition).list();
        Book foundBook = (Book) session.createCriteria(Book.class).createAlias("authors", "authorz").add(condition).uniqueResult();
        return foundBook;
        }

    public List<Book> findBooks(String title) {
        if (title==null || title.trim().length()==0) {
            return null;
        }
        List<Book> books = null;
        books = session.createCriteria(Book.class).add(Restrictions.ilike("title", "%"+title+"%")).list();
        return books;
    }

    public Book newBook(String title, String subTitle, String firstAuthorLastName, String firstAuthorFirstName, String secondAuthorLastName, String secondAuthorFirstName) {
        Book book = new Book();
        Author author = personService.findOrCreateAuthor(firstAuthorLastName, firstAuthorFirstName);
        book.authors.add(author);
        if (secondAuthorLastName!=null && secondAuthorLastName.length()>0) {
            Author secondAuthor = personService.findOrCreateAuthor(secondAuthorLastName, secondAuthorFirstName);
            book.authors.add(secondAuthor);
        }
        book.title = title;
        book.subTitle = subTitle;
        save(book);
        return book;
    }

    public Book findOrCreateBook(Book searchBook) {
        Book book = findBook(searchBook);
        if (book==null) {
            Author author = (Author) searchBook.authors.toArray()[0];
            Author secondAuthor = null;
            if (searchBook.authors.size()>1) {
                secondAuthor = (Author) searchBook.authors.toArray()[1];
            }
            book = newBook(searchBook.title, searchBook.subTitle, author.lastName, author.firstName,
                    (secondAuthor!=null)?secondAuthor.lastName: null,
                    (secondAuthor!=null)?secondAuthor.firstName: null);
        }
        return book;
    }
}
