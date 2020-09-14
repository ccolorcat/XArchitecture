package x.test;


import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.core.ClientInfoProvider;
import x.common.component.log.LogPrinter;
import x.common.component.network.ApiFactoryProvider;
import x.common.component.network.ApiFactoryProviderImpl;
import x.common.component.schedule.BackgroundHandlerXScheduler;
import x.common.component.schedule.MainXScheduler;
import x.common.component.store.StoreFactoryProvider;

/**
 * Author: cxx
 * Date: 2020-07-28
 * GitHub: https://github.com/ccolorcat
 */
public final class TestInitializer {

    public static void init() {
        IClient client = new TestClient();
        Hummingbird.init(client);
        Hummingbird.registerStateless(LogPrinter.class, new TestLogPrinter());
//        Hummingbird.registerStateless(StoreFactoryProvider.class, new TestStoreFactoryProvider(client));
        Hummingbird.registerStateless(ClientInfoProvider.class, new TestClientInfoProvider());
        Hummingbird.registerStateless(StoreFactoryProvider.class, new DiskStoreFactoryProvider(client));
        Hummingbird.registerStateless(ApiFactoryProvider.class, new ApiFactoryProviderImpl(Hummingbird.getClient()));
        Hummingbird.registerStateless(MainXScheduler.class, new TestMainScheduler());
        Hummingbird.registerStateless(BackgroundHandlerXScheduler.class, new TestBackgroundHandlerXScheduler());
    }

    public static String testUsername() {
        return "18986430015";
    }

    public static String testPassword() {
        return "cl031018";
    }
}
