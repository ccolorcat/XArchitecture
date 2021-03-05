package cc.colorcat.xarchitecture.sample;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.collection.ArraySet;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.finder.FileInfo;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.finder.FinderCore;
import x.common.component.finder.StreamWriter;
import x.common.component.log.Logger;
import x.common.component.runtime.PermissionsHandler;
import x.common.component.runtime.RuntimeFor;
import x.common.component.runtime.RuntimeForResult;
import x.common.component.schedule.BackgroundXScheduler;
import x.common.component.schedule.MainXScheduler;
import x.common.util.IoUtils;
import x.common.util.Once;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2021-01-26
 * GitHub: https://github.com/ccolorcat
 */
public class Picker {
    @NonNull
    public static Picker withGeneral(@NonNull Context context) {
        return new Picker(context);
    }

    @NonNull
    public static Picker withImage(@NonNull Context context) {
        return new Picker(context).setType("image/*");
    }

    @NonNull
    public static Picker withVideo(@NonNull Context context) {
        return new Picker(context).setType("video/*");
    }


    private final Lazy<BackgroundXScheduler> backgroundXScheduler = Lazy.by(() -> Hummingbird.visit(BackgroundXScheduler.class));
    private final Lazy<MainXScheduler> mainXScheduler = Lazy.by(() -> Hummingbird.visit(MainXScheduler.class));
    private final Lazy<FinderCore> finder = Lazy.by(() -> Hummingbird.visit(FinderCore.class));
    private final Once<Intent> intent = new Once<>();
    private final Context context;
    private String action = Intent.ACTION_GET_CONTENT;
    private Uri data;
    private String type = "*/*";
    private ArraySet<String> categories;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private Callback callback;

    public Picker(@NonNull Context context) {
        this.context = Utils.requireNonNull(context, "context == null");
    }

    @NonNull
    public Picker setAction(@NonNull String action) {
        this.action = Utils.requireNonNull(action, "action == null");
        return this;
    }

    @NonNull
    public Picker setData(@NonNull Uri data) {
        this.data = Utils.requireNonNull(data, "data == null");
        return this;
    }

    @NonNull
    public Picker setType(@NonNull String type) {
        this.type = Utils.requireNonNull(type, "type == null");
        return this;
    }

    @NonNull
    public Picker addCategory(@NonNull String category) {
        Utils.requireNonNull(category, "category == null");
        if (categories == null) {
            categories = new ArraySet<>();
        }
        categories.add(category);
        return this;
    }

    @NonNull
    public Picker removeCategory(String category) {
        if (category == null || categories == null) return this;
        categories.remove(category);
        return this;
    }

    @NonNull
    public Picker setPermissions(@NonNull String... permissions) {
        this.permissions = Utils.requireNonNull(permissions);
        return this;
    }

    public void pick(@NonNull Callback callback) {
        this.callback = Utils.requireNonNull(callback, "callback == null");
        this.intent.set(resolve());
        requestPermissions();
    }


    @NonNull
    private Intent resolve() {
        Intent result = new Intent(action);
        if (data != null) {
            result.setDataAndType(data, type);
        } else {
            result.setType(type);
        }
        if (categories != null) {
            for (String category : categories) {
                result.addCategory(category);
            }
        }
        return result;
    }

    private void requestPermissions() {
        RuntimeFor.once(context).permissions(permissions).request(new PermissionsHandler() {
            @Override
            public void onAllGranted(@NonNull String[] permissions) {
                requestFile();
            }

            @Override
            public void onDeniedOccur(@NonNull String[] granted, @NonNull String[] denied) {
                callback.onFailed(new RuntimeException(Arrays.toString(denied) + " denied"));
            }
        });
    }

    private void requestFile() {
        RuntimeFor.once(context).intent(intent.get()).forResult((resultCode, data) -> {
            if (resultCode != RuntimeForResult.RESULT_OK) {
                callback.onFailed(new RuntimeException("pick file  failed, resultCode=" + resultCode));
            } else if (data == null || data.getData() == null) {
                callback.onFailed(new RuntimeException("pick file failed, data is null."));
            } else {
                backgroundXScheduler.get().execute(() -> handleUri(data.getData()));
            }
        });
    }

    private void handleUri(Uri data) {
        InputStream input = null;
        try {
            ContentResolver resolver = context.getContentResolver();
            FileInfo info = FileInfo.resolve(resolver, data);
            Filename filename = info.filename;
            FileOperator operator = finder.get().requireFileOperator(null, null, filename);
            input = resolver.openInputStream(data);
            operator.write(input, new StreamWriter());
            File file = new File(operator.getUri().getPath());
            if (file.exists()) {
                mainXScheduler.get().execute(() -> callback.onSucceed(file));
            } else {
                throw new IOException("IO Error");
            }
        } catch (Throwable t) {
            mainXScheduler.get().execute(() -> callback.onFailed(t));
            Logger.getDefault().e(t);
        } finally {
            IoUtils.close(input);
        }
    }

    public interface Callback {
        void onSucceed(@NonNull File file);

        void onFailed(@NonNull Throwable throwable);
    }
}
