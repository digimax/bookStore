package digimax.services.app;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.people.Author;
import digimax.services.domain.BookService;
import digimax.services.domain.ImageService;
import digimax.services.domain.LibraryService;
import digimax.services.domain.PersonService;
import digimax.structural.RecursiveFileListIterator;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BootupServiceImpl implements BootupService {

    private static String BOOK_RIP_ROOT_FOLDER = "/Users/jonwilliams/wrk/maw_raw/pro/";
    public static String APP_IMAGE_FOLDER = "/Users/jonwilliams/wrk/digimax/nc/ncapsuld/target/app_images_folder/";


    @Inject
    private Logger logger;

    @Inject
    private Session session;

    @Inject
    private BookService bookService;

    @Inject
    private LibraryService libraryService;

    @Inject
    private ImageService imageService;

    public Library bootupLibrary() {
        long startTime = System.currentTimeMillis();
        Library library = libraryService.testInstance();
        library.name = "Qa Fresh Library";

        libraryService.save(library);

        List<Book> newBooks = new ArrayList<Book>();

        Iterator<File> fileIterator = new RecursiveFileListIterator(new File(BOOK_RIP_ROOT_FOLDER));
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            String fileAbsolutePath = file.getAbsolutePath();
            logger.debug("examining File :: {}", fileAbsolutePath);
            if (fileAbsolutePath.toLowerCase().contains(".png") && !fileAbsolutePath.toUpperCase().contains("SPINE")) {
                String fileNameWithLocation =
                        fileAbsolutePath.substring(BOOK_RIP_ROOT_FOLDER.length(), fileAbsolutePath.length());
                logger.debug("  processing File :: {}", fileNameWithLocation);
                String locationName = fileNameWithLocation.substring(0, fileNameWithLocation.lastIndexOf(File.separator));
                locationName = locationName.replaceAll(File.separator, ".");
                logger.debug("      locationName :: {}", locationName);
                String fileName = fileNameWithLocation.substring(locationName.length()+1, fileNameWithLocation.length());
                logger.debug("      fileName :: {}", fileName);
                String authorsName = (fileName.indexOf('~')>=0 && fileName.lastIndexOf('.')>=0)?
                        fileName.substring(fileName.indexOf('~')+1, fileName.lastIndexOf('.')):
                        null;
                logger.debug("      authorsName :: {}", authorsName);

                String bookTitle = (fileName.indexOf('~')>0)?
                        fileName.substring(0, fileName.indexOf('~')):
                        fileName.substring(0,fileName.lastIndexOf('.')).replace('_', ' ');
                String bookSubTitle = null;
                logger.debug("      title :: {}", bookTitle);
                String firstAuthorLastName = null;
                String firstAuthorFirstName = null;
                String secondAuthorLastName = null;
                String secondAuthorFirstName = null;

                if (authorsName!=null) {
                    boolean isThereASecondAuthor = authorsName.contains("&");
                    if (isThereASecondAuthor) {
                        String firstAuthorFullName = authorsName.substring(0, authorsName.indexOf("&"));
                        String secondAuthorFullName = authorsName.substring(authorsName.indexOf("&")+1, authorsName.length());
                        firstAuthorFirstName = (firstAuthorFullName.indexOf("_")>0)?firstAuthorFullName.substring(0, firstAuthorFullName.indexOf("_")):null;
                        firstAuthorLastName = (firstAuthorFullName.indexOf("_")>0)?firstAuthorFullName.substring(firstAuthorFullName.indexOf("_")+1, firstAuthorFullName.length()):firstAuthorFullName;
                        secondAuthorFirstName = (secondAuthorFullName.indexOf("_")>0)?secondAuthorFullName.substring(0, secondAuthorFullName.indexOf("_")):null;
                        secondAuthorLastName = (secondAuthorFullName.indexOf("_")>0)?secondAuthorFullName.substring(secondAuthorFullName.indexOf("_")+1, secondAuthorFullName.length()):secondAuthorFullName;

                    } else {
                        firstAuthorFirstName = (authorsName.indexOf("_")>0)?authorsName.substring(0, authorsName.indexOf("_")):null;
                        firstAuthorLastName = (authorsName.indexOf("_")>0)?authorsName.substring(authorsName.indexOf("_")+1, authorsName.length()):authorsName;
                    }
                } else {
                    firstAuthorFirstName = "UNKNOWN";
                    firstAuthorLastName = "UNKNOWN";
                }
                firstAuthorLastName = (firstAuthorLastName!=null)?firstAuthorLastName.replace('_', ' ')
                        : firstAuthorLastName;
                secondAuthorLastName = (secondAuthorLastName!=null)?secondAuthorLastName.replace('_', ' ')
                        : secondAuthorLastName;


                logger.debug("          firstAuthorFirstName :: {}", firstAuthorFirstName);
                logger.debug("          firstAuthorLastName :: {}", firstAuthorLastName);
                logger.debug("          secondAuthorFirstName :: {}", secondAuthorFirstName);
                logger.debug("          secondAuthorLastName :: {}", secondAuthorLastName);


                List<Image> spineImages = imageService.createImages(APP_IMAGE_FOLDER, fileName, file);
                Author searchAuthor = new Author();
                searchAuthor.lastName = firstAuthorLastName;
                searchAuthor.firstName = firstAuthorFirstName;

                Author secondSearchAuthor = new Author();
                secondSearchAuthor.lastName = secondAuthorLastName;
                secondSearchAuthor.firstName = secondAuthorFirstName;

                Book searchBook = new Book();
                searchBook.title = bookTitle;
                searchBook.subTitle = bookSubTitle;
                searchBook.authors.add(searchAuthor);
                searchBook.authors.add(secondSearchAuthor);
                Book book = bookService.findOrCreateBook(searchBook);
                for (Image image : spineImages) {
                    image.item = book;
                }
                book.images.addAll(spineImages);
                newBooks.add(book);
            }
        }
        libraryService.receive(library, newBooks);
        long finishTime = System.currentTimeMillis();
        logger.debug(String.format("bootupLibrary method. Time elapsed while Populating new Library :: %03d (seconds)"
                , (finishTime-startTime)/1000));
        return library;
    }

    public boolean hasPerformedBootup() {
        Library persistedLibrary = (Library) session.createCriteria(Library.class).uniqueResult();
        return persistedLibrary!=null && persistedLibrary.id!=null;
    }
}