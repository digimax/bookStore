package digimax;

import digimax.entities.geo.Address;
import digimax.entities.item.Book;
import digimax.entities.people.Author;
import digimax.entities.people.Customer;
import digimax.entities.people.Employee;
import digimax.entities.people.Person;
import digimax.services.AppModule;
import digimax.services.QaModule;
import digimax.services.domain.BookService;
import digimax.services.domain.PersonService;
import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.hibernate.HibernateModule;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.services.TapestryModule;
import org.apache.tapestry5.test.TapestryTestCase;
import org.hibernate.CacheMode;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.testng.Assert;
import org.testng.annotations.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SuppressWarnings("unchecked")
public class PersonServiceTest extends QaRegistryTest {

    private Collection collection;

    @BeforeClass
    public void oneTimeSetUp() {
        // one-time initialization code
        super.oneTimeSetUp();
        logger.info("@BeforeClass - oneTimeSetUp");
    }

    @AfterClass
    public void oneTimeTearDown() {
        // one-time cleanup code
        logger.info("@AfterClass - oneTimeTearDown");
    }

    @BeforeMethod
    public void setUp() {
        collection = new ArrayList();
        logger.info("@BeforeMethod - setUp");
    }

    @AfterMethod
    public void tearDown() {
        collection.clear();
//        Session session = registry.getService(Session.class);
//        session.clear();
//        session.flush();
            // cleanup all persisted People
            Session session = registry.getService(Session.class);

        BookService bookService = registry.getService(BookService.class);

        List<Book> persistedBookList = session.createCriteria(Book.class).list();

        for (Book book: persistedBookList) {
            bookService.delete(book);
        }
        //refetch
        persistedBookList = session.createCriteria(Book.class).list();
        Assert.assertEquals(persistedBookList.size(),0);
        logger.info("@AfterMethod - tearDown");        
        
            PersonService personService = registry.getService(PersonService.class);

            List<Person> persistedPersonList = session.createCriteria(Person.class).list();

            for (Person person: persistedPersonList) {
                personService.delete(person);
            }
            //refetch
            persistedPersonList = session.createCriteria(Person.class).list();
            Assert.assertEquals(persistedPersonList.size(),0);
            logger.info("@AfterMethod - tearDown");
    }

