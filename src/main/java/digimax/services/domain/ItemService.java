package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.item.Item;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

public interface ItemService {
    Item testInstance();

    @CommitAfter
    void save(Item item);

    @CommitAfter
    void delete(Item item);
}
