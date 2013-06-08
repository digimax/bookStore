package digimax.components.domain;

import digimax.entities.item.Book;
import digimax.entities.item.Cart;
import digimax.entities.item.Item;
import digimax.entities.item.LineItem;
import digimax.pages.BookCart;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 6/8/13
 * Time: 11:37 AM
 */
public class BookcartTool {

    @Inject
    Logger logger;

    @InjectComponent
    private Zone refreshZone;


    private boolean cartExists;
    @SessionState
    private Cart cart;

    @Property
    @Parameter(required = true)
    private Book book;


    public BookcartTool() {
        super();
    }

    public boolean isInBookcart() {
        List<LineItem> lineItems = cart.items;
        for (LineItem lineItem : lineItems) {
            if (book.equals(lineItem.item)) {
                return true;
            }
        }
        return false;
    }

    private Object onActionFromAddItemLink() {
        logger.debug("Start Diagnostics");
        LineItem lineItem = new LineItem();
        lineItem.item = book;
        lineItem.quantity = 1;
        cart.items.add(lineItem);

        logger.debug("End Diagnostics");
        return BookCart.class;
    }

    private Object onActionFromRemoveItemLink() {
        List<LineItem> lineItems = cart.items;
        for (LineItem lineItem : lineItems) {
            if (book.equals(lineItem.item)) {
                lineItems.remove(lineItem);
                break;
            }
        }
        return refreshZone.getBody();
    }

    public int getItemCount() {
        return cart.items.size();
    }

}
