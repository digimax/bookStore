package digimax.entities.library;

import digimax.entities.geo.Address;
import digimax.entities.item.Inventory;
import digimax.entities.item.Item;
import digimax.entities.item.Shelf;
import digimax.entities.people.Person;
import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@SuppressWarnings("unchecked")
@Entity
public class Library extends DomainObject implements Inventory {

    @Property
    @Validate("required")
    public String name;

    @Property
    @OneToOne(cascade = CascadeType.ALL)
    public Address address;

    @Property
    @OneToOne(cascade = CascadeType.ALL)
    public Address shippingAddress;


    @Property
    @OneToOne(cascade = CascadeType.ALL)
    public Bins bins;

    @Property
    @OneToMany(cascade = CascadeType.ALL)
    public List<Person> users = new ArrayList<Person>();

    @Property
    @OneToMany(cascade = CascadeType.ALL)
    public List<Shelf> shelves = new ArrayList<Shelf>();


    public Set<Item> items() {
        Set items = new HashSet<Item>();
        for (Shelf shelf: shelves) {
            items.add(shelf.books);
        }
        return items;
    }
}
