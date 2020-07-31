package x.common.component.store;

import androidx.annotation.NonNull;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-26
 * GitHub: https://github.com/ccolorcat
 */
final class RemoveStoreExecutor implements StoreExecutor {
    private final Store store;
    private final String key;
    private boolean needCommit;

    RemoveStoreExecutor(@NonNull Store store, @NonNull String key, boolean needCommit) {
        this.store = Utils.requireNonNull(store, "store == null");
        this.key = Utils.requireNonNull(key, "key == null");
        this.needCommit = needCommit;
    }

    @Override
    public Object execute(Object[] args) {
        Store.Editor editor = store.edit().remove(key);
        if (needCommit) return editor.commit();
        editor.apply();
        return null;
    }
}
