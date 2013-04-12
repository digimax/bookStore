package digimax.services;

import java.io.IOException;

import digimax.entities.library.Library;
import digimax.services.app.BootupService;
import org.apache.tapestry5.*;
import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionSource;
import org.apache.tapestry5.hibernate.HibernateSymbols;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.ApplicationDefaults;
import org.apache.tapestry5.ioc.services.SymbolProvider;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;

/**
 * This module is automatically included as part of the Tapestry IoC Registry if <em>tapestry.execution-mode</em>
 * includes <code>qa</code> ("quality assurance").
 */
public class QaModule
{


    public static void bind(ServiceBinder binder)
    {
        // Bind any services needed by the QA team to produce their reports
        // binder.bind(MyServiceMonitorInterface.class, MyServiceMonitorImpl.class);
        binder.bind(BootupService.class);
    }


    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The factory default is true but during the early stages of an app
        // overriding to false is a good idea. In addition, this is often overridden
        // on the command line as -Dtapestry.production-mode=false
        configuration.add(SymbolConstants.PRODUCTION_MODE, false);

        // The app version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions.
        configuration.add(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT-QA");
    }

        @Contribute(SymbolProvider.class)
        @ApplicationDefaults
        public static void provideApplicationDefaults(
                MappedConfiguration<String, String> configuration) {
            configuration.add(HibernateSymbols.DEFAULT_CONFIGURATION,
                    "false");
            configuration.add("tapestry.app-package",
                    "digimax");
        }
    @Contribute(HibernateSessionSource.class)
    public static void provideHibernateConfigurer(
            OrderedConfiguration<HibernateConfigurer>
                    configuration) {
        HibernateConfigurer configurer = new HibernateConfigurer() {

            public void configure(Configuration configuration) {
                configuration.configure("test-hibernate.xml");
            }
        };

        configuration.add("TestConfig", configurer, "after:*");

    }

    @Startup //TODO: is this a known bug in Tapestry IOC that this method is being invoked twice?
    public static void bootupApplication(Logger logger, BootupService bootupService)
    {
//        if (bootupService.hasPerformedBootup()) {//work around for tapestry bug
//            logger.info("Starting up...");
//            logger.debug("bootupApplication ");
//            Library library = bootupService.bootupLibrary();
//        }
    }


}
