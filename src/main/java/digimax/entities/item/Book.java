package digimax.entities.item;

import digimax.entities.people.Author;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;

import javax.persistence.*;

import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.Cascade;
import org.hibernate.cache.ReadWriteCache;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Book extends Item {

    @Property
    @Validate("required")
    public String title;

    @Property
    public String subTitle;

    @Property
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
//    @JoinTable(name="Book_Author",
//            joinColumns={@JoinColumn(name="Book_id")},
//            inverseJoinColumns={@JoinColumn(name="Author_id")})
    public List<Author> authors = new ArrayList<Author>();


    @Property
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
//    @JoinColumn(name="shelf_id")
    public Shelf shelf;

    public Book() {
        super();
        subTitle="";
    }


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }
}
