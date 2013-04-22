package digimax.pages;

import digimax.entities.people.Author;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.hibernate.sql.Insert;
import org.slf4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:17 PM
 */
public class Search {

    @Inject
    private Logger logger;

    @Inject
    private AlertManager manager;

    @Inject
    private Messages messages;

    @Inject
    private Request request;

    @Inject
    private AjaxResponseRenderer ajaxResponseRenderer;

    @Inject
    private ComponentResources componentResources;

    @Inject
    PersonService personService;

    @InjectComponent
    private Form byAuthorForm;

    @InjectComponent
    private Zone byAuthorFormZone;

    @InjectComponent
    private Form byTitleForm;

    @InjectComponent
    private Zone authorsZone;

    @Property
//    @Validate("required")
    private String authorName;

    @Property
//    @Validate("required")
    private String title;

    @Property
    private Author author;

    @Property
    @Persist(PersistenceConstants.SESSION)
    private List<Author> authors;

    void onSelectedFromSearchByAuthor() {
        componentResources.discardPersistentFieldChanges();
        authors = null;
        logger.debug("Start Diagnostics");
        if (byAuthorForm.getHasErrors()) {
            return;
        }
        authors = personService.findAuthors(authorName);
        if (authors==null || authors.size()==0) {
            manager.alert(Duration.SINGLE, Severity.WARN, messages.format("noAuthorsFound", authorName));
            return;
        }
        if (request.isXHR()) {
            logger.debug("byAuthorFormZone :: {}", byAuthorFormZone);
            logger.debug("authorsZone :: {}", authorsZone);
            ajaxResponseRenderer.addRender(byAuthorFormZone).addRender(authorsZone);
        }
        logger.debug("End Diagnostics");
    }

    void onSelectedFromSearchByTitle() {
        componentResources.discardPersistentFieldChanges();
        logger.debug("Start Diagnostics");
        if (byTitleForm.getHasErrors()) {
            return;
        }
        logger.debug("End Diagnostics");
    }

    public boolean getHasAuthors() {
        return (authors!=null && authors.size()>0);
    }

}
