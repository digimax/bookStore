package digimax.entities.item;

import digimax.entities.app.Image;
import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
//@Proxy(lazy=false)
@Inheritance(strategy=InheritanceType.JOINED)
public class Item extends DomainObject {

    public static final Float DEFAULT_PRICE = new Float(119.99);

    @Property
    @NonVisual
    @Validate("required")
    public Float price;

    @Property
    @OneToMany(mappedBy="item", fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL} )
    @Fetch(FetchMode.SUBSELECT)
//            cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
//    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
    public List<Image> images = new ArrayList<Image>();

    public Float getCalculatedPrice() {
        if (price==null) {
            price = DEFAULT_PRICE;
        }
        return price;
    }

    public String getFormattedPrice() {
        return NumberFormat.getCurrencyInstance().format(getCalculatedPrice());
    }

}
