package digimax.services.domain;

import digimax.entities.people.Person;
import digimax.entities.store.Store;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 5:04 PM
 * To change this template use File | Settings | File Templates.
 */
public interface StoreService {

    @CommitAfter
    void save(Store store);

    @CommitAfter
    void delete(Store store);
}
