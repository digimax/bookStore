package digimax.entities.people;

import digimax.entities.item.Book;
import org.apache.tapestry5.annotations.Property;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Author extends Person {

    @Property
    @ManyToMany(fetch = FetchType.LAZY,
            mappedBy="authors")
    public List<Book> books = new ArrayList<Book>();

    public Author() {
        super();
    }

    public Author(String lastName, String firstName) {
        this();
        this.lastName = lastName;
        this.firstName = firstName;
    }

//    public Long getUid() {
//        return id;
//    }

}
