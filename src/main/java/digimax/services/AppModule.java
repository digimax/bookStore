package digimax.services;

import java.io.IOException;
import java.util.Map;

import digimax.services.app.BootupServiceImpl;
import digimax.services.app.asset.FileAssetAliasManager;
import digimax.services.app.asset.FileBindingFactory;
import digimax.services.app.asset.FilePathAssetFactory;
import digimax.services.app.asset.FilePathAssetRequestHandler;
import digimax.services.domain.ImageService;
import digimax.services.domain.*;
import org.apache.tapestry5.*;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.internal.services.AssetResourceLocator;
import org.apache.tapestry5.internal.services.RequestConstants;
import org.apache.tapestry5.internal.services.ResourceStreamer;
import org.apache.tapestry5.internal.services.assets.ClasspathAssetRequestHandler;
import org.apache.tapestry5.internal.services.assets.ContextAssetRequestHandler;
import org.apache.tapestry5.internal.services.assets.StackAssetRequestHandler;
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.ioc.annotations.Autobuild;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.services.*;
import org.apache.tapestry5.services.assets.AssetRequestHandler;
import org.got5.tapestry5.jquery.JQuerySymbolConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This module is automatically included as part of the Tapestry IoC Registry, it's a good place to
 * configure and extend Tapestry, or to place your own service definitions.
 */
