package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.BookLineItem;
import digimax.entities.item.Cart;
import digimax.entities.item.LineItem;
import digimax.entities.people.Author;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:19 PM
 */
public class BookCart {

    @Property
    private LineItem lineItem;

    public Image getHorizontalImage() {
        Book book = (Book) lineItem.item;
        List<Image> images = book.images;
        Image image = images.get(2);
        return image;
    }

    public Book getBook() {
        return (Book) lineItem.item;
    }

    public List<LineItem> getItems() {
        return cart.items;
    }
    @Inject
    private Logger logger;

    private boolean cartExists;
    @SessionState//(create=false)
    private Cart cart;

    public boolean isExists() {
        return cartExists && cart.items.size()>0;
    }

    public String getAuthorName() {
        return ((Author)getBook().authors.toArray()[0]).getFullName();
    }

    private void onActionFromClearCart() {
        logger.debug("Start Diagnostics");
        cart.items.clear();
        logger.debug("End Diagnostics");
    }


}

