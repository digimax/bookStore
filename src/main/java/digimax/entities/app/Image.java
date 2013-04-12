package digimax.entities.app;

import digimax.entities.item.Item;
import digimax.entities.item.Shelf;
import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;


@Entity
public class Image extends DomainObject {

    @Property
    public String fileName;

    @Property
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
    public Item item;


    public Image() {
        super();
    }

    public Image(String fileName) {
        this();
        this.fileName = fileName;
    }

}
