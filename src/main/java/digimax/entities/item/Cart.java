package digimax.entities.item;

import org.apache.tapestry5.annotations.Property;

import java.util.ArrayList;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 5/1/13
 * Time: 3:13 PM
 */
public class Cart {

    @Property
    public List<LineItem> items = new ArrayList<LineItem>();
}
