package digimax.pages;

import digimax.entities.item.Cart;
import digimax.entities.people.Customer;
import digimax.entities.people.Person;
import digimax.entities.people.Visit;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.Messages;
import org.slf4j.Logger;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/16/13
 * Time: 9:19 PM
 */
public class Login {

    public static final String LOGIN_COOKIE_NAME = "digimax.ncapsuld.Login.userName";

    @Inject
    private Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private AlertManager alertManager;

    @Inject
    private PersonService personService;

    @InjectComponent
    private Form form;

    private boolean visitExists;
    @SessionState
    private Visit visit;

    @Property
    boolean rememberMe;

    @Property
    private String password;

    @Property
    private String userName;

    private Object onSelectedFromLogin() {
        logger.debug("Start Diagnostics");
        if (form.getHasErrors()) {
            return this;
        }

        //check to see if username exisits
        Person user = personService.login(userName, password);
        if (user==null) {
            form.recordError(messages.get("invalid-login"));
            return this;
        }

        //Valid login
        visit.user = user;
        alertManager.alert(Duration.SINGLE, Severity.SUCCESS, messages.format("welcome", user.getFullName()));

        logger.debug("End Diagnostics");
        return Index.class;
    }
}
