package digimax.services.domain;

import digimax.entities.item.Book;
import digimax.entities.item.BookMeta;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/18/13
 * Time: 12:39 PM
 */
public interface BookMetaService {

    @CommitAfter
    void save(BookMeta bookMeta);

    @CommitAfter
    void delete(BookMeta bookMeta);

    @CommitAfter
    void populateBookMeta(final Book book);
}
