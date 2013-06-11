package digimax.entities.item;

import digimax.entities.geo.Address;
import digimax.entities.geo.Location;
import digimax.entities.library.Bins;
import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 2:09 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Shelf extends Location {

    @Property
    @OneToMany(mappedBy="shelf")//, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
//    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
    public Set<Book> books = new HashSet<Book>();


    public Shelf(String name) {
        this();
        this.name = name;
    }

    public Shelf() {
        super();
    }

    public static final class Compare implements Comparator<Shelf> {
        @Override
        public int compare(Shelf shelf1, Shelf shelf2) {
            return shelf1.name.compareTo(shelf2.name);
//            return (shelf1.name.compareTo(shelf2.name))?-1:(shelf1.name.equals(shelf2.name)?0:1); //TODO: implement this method
        }
    }

    public List<Book> getSortedBooks() {
        List<Book> sortedBooks = new ArrayList<Book>();
        for (Book book: books) {
            sortedBooks.add(book);
            Collections.sort(sortedBooks, new Book.Compare());
        }
        return sortedBooks;
    }

}
