package digimax.services.app;

import digimax.entities.library.Library;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.ioc.annotations.Startup;

public interface BootupService {

    @CommitAfter
    Library bootupLibrary();

    boolean hasPerformedBootup();
}
