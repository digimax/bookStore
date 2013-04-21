package digimax.services.domain;

import digimax.entities.people.Author;
import digimax.entities.people.Customer;
import digimax.entities.people.Employee;
import digimax.entities.people.Person;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import java.util.List;

public interface PersonService {

    Person testInstance();

    @CommitAfter
    void save(Person person);

    @CommitAfter
    void delete(Person person);

    @Log
    Person login(String userName, String password);

    boolean isUserNameUnique(String userName);

    boolean isPassword(Person person, String password);

    List<Author> findAuthors(String name);

    Author findAuthor(String lastName, String firstName);
    @CommitAfter
    Author newAuthor(String userName, String password, String firstName, String lastName);

    Customer findCustomer(String userName, String password);
    @CommitAfter
    Customer newCustomer(String userName, String password, String firstName, String lastName);

    Employee findEmployee(String userName, String password);
    @CommitAfter
    Employee newEmployee(String userName, String password, String firstName, String lastName);

    List<Person> persons();

    @CommitAfter
    Author findOrCreateAuthor(String firstAuthorLastName, String firstAuthorFirstName);
}
