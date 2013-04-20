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
    public int height;

    @Property
    public int width;

    @Property
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
    public Item item;


    public Image() {
        super();
    }

    public Image(String fileName, int width, int height) {
        this();
        this.fileName = fileName;
        this.width = width;
        this.height = height;
    }

}
