package digimax.entities.people;

import digimax.structural.DomainObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.NonVisual;
import org.apache.tapestry5.beaneditor.Validate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created with IntelliJ IDEA.
 * User: jonwilliams
 * Date: 4/6/13
 * Time: 8:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
public class IdentityMeta extends DomainObject {

    @Property
    @Validate("required")
    String password;

    public boolean isCorrectPassword(String password) {
        return this.password!=null && this.password.equals(DigestUtils.md5Hex(password));
    }

    public void setPassword(String password) {
        this.password = (password==null) ? null : DigestUtils.md5Hex(password);
    }
}