public class AppModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AppModule.class);

    @Match("*Service")
    public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
    {
        advisor.addTransactionCommitAdvice(receiver);
    }

    public static void bind(ServiceBinder binder)
    {
        {
            binder.bind(FileAssetAliasManager.class);
            binder.bind(FilePathAssetFactory.class);
            binder.bind(BindingFactory.class,
                FileBindingFactory.class).withId("FileBindingFactory");



            binder.bind(BookService.class);
            binder.bind(BookMetaService.class);
            binder.bind(LibraryService.class);
            binder.bind(LocationService.class);
            binder.bind(PersonService.class);
            binder.bind(StoreService.class);
            binder.bind(ImageService.class);
            binder.bind(ItemService.class);
        }
        // binder.bind(MyServiceInterface.class, MyServiceImpl.class);

        // Make bind() calls on the binder object to define most IoC services.
        // Use service builder methods (example below) when the implementation
        // is provided inline, or requires more initialization than simply
        // invoking the constructor.
    }

    public static void contributeFactoryDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // The app version number is incorprated into URLs for some
        // assets. Web browsers will cache assets because of the far future expires
        // header. If existing assets are changed, the version number should also
        // change, to force the browser to download new versions. This overrides Tapesty's default
        // (a random hexadecimal number), but may be further overriden by DevelopmentModule or
        // QaModule.
        configuration.override(SymbolConstants.APPLICATION_VERSION, "1.0-SNAPSHOT");
    }

    public static void contributeApplicationDefaults(
            MappedConfiguration<String, Object> configuration)
    {
        // Contributions to ApplicationDefaults will override any contributions to
        // FactoryDefaults (with the same key). Here we're restricting the supported
        // locales to just "en" (English). As you add localised message catalogs and other assets,
        // you can extend this list of locales (it's a comma separated series of locale names;
        // the first locale name is the default when there's no reasonable match).
        configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en");
        configuration.add(SymbolConstants.HMAC_PASSPHRASE, "dontdosmebro");
        configuration.add(JQuerySymbolConstants.SUPPRESS_PROTOTYPE, "true");
        configuration.add(JQuerySymbolConstants.JQUERY_ALIAS, "$");
    }

    /**
     * Keep Tapestry from processing requests to the web service path.
     *
     * @param configuration {@link Configuration}
     */
    public static void contributeIgnoredPathsFilter(
            final Configuration<String> configuration) {
        configuration.add("/ws/.*");
    }

    /**
     * This is a service definition, the service will be named "TimingFilter". The interface,
     * RequestFilter, is used within the RequestHandler service pipeline, which is built from the
     * RequestHandler service configuration. Tapestry IoC is responsible for passing in an
     * appropriate Logger instance. Requests for static resources are handled at a higher level, so
     * this filter will only be invoked for Tapestry related requests.
     * <p/>
     * <p/>
     * Service builder methods are useful when the implementation is inline as an inner class
     * (as here) or require some other kind of special initialization. In most cases,
     * use the static bind() method instead.
     * <p/>
     * <p/>
     * If this method was named "build", then the service id would be taken from the
     * service interface and would be "RequestFilter".  Since Tapestry already defines
     * a service named "RequestFilter" we use an explicit service id that we can reference
     * inside the contribution method.
     */
    public RequestFilter buildTimingFilter(final Logger log)
    {
        return new RequestFilter()
        {
            public boolean service(Request request, Response response, RequestHandler handler)
                    throws IOException
            {
                long startTime = System.currentTimeMillis();

                try
                {
                    // The responsibility of a filter is to invoke the corresponding method
                    // in the handler. When you chain multiple filters together, each filter
                    // received a handler that is a bridge to the next filter.

                    return handler.service(request, response);
                } finally
                {
                    long elapsed = System.currentTimeMillis() - startTime;

                    log.info(String.format("Request time: %d ms", elapsed));
                }
            }
        };
    }

    /**
     * This is a contribution to the RequestHandler service configuration. This is how we extend
     * Tapestry using the timing filter. A common use for this kind of filter is transaction
     * management or security. The @Local annotation selects the desired service by type, but only
     * from the same module.  Without @Local, there would be an error due to the other service(s)
     * that implement RequestFilter (defined in other modules).
     */
    public void contributeRequestHandler(OrderedConfiguration<RequestFilter> configuration,
                                         @Local
                                         RequestFilter filter)
    {
        // Each contribution to an ordered configuration has a name, When necessary, you may
        // set constraints to precisely control the invocation order of the contributed filter
        // within the pipeline.

        configuration.add("Timing", filter);
    }

    public static void contributeAssetDispatcher(MappedConfiguration<String, AssetRequestHandler> configuration,

//                                                 @ContextProvider
                                                 FilePathAssetFactory fileAssetFactory,

                                                 @Autobuild
                                                 StackAssetRequestHandler stackAssetRequestHandler,

                                                 FileAssetAliasManager fileAssetAliasManager, ResourceStreamer streamer,
                                                 AssetResourceLocator assetResourceLocator)
    {

        LOGGER.debug("contributeAssetDispatcher fileAssetFactory :: {}", fileAssetFactory);
        LOGGER.debug("contributeAssetDispatcher fileAssetAliasManager :: {}", fileAssetAliasManager);

//        Map<String, String> mappings = classpathAssetAliasManager.getMappings();
//
//        for (String folder : mappings.keySet())
//        {
//            String path = mappings.get(folder);
//
//            configuration.add(folder, new ClasspathAssetRequestHandler(streamer, assetResourceLocator, path));
//        }
//
        configuration.add("images",
                new FilePathAssetRequestHandler(streamer, fileAssetFactory.getRootResource()));
    }

    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
//        configuration.add("digimax.entities.geo");
//        configuration.add("digimax.entities.invoice");
//        configuration.add("digimax.entities.item");
//        configuration.add("digimax.entities.library");
//        configuration.add("digimax.entities.people");
//        configuration.add("digimax.entities.store");
    }

    public static void
    contributeFileAssetAliasManager(MappedConfiguration<String, String>
                                            configuration) {
        configuration.add("images",
                "file:"+BootupServiceImpl.APP_IMAGE_FOLDER);
    }

    public void contributeAssetSource(MappedConfiguration<String,
            AssetFactory> configuration,
                                      FilePathAssetFactory filesystemAssetFactory) {
        configuration.add("file", filesystemAssetFactory);
    }

    public static void contributeBindingSource(MappedConfiguration<String,
            BindingFactory> configuration,
                                               @InjectService("FileBindingFactory") org.apache.tapestry5.services.BindingFactory
                                                       fileBindingFactory) {
        configuration.add("file", fileBindingFactory);
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
//        configuration.add(new LibraryMapping("", "digimax.components.domain"));
//        configuration.add(new LibraryMapping("", "com.example.app.chat"));
    }
}
