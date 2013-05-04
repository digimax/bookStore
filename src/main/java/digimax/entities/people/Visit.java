package digimax.entities.people;

import org.apache.tapestry5.annotations.Property;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 5/3/13
 * Time: 10:25 AM
 */
public class Visit {

    @Property
    public Person user;

    public boolean isValid() {
        return user!=null?true : false;
    }

}
