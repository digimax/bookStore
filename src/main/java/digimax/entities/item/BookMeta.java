package digimax.entities.item;

import digimax.structural.DomainObject;
import org.apache.tapestry5.annotations.Property;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/18/13
 * Time: 12:09 PM
 */
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
public class BookMeta extends DomainObject {

    @Property
    public String isbn;

    @Property
    public String isbn13;

    @Property
    public String authorNames;

    @Property
    public String description;

    @Property
    public String title;

    @Property
    public String longTitle;

    @Property
    public String publisherName;

    @Property
    public String thumbnailUrl;

    @Property
    @OneToOne(fetch = FetchType.LAZY)//, optional=true)
    @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
//    @PrimaryKeyJoinColumn
    public Book book;
}
