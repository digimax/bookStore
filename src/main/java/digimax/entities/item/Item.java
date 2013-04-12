package digimax.entities.item;

import digimax.entities.app.Image;
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
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Item extends DomainObject {

    @Property
    @Validate("required")
    public Float price;

    @Property
    @OneToMany(mappedBy="item", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
    @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
    public List<Image> images = new ArrayList<Image>();

}
