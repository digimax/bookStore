package digimax.pages;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.services.app.BootupServiceImpl;
import digimax.services.domain.LibraryService;
import digimax.structural.ApplicationRuntimeException;
import digimax.structural.image.PNGInline;
import org.apache.tapestry5.*;
import org.apache.tapestry5.annotations.*;
import org.apache.tapestry5.internal.services.LinkSource;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A product of Digimax Technology Inc. (digimax.com)
 * User: jonwilliams
 * Date: 4/14/13
 * Time: 2:21 PM
 */
public class Browse {

    @Inject
    Session session;

    @Inject
    ComponentResources componentResources;

    @Inject
    private LinkSource linkSource;

    @Inject
    private Logger logger;

    @Inject
    LibraryService libraryService;

    @Property
    Book book;

    @Property
    Shelf shelf;

    public Library getLibrary() {
        return (Library) session.createCriteria(Library.class).uniqueResult();
    }

    public Image getSmallImage() {
        Image image = book.images.get(1);
        return image;
    }
}
