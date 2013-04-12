package digimax.entities.item;

import javax.persistence.Entity;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 2:04 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class BookLineItem extends LineItem {
    public Book book() {
        return (Book) item;
    };
}
