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
import java.util.ArrayList;
import java.util.List;

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
    public List<Book> books = new ArrayList<Book>();


    public Shelf(String name) {
        this();
        this.name = name;
    }

    public Shelf() {
        super();
    }

}
