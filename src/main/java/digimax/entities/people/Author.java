package digimax.entities.people;

import digimax.entities.item.Book;
import org.apache.tapestry5.annotations.Property;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;

import java.util.*;

@Entity
public class Author extends Person {

    @Property
    @ManyToMany(mappedBy = "authors")
    public Set<Book> books = new HashSet<Book>();

    public Author() {
        super();
    }

    public Author(String lastName, String firstName) {
        this();
        this.lastName = lastName;
        this.firstName = firstName;
    }

    public List<Book> getSortedBooks() {
        List<Book> sortedBooks = new ArrayList<Book>();
        for (Book book: books) {
            sortedBooks.add(book);
        }
        Collections.sort(sortedBooks, new Book.Compare());
        return sortedBooks;
    }

//    public Long getUid() {
//        return id;
//    }

}
