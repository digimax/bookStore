package digimax.entities.people;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 5/3/13
 * Time: 10:25 AM
 */
public class Visit {

    @Inject
    private Logger logger;


    @Property
    public Person user;

    public boolean isValid() {
        return user!=null;
    }

    public Visit() {
        super();
    }

}