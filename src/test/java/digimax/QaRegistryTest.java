package digimax;

import digimax.services.AppModule;
import digimax.services.QaModule;
import org.apache.tapestry5.hibernate.HibernateCoreModule;
import org.apache.tapestry5.hibernate.HibernateModule;
import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.TapestryModule;
import org.apache.tapestry5.test.TapestryTestCase;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.util.ArrayList;

public class QaRegistryTest {
//public class QaRegistryTest extends TapestryTestCase {

    protected final static Logger logger = LoggerFactory.getLogger(QaRegistryTest.class);
    protected Registry registry;



    @BeforeClass
    public void oneTimeSetUp() {
        registry = new RegistryBuilder().add(TapestryModule.class,
                HibernateCoreModule.class,
                HibernateModule.class,
                AppModule.class,
                QaModule.class).build();
        registry.performRegistryStartup();
        Session session = registry.getService(Session.class);
        session.flush();

        logger.info("QaRegistryTest@BeforeMethod - setUp");
    }

    @AfterClass
    public void shutdown() {
        registry.shutdown();
    }


}
