package x.common.test;


import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.log.LogPrinter;
import x.common.component.network.ApiFactoryProvider;
import x.common.component.network.ApiFactoryProviderImpl;
import x.common.component.schedule.MainXScheduler;
import x.common.component.store.StoreFactoryProvider;

/**
 * Author: cxx
 * Date: 2020-07-28
 * GitHub: https://github.com/ccolorcat
 */
public final class TestManager {

    public static void init() {
        IClient client = new TestClient();
        Hummingbird.init(client);
        Hummingbird.registerStateless(LogPrinter.class, new TestLogPrinter());
        Hummingbird.registerStateless(StoreFactoryProvider.class, new TestStoreFactoryProvider(client));
        Hummingbird.registerStateless(ApiFactoryProvider.class, new ApiFactoryProviderImpl(Hummingbird.getClient()));
        Hummingbird.registerStateless(MainXScheduler.class, new TestMainScheduler());
    }
}
