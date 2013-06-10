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
import org.hibernate.criterion.Junction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import java.util.List;

@SuppressWarnings("unchecked")
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

    public boolean isUserNameUnique(String userName) {
        List<Person> users = session.createCriteria(Person.class).add(Restrictions.eq("userName", userName)).list();
        return (users!=null && users.size()==1)? false : true;
    }

    public boolean isPassword(Person person, String password) {
        return person.identityMeta.isCorrectPassword(password);
    }

    public List<Author> findAuthors(String name) {
        if (name==null || name.length()==0) {
            return null;
        }
        List<Author> authors = null;
        //parse out the first and last name
        int spaceTokenIndex = name.indexOf(' ');
        if (spaceTokenIndex>0) {
            //Search by firstName and lastName
            String firstName = name.substring(0, spaceTokenIndex);
            String lastName = name.substring(spaceTokenIndex+1, name.length());
            Criterion condition =
                    Restrictions.conjunction().add(Restrictions.ilike("lastName", "%"+lastName+"%"))
                            .add(Restrictions.ilike("firstName", "%"+firstName+"%"));
            authors =
                    session.createCriteria(Author.class).add(condition).list();

        } else {
            //Search by lastName only
            authors =
                    session.createCriteria(Author.class).add(Restrictions.ilike("lastName", "%"+name+"%")).list();
        }
        return authors;
    }

    public Author findAuthor(String lastName, String firstName) {
        Junction junction = Restrictions.conjunction().add(Restrictions.eq("lastName", lastName));
        if (firstName!=null && firstName.trim().length()>0) {
            junction.add(Restrictions.eq("firstName", firstName));
        } else {
            junction.add(Restrictions.or(Restrictions.and(Restrictions.isNotNull("firstName"),
                Restrictions.eq("firstName", firstName)), Restrictions.isNull("firstName")));
        }
        Author author  = (Author) session.createCriteria(Author.class).add(junction).uniqueResult();
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
        author.identityMeta.setNewPassword(password);
        save(author);
        return author;
    }

    public Author findOrCreateAuthor(String authorLastName, String authorFirstName) {
        Author author  = findAuthor(authorLastName, authorFirstName);
        if (author==null) {
            author = newAuthor(null, null, authorLastName, authorFirstName);
        }
        return author;

    }


    public Customer findCustomer(String userName, String password) {
        Customer customer =
                (Customer) session.createCriteria(Customer.class).add(Restrictions.eq("userName", userName)).uniqueResult();
        return (customer!=null && isPassword(customer, password))? customer:null;
    }

    public Customer newCustomer(String userName, String password, String firstName, String lastName) {
        //check this user doesn't already exist
        if (!isUserNameUnique(userName))
            return null;

        Customer customer = new Customer();
        customer.userName = userName;
        customer.firstName = firstName;
        customer.lastName = lastName;
        customer.identityMeta.setNewPassword(password);
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
        employee.identityMeta.setNewPassword(password);
        save(employee);
        return employee;
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
