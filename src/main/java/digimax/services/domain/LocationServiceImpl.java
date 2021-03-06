package digimax.services.domain;

import digimax.entities.geo.Address;
import digimax.entities.geo.Location;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.store.Store;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class LocationServiceImpl implements LocationService {
    @Inject
    private Logger logger;

    @Inject
    private Session session;

    public Location testInstance() {
        Address address = new Address();
        address.street1 = "1700 Grant St.";
        address.street2 = "Suite B.";
        address.city = "Berkeley";
        address.state = "CA";
        address.zip = "94700";

        address.latitude = 333;
        address.longitude = 333;
        return address;
    }

    public void save(Location location) {
        session.save(location);
    }

    public void delete(Location location) {
        session.delete(location);
    }

    @Override
    public Location findOrCreateLibraryLocation(Library library, String locationName) {
        for (Location location : library.shelves) {
            if (location.name.equals(locationName)) {
                return location;
            }
        }
        Shelf newLocation = new Shelf(locationName);
        library.shelves.add(newLocation);
        return newLocation;
    }

    @Override
    public Location findOrCreateStoreLocation(Store store, String locationName) {
        for (Location location : store.shelves) {
            if (location.name.equals(locationName)) {
                return location;
            }
        }
        Shelf newLocation = new Shelf(locationName);
        store.shelves.add(newLocation);
        return newLocation;
    }
}
