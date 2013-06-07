package digimax.structural;

import org.apache.tapestry5.beaneditor.NonVisual;

import javax.persistence.*;

@MappedSuperclass
public class DomainObject {

    @Id
    @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonVisual
    public Long id;


    @Override
    public boolean equals(Object o) {
        DomainObject domainObject = (DomainObject) o;
        boolean isEqual = (domainObject!=null && getClass() == domainObject.getClass() &&
                (domainObject==this || (domainObject.id!=null && (domainObject.id.longValue()==this.id.longValue()))));
        return isEqual;
    }
}
