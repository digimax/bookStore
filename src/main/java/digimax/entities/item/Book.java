    package digimax.entities.item;

    import digimax.entities.people.Author;
    import digimax.entities.people.IdentityMeta;
    import org.apache.tapestry5.annotations.Property;
    import org.apache.tapestry5.beaneditor.NonVisual;

    import javax.persistence.*;

    import org.apache.tapestry5.beaneditor.Validate;
    import org.hibernate.annotations.Cascade;
    import org.hibernate.cache.ReadWriteCache;

    import java.util.*;

    @Entity
    public class Book extends Item {

        @Property
        @Validate("required")
        public String title;

        @Property
        public String subTitle;

        @Property
        @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "book")
        @Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
        public BookMeta bookMeta;

        @Property
        @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    //    @JoinTable(name="Book_Author",
    //            joinColumns={@JoinColumn(name="Book_id")},
    //            inverseJoinColumns={@JoinColumn(name="Author_id")})
        public Set<Author> authors = new HashSet<Author>();


        @Property
        @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH} )
        @Cascade( org.hibernate.annotations.CascadeType.SAVE_UPDATE )
        public Shelf shelf;

        public Book() {
            super();
            subTitle="";
        }

    //    public Long getUid() {
    //        return id;
    //    }

        public static final class Compare implements Comparator<Book> {

            @Override
            public int compare(Book book1, Book book2) {
                return book1.title.compareTo(book2.title);
            }
        }
    }
