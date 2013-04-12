package digimax.entities.invoice;

import digimax.entities.item.Item;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.hibernate.HibernatePersistenceConstants;

import javax.persistence.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 1:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Invoice extends Item {

    @Property
//    @Persist(HibernatePersistenceConstants.ENTITY)
    @OneToMany
    public List<InvoiceLineItem> lineItems;
}
