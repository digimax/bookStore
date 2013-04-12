package digimax.services.domain;

import digimax.entities.store.Store;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;

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
}
