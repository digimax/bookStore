package digimax.entities.geo;

import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:14 PM
 * To change this template use File | Settings | File Templates.
 */
@javax.persistence.Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class Location extends DomainObject {

    @Property
    @Validate("required")
    public String name;


    public int latitude;
    public int longitude;


}
