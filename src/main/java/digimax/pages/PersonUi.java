package digimax.pages;

import digimax.entities.people.Person;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 7:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonUi {

    @PageActivationContext
    @Property
    private Person person;
}
