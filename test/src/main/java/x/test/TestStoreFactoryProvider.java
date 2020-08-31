package x.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import x.common.IClient;
import x.common.component.store.BaseStoreFactoryProvider;
import x.common.component.store.Store;

/**
 * Author: cxx
 * Date: 2020-07-27
 * GitHub: https://github.com/ccolorcat
 */
final class TestStoreFactoryProvider extends BaseStoreFactoryProvider {
    protected TestStoreFactoryProvider(@NonNull IClient client) {
        super(client);
    }

    @NonNull
    @Override
    protected Store newStore(String name) {
        return new Store() {
            private final Map<String, String> delegate = new HashMap<>();

            @Nullable
            @Override
            public String read(@NonNull String key) {
                return delegate.get(key);
            }

            @NonNull
            @Override
            public Editor edit() {
                return new Editor() {
                    @NonNull
                    @Override
                    public Editor write(@NonNull String key, String value) {
                        delegate.put(key, value);
                        return this;
                    }

                    @NonNull
                    @Override
                    public Editor remove(@NonNull String key) {
                        delegate.remove(key);
                        return this;
                    }

                    @NonNull
                    @Override
                    public Editor clear() {
                        delegate.clear();
                        return this;
                    }

                    @Override
                    public void apply() {

                    }

                    @Override
                    public boolean commit() {
                        return true;
                    }
                };
            }
        };
    }
}
