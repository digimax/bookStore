package digimax;

import digimax.entities.library.Library;
import digimax.services.app.BootupService;
import org.hibernate.Session;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
public class BootupServiceTest extends QaRegistryTest {

    @BeforeMethod
    public void bootupLibrary() {
        BootupService bootupService = registry.getService(BootupService.class);
        bootupService.bootupLibrary();
    }

    @Test
    public void testBootupLibrary() {

        Session session = registry.getService(Session.class);
        Library populatedLibrary = (Library) session.createCriteria(Library.class).uniqueResult();

        Assert.assertNotNull(populatedLibrary);
        Assert.assertNotNull(populatedLibrary.id);
    }
}
