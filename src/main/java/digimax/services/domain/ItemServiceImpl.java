package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.item.Item;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

public class ItemServiceImpl implements ItemService {

    @Inject
    private Session session;

    public Item testInstance() {
        Item testItem = new Book();
        testItem.price = 15.77f;
        return testItem;
    }

    public void save(Item item) {
        session.save(item);
    }

    public void delete(Item item) {
        session.delete(item);
    }
}
