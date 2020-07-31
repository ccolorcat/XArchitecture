package x.common.component.store;

import androidx.annotation.NonNull;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-26
 * GitHub: https://github.com/ccolorcat
 */
final class ClearStoreExecutor implements StoreExecutor {
    private final Store store;
    private final boolean needCommit;

    ClearStoreExecutor(@NonNull Store store, boolean needCommit) {
        this.store = Utils.requireNonNull(store, "store == null");
        this.needCommit = needCommit;
    }

    @Override
    public Object execute(Object[] args) {
        Store.Editor editor = store.edit().clear();
        if (needCommit) return editor.commit();
        editor.apply();
        return null;
    }
}
