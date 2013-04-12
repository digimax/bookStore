package digimax.pages;

import digimax.entities.people.Employee;
import digimax.entities.people.Person;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.annotations.PageLoaded;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import java.util.List;


public class Contact
{
    @Inject
    private Logger logger;

    @Inject
    PersonService personService;

    @Property
    private Person person;

    void onActivate() {
        personService.save(getEmployee());
        logger.info("Changes saved successfully");
    }

    public Employee getEmployee() {
        return (Employee) personService.testInstance();
    }

    public List<Person> getPersons() {
        return personService.persons();
    }


   public String[] getTesters() {
       return new String[] {"one","two","three","four"};
   }

   @Property
   String tested;

}
