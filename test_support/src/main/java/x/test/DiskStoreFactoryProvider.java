package x.test;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.finder.DirOperator;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filetype;
import x.common.component.finder.FinderCore;
import x.common.component.finder.Module;
import x.common.component.finder.StringReader;
import x.common.component.finder.StringWriter;
import x.common.component.store.BaseStoreFactoryProvider;
import x.common.component.store.Store;

/**
 * Author: cxx
 * Date: 2020-08-31
 * GitHub: https://github.com/ccolorcat
 */
final class DiskStoreFactoryProvider extends BaseStoreFactoryProvider {
    private static final String STORE = Module.UNKNOWN;
    private final FinderCore finder = Hummingbird.visit(FinderCore.class);

    protected DiskStoreFactoryProvider(@NonNull IClient client) {
        super(client);
    }

    @NonNull
    @Override
    protected Store newStore(String name) {
        return new Store() {
            @Nullable
            @Override
            public String read(@NonNull String key) {
                FileOperator fo = getFileOperator(key);
                return fo.exists() ? fo.quietRead(new StringReader()) : null;
            }

            @NonNull
            @Override
            public Editor edit() {
                return new Editor() {
                    private boolean success = true;

                    @NonNull
                    @Override
                    public Editor write(@NonNull String key, String value) {
                        success = getFileOperator(key).quietWrite(value, new StringWriter());
                        return this;
                    }

                    @NonNull
                    @Override
                    public Editor remove(@NonNull String key) {
                        getFileOperator(key).delete();
                        return this;
                    }

                    @NonNull
                    @Override
                    public Editor clear() {
                        DirOperator operator = finder.getDirOperator(STORE, name, Filetype.other);
                        if (operator != null) operator.delete();
                        return this;
                    }

                    @Override
                    public void apply() {

                    }

                    @Override
                    public boolean commit() {
                        return success;
                    }
                };
            }

            private FileOperator getFileOperator(String key) {
                return finder.getFileOperator(STORE, name, key, "");
            }
        };
    }
}
