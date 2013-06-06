package digimax.services.domain;

import digimax.entities.geo.Location;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.people.Person;
import digimax.entities.store.Store;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:53 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LocationService {

    Location testInstance();

    @CommitAfter
    void save(Location location);

    @CommitAfter
    void delete(Location location);

    @CommitAfter
    Location findOrCreateLibraryLocation(Library library, String locationName);

    @CommitAfter
    Location findOrCreateStoreLocation(Store store, String locationName);

}
