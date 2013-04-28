package digimax.pages;

import digimax.entities.people.Customer;
import digimax.entities.people.Person;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.alerts.AlertManager;
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

    @Property
    boolean rememberMe;

    @Property
    private String password;

    @Property
    private String userName;

    private void onSelectedFromLogin() {
        logger.debug("Start Diagnostics");
        if (form.getHasErrors()) {
            return;
        }

        //check to see if username exisits
        Customer customer = personService.findCustomer(userName, password);
        if (customer==null) {
            form.recordError(messages.get("invalid-login"));
            return;
        }
        logger.debug("End Diagnostics");
    }
}
