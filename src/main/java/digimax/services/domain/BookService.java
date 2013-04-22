package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.people.Author;
import digimax.entities.people.Person;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import java.util.List;

public interface BookService {

    public Book testInstance();

    @CommitAfter
    void save(Book book);

    @CommitAfter
    void delete(Book book);

    Book findBook(Book searchBook);

    List<Book> findBooks(String title);

    @CommitAfter
    Book newBook(String title, String subTitle, String firstAuthorLastName, String firstAuthorFirstName
            ,String secondAuthorLastName, String secondAuthorFirstName);

    @CommitAfter
    Book findOrCreateBook(Book searchBook);

}

