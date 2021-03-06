package digimax.pages;

import digimax.entities.geo.Address;
import digimax.entities.people.Customer;
import digimax.services.domain.PersonService;
import digimax.services.domain.PersonServiceImpl;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.PersistenceConstants;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/26/13
 * Time: 3:45 PM
 */
public class NewUser {

    @Inject
    Logger logger;

    @Inject
    private Messages messages;

    @Inject
    private AlertManager alertManager;

    @Inject
    PersonService personService;

    @InjectComponent
    Form form;

    @Property
    @Persist(PersistenceConstants.SESSION)
    private Customer customer;

    @Property
    private String verifyPassword;

    @BeginRender
    void beginRender(MarkupWriter writer) {
        logger.debug("Start Diagnostics");
        if (customer==null) {
            customer = new Customer();
            customer.address = new Address();
        }
        logger.debug("End Diagnostics");
    }

    public Customer getWrappedCustomer() {
        logger.debug("Start Diagnostics");
        Customer wrappedCustomer = customer;
        return wrappedCustomer;
    }

    private Object onSelectedFromJoinUp() {
        logger.debug("Start Diagnostics");
        if (form.getHasErrors()) {
            return this;
        }
        if (!personService.isUserNameUnique(customer.userName)) {
            alertManager.alert(Duration.SINGLE, Severity.WARN, messages.format("existing-customer", customer.userName));
            return Login.class;
        }
        if (!verifyPassword.equals(customer.identityMeta.password)) {
            alertManager.alert(Duration.SINGLE, Severity.WARN, messages.format("bad-matching-password"));
            return this;
        }
        return Index.class;
    }

}
