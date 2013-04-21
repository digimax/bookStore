package digimax.pages;

import digimax.entities.people.Author;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.Validate;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.sql.Insert;
import org.slf4j.Logger;

import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:17 PM
 */
public class Search {

    @Inject
    Logger logger;

    @Inject
    private AlertManager manager;

    @Inject
    private Messages messages;

    @Inject
    PersonService personService;

    @InjectComponent
    private Form byAuthorForm;

    @InjectComponent
    private Form byTitleForm;

    @Property
//    @Validate("required")
    private String author;

    @Property
//    @Validate("required")
    private String title;

    void onSelectedFromSearchByAuthor() {
        logger.debug("Start Diagnostics");
        if (byAuthorForm.getHasErrors()) {
            return;
        }
        List<Author> authors = personService.findAuthors(author);
        if (authors==null || authors.size()==0) {
            manager.alert(Duration.SINGLE, Severity.WARN, messages.format("noAuthorsFound", author));
        }
        logger.debug("End Diagnostics");
    }

    void onSelectedFromSearchByTitle() {
        logger.debug("Start Diagnostics");
        if (byTitleForm.getHasErrors()) {
            return;
        }
        logger.debug("End Diagnostics");
    }

}
