package digimax.entities.invoice;

import digimax.entities.item.LineItem;
import org.apache.tapestry5.annotations.Property;
import javax.persistence.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 2:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class InvoiceLineItem extends LineItem {

    public Invoice invoice() {
        return (Invoice) item;
    }

}
