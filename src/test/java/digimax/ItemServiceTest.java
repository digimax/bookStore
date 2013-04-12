package digimax;

import digimax.entities.item.Item;
import digimax.services.domain.ItemService;
import org.testng.Assert;
import org.testng.annotations.Test;

@SuppressWarnings("unchecked")
public class ItemServiceTest extends QaRegistryTest {

    @Test
    public void testInstance() {
        ItemService itemService = registry.getService(ItemService.class);
        Item testItem = itemService.testInstance();
        Assert.assertNotNull(testItem);
    }


}
