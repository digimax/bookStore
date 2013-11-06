package digimax.services.app;

import digimax.entities.app.Image;
import digimax.entities.item.Book;
import digimax.entities.item.BookMeta;
import digimax.entities.item.Shelf;
import digimax.entities.library.Library;
import digimax.entities.people.Author;
import digimax.entities.people.Employee;
import digimax.services.domain.*;
import digimax.structural.RecursiveFileListIterator;
import org.apache.commons.lang.WordUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.hibernate.Session;
import org.slf4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BootupServiceImpl implements BootupService {

    //System property digimax.ncapsuld.rip.root
    private static String BOOK_RIP_ROOT_FOLDER = System.getProperty("digimax.ncapsuld.rip.root")!=null?
            System.getProperty("digimax.ncapsuld.rip.root"): "/dig/wrk/maw_raw/pro/";
    //System property digimax.ncapsuld.app.image.folder
    public static String APP_IMAGE_FOLDER = System.getProperty("digimax.ncapsuld.app.image.folder")!=null?
            System.getProperty("digimax.ncapsuld.app.image.folder"): "/dig/wrk/maw_raw/app_images_folder/";


    @Inject
    private Logger logger;

    @Inject
    private Session session;

    @Inject
    private BookService bookService;

    @Inject
    private LibraryService libraryService;

    @Inject
    private LocationService locationService;

    @Inject
    private ImageService imageService;

    public Library bootupLibrary() {
        long startTime = System.currentTimeMillis();
        Library library = libraryService.testInstance();
        library.name = "Qa Fresh Library";

        Employee librarian = new Employee();
        librarian.firstName="Jon";
        librarian.lastName="Williams";
        librarian.userName="jon@digimax.com";
        librarian.identityMeta.setNewPassword("empty0");
        library.users.add(librarian);



        libraryService.save(library);

//        List<Book> newBooks = new ArrayList<Book>();

        File rootFolder = new File(BOOK_RIP_ROOT_FOLDER);
        Iterator<File> fileIterator = new RecursiveFileListIterator(rootFolder);
        while (fileIterator.hasNext()) {
            File file = fileIterator.next();
            String fileAbsolutePath = file.getAbsolutePath();
            logger.debug("examining File :: {}", fileAbsolutePath);
            if (fileAbsolutePath.toLowerCase().contains(".png") && !fileAbsolutePath.toUpperCase().contains("SPINE")) {
                String fileNameWithLocation =
                        fileAbsolutePath.substring(BOOK_RIP_ROOT_FOLDER.length(), fileAbsolutePath.length());
                fileNameWithLocation = fileNameWithLocation.replace('^', '~');
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
                        fileName.substring(0, fileName.indexOf('~')).replace('_', ' '):
                        fileName.substring(0,fileName.lastIndexOf('.')).replace('_', ' ');
                String bookSubTitle = null;
                if (bookTitle.contains("[")&&bookTitle.contains("]")) {
                    bookSubTitle = bookTitle.substring(bookTitle.indexOf('[')+1, bookTitle.indexOf(']'));
                    bookTitle = bookTitle.substring(0, bookTitle.indexOf('['));
                }

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
                    firstAuthorFirstName = "";
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
                searchAuthor.lastName = WordUtils.capitalizeFully(firstAuthorLastName);
                searchAuthor.firstName = WordUtils.capitalizeFully(firstAuthorFirstName);

                Book searchBook = new Book();
                searchBook.title = WordUtils.capitalizeFully(bookTitle);
                searchBook.subTitle = WordUtils.capitalizeFully(bookSubTitle);
                searchBook.authors.add(searchAuthor);
                if (secondAuthorLastName!=null && secondAuthorLastName.length()>0) {
                    Author secondSearchAuthor = new Author();
                    secondSearchAuthor.lastName = WordUtils.capitalizeFully(secondAuthorLastName);
                    secondSearchAuthor.firstName = WordUtils.capitalizeFully(secondAuthorFirstName);
                    searchBook.authors.add(secondSearchAuthor);
                }
                Book book = bookService.findOrCreateBook(searchBook);
                for (Image image : spineImages) {
                    image.item = book;
                }
                book.images.addAll(spineImages);
                //new BookMeta(book);
                List<Book> receivedBooks = new ArrayList<>();
                Shelf shelf = (Shelf) locationService.findOrCreateLibraryLocation(library, locationName);
                receivedBooks.add(book);
                libraryService.receive(library, shelf, receivedBooks);
            }
        }
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