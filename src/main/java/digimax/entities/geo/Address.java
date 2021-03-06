package digimax.entities.geo;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Address extends Location {

    @Property
    @Validate("required")
    public String street1;

    @Property
    public String street2;

    @Property
    @Validate("required")
    public String city;

    @Property
    @Validate("required")
    public String state;

    @Property
    @Validate("required,regexp")
    public String zip;
}
