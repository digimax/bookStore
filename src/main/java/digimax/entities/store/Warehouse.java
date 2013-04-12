package digimax.entities.store;

import digimax.entities.item.Shelf;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 2:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonVisual
    public Long id;

    @Property
    @Validate("required")
    public String name;

    @Property
    @OneToMany
    public List<Shelf> shelves;
}
