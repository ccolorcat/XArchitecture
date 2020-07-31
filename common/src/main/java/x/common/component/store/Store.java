package x.common.component.store;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
public interface Store {
    @Nullable
    String read(@NonNull String key);

    @NonNull
    Editor edit();

    interface Editor {
        @NonNull
        Editor write(@NonNull String key, String value);

        @NonNull
        Editor remove(@NonNull String key);

        @NonNull
        Editor clear();

        void apply();

        boolean commit();
    }
}
