package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.item.Item;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.store.Store;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoreServiceImpl implements StoreService {

    @Inject
    private Logger logger;

    @Inject
    private Session session;


    public void save(Store store) {
        session.save(store);
    }

    public void delete(Store store) {
        session.delete(store);
    }

    @Override
    public Set<Item> ripLibrary(Store store, Library library) {
        Set<Item> items = new HashSet<Item>();
        for (Shelf shelf: library.shelves) {
            Shelf newShelf = new Shelf();
            newShelf.name = String.format("store %s", shelf.name);
            newShelf.books = shelf.books;
            store.shelves.add(newShelf);
            for (Book book: newShelf.books) {
                items.add(book);
            }
        }
        return items;
    }

    @Override
    public Shelf findShelf(Store store, String locationName) {
        Shelf foundShelf = null;
        for (Shelf shelf: store.shelves) {
            if (shelf.name == locationName) {
                foundShelf = shelf;
                break;
            }
        }
        return foundShelf;
    }
}
