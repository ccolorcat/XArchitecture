package x.common.component.store;

import androidx.annotation.NonNull;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-26
 * GitHub: https://github.com/ccolorcat
 */
final class WriteStoreExecutor implements StoreExecutor {
    private final Store store;
    private final StoreSerializer serializer;
    private final String[] keys;
    private final boolean needCommit;

    WriteStoreExecutor(@NonNull Store store, @NonNull StoreSerializer serializer, @NonNull String[] keys, boolean needCommit) {
        this.store = Utils.requireNonNull(store, "store == null");
        this.serializer = Utils.requireNonNull(serializer, "serializer == null");
        this.keys = Utils.requireNonNull(keys, "keys == null");
        this.needCommit = needCommit;
    }

    @Override
    public Object execute(Object[] args) {
        if (keys.length != args.length) {
            throw new AssertionError("the number of args not match @Write");
        }
        Store.Editor editor = store.edit();
        for (int i = 0, size = keys.length; i < size; ++i) {
            editor.write(keys[i], serializer.serialize(args[i]));
        }
        if (needCommit) return editor.commit();
        editor.apply();
        return null;
    }
}