    @Test
    public void testEmptyCollection() {
        Assert.assertEquals(collection.isEmpty(),true);
        logger.info("@Test - testEmptyCollection");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testOneItemCollection() {
        collection.add("itemA");
        Assert.assertEquals(collection.size(), 1);
        logger.info("@Test - testOneItemCollection");
    }

    @Test
    public void testPersonTestInstance() {
        PersonService personService = registry.getService(PersonService.class);
        Person person = personService.testInstance();
        Assert.assertEquals(person.firstName, "Jon");
        Assert.assertEquals(person.lastName, "Williams");
    }

    @Test
    public void testSave() {
        Session session = registry.getService(Session.class);

        PersonService personService = registry.getService(PersonService.class);
        Person person = personService.testInstance();
        Address personAddress = person.address;
        personAddress.street1 = "124 Huckelbury Lane";
        Assert.assertTrue(person.id == null);
        personService.save(person);
        Assert.assertFalse(person.id == null);
        Long personId = person.id;
        Assert.assertEquals(session.getCacheMode(), CacheMode.NORMAL);
        session.evict(person);

        Person persistedPerson = (Person) session.createCriteria(Person.class).add(Restrictions.eq("id", personId)).uniqueResult();
        Assert.assertEquals(person, persistedPerson);
        Assert.assertEquals(persistedPerson.id, personId);
        Assert.assertEquals(persistedPerson.firstName, "Jon");
        Assert.assertEquals(persistedPerson.lastName, "Williams");
        Assert.assertEquals(persistedPerson.address.id, personAddress.id);
        Assert.assertEquals(persistedPerson.address, personAddress);
        Assert.assertEquals(personAddress.street1, person.address.street1);
        logger.debug("personAddress.id::"+personAddress.id);
        logger.debug("personAddress.street1::"+personAddress.street1);
    }

    @Test
    public void testDelete() {
        Session session = registry.getService(Session.class);

        PersonService personService = registry.getService(PersonService.class);
        Person person = personService.testInstance();
        Address personAddress = person.address;
        personAddress.street1 = "124 Huckelbury Lane";
        Assert.assertTrue(person.id == null);
        personService.save(person);
        Assert.assertFalse(person.id == null);
        Long personId = person.id;
        Assert.assertEquals(session.getCacheMode(), CacheMode.NORMAL);
        session.evict(person);

        Person persistedPerson = (Person) session.createCriteria(Person.class).add(Restrictions.eq("id", personId)).uniqueResult();
        Assert.assertEquals(person, persistedPerson);
        Assert.assertEquals(personId, persistedPerson.id);
        personService.delete(persistedPerson);
        List<Person> people = session.createCriteria(Person.class).list();
        Assert.assertEquals(people.size(), 0);

    }


    @Test
    public void testUserNameUnique() {
        Session session = registry.getService(Session.class);

        PersonService personService = registry.getService(PersonService.class);

        List<Person> emptyPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(emptyPersonList.size(),0);

        Person employee1 = new Employee();
        employee1.firstName="Dave";
        employee1.lastName="Stewart";
        employee1.userName="FooBar1";

        personService.save(employee1);

        List<Person> populatedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(populatedPersonList.size(),1);

        Person customer1 = new Customer();
        customer1.firstName="Annie";
        customer1.firstName="Lennox";
        customer1.userName="Bones1";

        personService.save(customer1);

        populatedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(populatedPersonList.size(),2);

        //try with known userName
        Assert.assertFalse(personService.isUserNameUnique("Bones1"));

        //try with unknown userName
        Assert.assertTrue(personService.isUserNameUnique("Bambata1"));
    }

    @Test
    public void testFindAuthor() {

        Session session = registry.getService(Session.class);
        BookService bookService = registry.getService(BookService.class);
        PersonService personService = registry.getService(PersonService.class);

        List<Person> emptyPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(emptyPersonList.size(),0);
        Book book = new Book();
        book.title = "All About Toast";
        book.subTitle = "Extra Honey Chapters";


        Author author1 = new Author();
        author1.firstName="Martha";
        author1.lastName="Stewart";
        author1.userName="martha@martha.com";
        author1.identityMeta.setPassword("zzz");

        book.authors.add(author1);
        bookService.save(book);

//        personService.save(author1);
        assertNotNull(author1.identityMeta);

        session.evict(author1);
        List<Person> populatedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(populatedPersonList.size(),1);

        Person author2 = new Author();
        author2.firstName="Jimmy";
        author2.lastName="Pastrami";
        author2.userName="jim@pastrami.com";
        author2.identityMeta.setPassword("zzz");

        personService.save(author2);
        assertNotNull(author2.identityMeta);

        session.evict(author2);
        populatedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(populatedPersonList.size(),2);

        //try existing author
        Author persistedAuthor = personService.findAuthor("Stewart", "Martha");
        Assert.assertNotNull(persistedAuthor);

        //try non existing author
        persistedAuthor = personService.findAuthor("Pumpkinhead", "Peter");
        Assert.assertNull(persistedAuthor);


    }
    
    @Test
    public void testFindEmployee() {
        Session session = registry.getService(Session.class);

        PersonService personService = registry.getService(PersonService.class);

        List<Person> emptyPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(emptyPersonList.size(),0);

        Person employee1 = new Employee();
        employee1.firstName="Dave";
        employee1.lastName="Stewart";
        employee1.userName="FooBar1";
        employee1.identityMeta.setPassword("xxx");

        personService.save(employee1);
        assertNotNull(employee1.identityMeta);


        List<Person> populatedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(populatedPersonList.size(),1);

        Person customer1 = new Customer();
        customer1.firstName="Annie";
        customer1.firstName="Lennox";
        customer1.userName="Bones1";
        customer1.identityMeta.setPassword("xxx");

        personService.save(customer1);
        assertNotNull(customer1.identityMeta);

        populatedPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(populatedPersonList.size(),2);

        session.evict(employee1);
        session.evict(customer1);

        Employee  employee =  personService.findEmployee("Oscar1","xxx");
        assertNull(employee);

        Customer customer = personService.findEmployee("FooBar1","xxx");
        assertNotNull(customer);

    }

    @Test
    public void testNewEmployee() {
        Session session = registry.getService(Session.class);

        PersonService personService = registry.getService(PersonService.class);

        List<Person> emptyPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(emptyPersonList.size(),0);

        Employee persistedEmployee = personService.newEmployee("Yoda1","xxx", "Benji", "Johanssen");
        Assert.assertNotNull(persistedEmployee);
        Assert.assertNotNull(persistedEmployee.id);

        Assert.assertEquals(persistedEmployee.userName, "Yoda1");
        Assert.assertEquals(persistedEmployee.firstName , "Benji");
        Assert.assertEquals(persistedEmployee.lastName , "Johanssen");

        List<Person> personList = session.createCriteria(Person.class).list();
        Assert.assertEquals(personList.size(),1);

        Person employee = personService.findEmployee("Yoda1", "xxx");
        //test username collisions
        Employee newEmployee = personService.newEmployee(employee.userName,"yyy", "chester", "tester");
        //null indicates that their was a name collision
        assertNull(newEmployee);
    }

    @Test
    public void testFindOrCreateAuthor() {
        Session session = registry.getService(Session.class);

//        BookService bookService = registry.getService(BookService.class);
        PersonService personService = registry.getService(PersonService.class);

        Author author1 = personService.newAuthor(null, null, "Drew", "Nancy");
        Author author2 = personService.newAuthor(null, null, "Tyson", "Mike");
        Author author3 = personService.newAuthor(null, null, "Clark", "Petula");

        //Test find known author
        Author persistedAuthor = personService.findAuthor("Drew", "Nancy");
        Assert.assertNotNull(persistedAuthor);
        Assert.assertEquals(persistedAuthor, author1);
        Assert.assertEquals(persistedAuthor.id, author1.id);

        //test find unknown author
        persistedAuthor = personService.findAuthor("Mouse", "Malcolm");
        Assert.assertNull(persistedAuthor);

        List<Author> persistedAuthors = session.createCriteria(Author.class).list();
        Assert.assertEquals(persistedAuthors.size(), 3);

        session.evict(author1);
        session.evict(author2);
        session.evict(author3);
        session.evict(persistedAuthor);

        //test findCreate known author
        persistedAuthor = personService.findOrCreateAuthor("Tyson", "Mike");
        Assert.assertEquals(persistedAuthor, author2);
        Assert.assertEquals(persistedAuthor.id, author2.id);

        //test findCreate unknown author
        persistedAuthors = session.createCriteria(Author.class).list();
        Assert.assertEquals(persistedAuthors.size(), 3);
        persistedAuthor = personService.findOrCreateAuthor("Wholesome","Hans");
        //ensure new book was created properly
        Assert.assertNotNull(persistedAuthor);
        Assert.assertNotNull(persistedAuthor.id);

        persistedAuthors = session.createCriteria(Author.class).list();
        Assert.assertEquals(persistedAuthors.size(), 4);
        Assert.assertTrue(persistedAuthors.contains(persistedAuthor));
    }

    @Test
    public void testLogin() {
        Session session = registry.getService(Session.class);

        PersonService personService = registry.getService(PersonService.class);

        List<Person> emptyPersonList = session.createCriteria(Person.class).list();
        Assert.assertEquals(emptyPersonList.size(),0);

        Employee persistedEmployee = personService.newEmployee("Yoda1","xxx", "Benji", "Johanssen");
        Assert.assertNotNull(persistedEmployee);
        Assert.assertNotNull(persistedEmployee.id);

        Assert.assertEquals(persistedEmployee.userName, "Yoda1");
        Assert.assertEquals(persistedEmployee.firstName , "Benji");
        Assert.assertEquals(persistedEmployee.lastName , "Johanssen");

        List<Person> personList = session.createCriteria(Person.class).list();
        Assert.assertEquals(personList.size(),1);

        Person employee = personService.findEmployee("Yoda1", "xxx");

        Person authenticatedUser = personService.login("Yoda1","xxx");

        Assert.assertNotNull(employee);
        Assert.assertNotNull(authenticatedUser);
        Assert.assertEquals(authenticatedUser, employee);

        //try invalid login
        Person invalidUser = personService.login("Whatutalkingbout","");
        Assert.assertNull(invalidUser);

    }




}
