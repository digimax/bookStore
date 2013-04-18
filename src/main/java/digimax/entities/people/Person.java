package digimax.entities.people;

import digimax.entities.geo.Address;
import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 1:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Person extends DomainObject {

    @Property
    @OneToOne(cascade = CascadeType.ALL)
    public IdentityMeta identityMeta = new IdentityMeta();

    @Property
    @Validate("required")
    public String firstName;

    @Property
    @Validate("required")
    public String lastName;

    @Property
    @Validate("required, regex")
    public String userName;


    @Property
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    public Address address;

    public String getFullName() {
        if (firstName!=null)
            return String.format("%s %s", firstName, lastName);
        return lastName;
    }


}
