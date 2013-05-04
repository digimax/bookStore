package digimax.pages;

import digimax.entities.library.Library;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/16/13
 * Time: 8:23 PM
 */
public class Help {

    @Inject
    Session session;

    public Library getLibrary() {
        return (Library) session.createCriteria(Library.class).uniqueResult();
    }
}
