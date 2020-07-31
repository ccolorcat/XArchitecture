package x.common.component.store;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.util.Utils;


/**
 * Author: cxx
 * Date: 2020-07-24
 * GitHub: https://github.com/ccolorcat
 */
final class SharedPreferencesStore implements Store {
    private final SharedPreferences mDelegate;

    SharedPreferencesStore(@NonNull SharedPreferences delegate) {
        mDelegate = Utils.requireNonNull(delegate, "delegate == null");
    }

    @Nullable
    @Override
    public String read(@NonNull String key) {
        return mDelegate.getString(key, null);
    }

    @NonNull
    @Override
    public Store.Editor edit() {
        return new Editor(mDelegate.edit());
    }

    private static class Editor implements Store.Editor {
        private final SharedPreferences.Editor mEditor;

        private Editor(@NonNull SharedPreferences.Editor editor) {
            mEditor = editor;
        }

        @NonNull
        @Override
        public Store.Editor write(@NonNull String key, String value) {
            mEditor.putString(key, value);
            return this;
        }

        @NonNull
        @Override
        public Store.Editor remove(@NonNull String key) {
            mEditor.remove(key);
            return this;
        }

        @NonNull
        @Override
        public Store.Editor clear() {
            mEditor.clear();
            return this;
        }

        @Override
        public void apply() {
            mEditor.apply();
        }

        @Override
        public boolean commit() {
            return mEditor.commit();
        }
    }
}
