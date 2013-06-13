package digimax.entities.item;

import org.apache.tapestry5.annotations.Property;
import sun.misc.Regexp;

import java.text.NumberFormat;
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

    public Float getTotal() {
        Float total = 0f;
        for (LineItem lineItem: items) {
            total += lineItem.quantity*lineItem.item.getCalculatedPrice();
        }
        return total;
    }

    public String getFormattedTotal() {
        return NumberFormat.getCurrencyInstance().format(getTotal());
    }
}
