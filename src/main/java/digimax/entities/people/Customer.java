package digimax.entities.people;

import digimax.entities.geo.Address;
import org.apache.tapestry5.annotations.Property;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Customer extends Person {

    @Property
    @OneToOne
    public Address shippingAddress;

}
