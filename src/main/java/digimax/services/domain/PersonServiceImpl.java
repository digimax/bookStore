package digimax.services.domain;

import digimax.entities.geo.Address;
import digimax.entities.item.Book;
import digimax.entities.people.Author;
import digimax.entities.people.Customer;
import digimax.entities.people.Employee;
import digimax.entities.people.Person;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 3:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class PersonServiceImpl implements PersonService {

    @Inject
    private Logger logger;

    @Inject
    private Session session;

    @Inject
    private LocationService locationService;

    public Person testInstance() {
        Person employee = new Employee();
        employee.firstName = "Jon";
        employee.lastName = "Williams";
        employee.userName = "Bounce1";

        Address address = (Address) locationService.testInstance();
        employee.address = address;
        return employee;
    }

    //Two Phase login
    public Person login(String userName, String password) {
        Person person = (Person)
                session.createCriteria(Person.class).add(Restrictions.eq("userName", userName)).uniqueResult();
        if (person!=null && person.identityMeta.isCorrectPassword(password))
                return person;
        return null;
    }

    @SuppressWarnings("unchecked")
    public boolean isUserNameUnique(String userName) {
        List<Person> users = session.createCriteria(Person.class).add(Restrictions.eq("userName", userName)).list();
        return (users!=null && users.size()==1)? false : true;
    }

    public boolean isPassword(Person person, String password) {
        return person.identityMeta.isCorrectPassword(password);
    }

    public Author findAuthor(String lastName, String firstName) {
        Criterion condition =
                Restrictions.conjunction().add(Restrictions.eq("lastName", lastName))
                        .add(Restrictions.eq("firstName", firstName));
        Author author =
                (Author) session.createCriteria(Author.class).add(condition).uniqueResult();
        return author;
    }

    public Author newAuthor(String userName, String password, String lastName, String firstName) {
        //check this user doesn't already exist
        if (!isUserNameUnique(userName))
            return null;

        Author author = new Author();
        author.userName = userName;
        author.firstName = firstName;
        author.lastName = lastName;
        author.identityMeta.setPassword(password);
        save(author);
        return author;
    }

    public Author findOrCreateAuthor(String firstAuthorLastName, String firstAuthorFirstName) {
        Author author  = findAuthor(firstAuthorLastName, firstAuthorFirstName);
        if (author==null) {
            author = newAuthor(null, null, firstAuthorLastName, firstAuthorFirstName);
        }
        return author;

    }


    public Customer findCustomer(String userName, String password) {
        Customer customer =
                (Customer) session.createCriteria(Customer.class).add(Restrictions.eq("userName", userName)).uniqueResult();
        return isPassword(customer, password)?customer:null;
    }

    public Customer newCustomer(String userName, String password, String firstName, String lastName) {
        //check this user doesn't already exist
        if (!isUserNameUnique(userName))
            return null;

        Customer customer = new Customer();
        customer.userName = userName;
        customer.firstName = firstName;
        customer.lastName = lastName;
        customer.identityMeta.setPassword(password);
        save(customer);
        return customer;
    }

    public Employee findEmployee(String userName, String password) {
        Employee employee =
                (Employee) session.createCriteria(Employee.class).add(Restrictions.eq("userName", userName)).uniqueResult();
        if (employee!=null && employee.identityMeta.isCorrectPassword(password))
            return employee;
        return null;
    }

    public Employee newEmployee(String userName, String password, String firstName, String lastName) {
        //check this user doesn't already exist
        if (!isUserNameUnique(userName))
            return null;

        Employee employee = new Employee();
        employee.userName = userName;
        employee.firstName = firstName;
        employee.lastName = lastName;
        logger.debug("employee.identityMeta::"+employee.identityMeta);
        employee.identityMeta.setPassword(password);
        save(employee);
        return employee;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @SuppressWarnings("unchecked")
    public List<Person> persons() {
        return session.createCriteria(Person.class).list();
    }

//    public void save(Person person) {
//        try {
//            session.beginTransaction();
//            session.save(person);
//            session.getTransaction().commit();
//        } catch (RuntimeException e) {
//            session.getTransaction().rollback();
//        }
//    }
//

    public void save(Person person) {
        session.save(person);
    }

    public void delete(Person person) {
        session.delete(person);
    }

}
