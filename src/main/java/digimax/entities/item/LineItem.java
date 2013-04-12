package digimax.entities.item;

import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class LineItem extends DomainObject {

    @Property
    @Validate("required")
    public int quantity;

    @Property
    @Validate("required")
    public String description;

    @Property
    @ManyToOne
    public Item item;
}
