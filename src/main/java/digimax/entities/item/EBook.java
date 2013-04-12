package digimax.entities.item;

import org.apache.tapestry5.annotations.Property;
import javax.persistence.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:27 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class EBook extends Book {

    @Property
    public String subSubTitle;


    public EBook() {
        super(); //TODO: why is this being instantiated once by tapestry?
                 //TODO: why critera for Book included EBook columns?
    }

}
