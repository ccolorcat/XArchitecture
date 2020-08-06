package x.common.component.loader;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.io.File;

import x.common.component.Hummingbird;
import x.common.component.Lazy;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.finder.FinderCore;
import x.common.component.finder.Module;
import x.common.component.log.Logger;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-17
 */
public class ObjectLoader {
    static final Logger LOGGER = Logger.getLogger("ObjectLoader");

    @NonNull
    public static Uri parse(@NonNull Context context, @DrawableRes int drawableId) {
        return parse(context.getResources(), drawableId);
    }

    @NonNull
    public static Uri parse(@NonNull Resources resources, @DrawableRes int drawableId) {
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + resources.getResourcePackageName(drawableId)
                + '/' + resources.getResourceTypeName(drawableId)
                + '/' + resources.getResourceEntryName(drawableId));
    }


    public static ObjectLoader with(@NonNull View view) {
        return with(view.getContext());
    }

    public static ObjectLoader with(@NonNull Fragment fragment) {
        return with(fragment.requireContext());
    }

    public static ObjectLoader with(@NonNull Context context) {
        return new ObjectLoader(context);
    }

    private static final Lazy<FinderCore> FINDER = Lazy.by(() -> Hummingbird.visit(FinderCore.class));

    private Context context;
    private Uri uri;

    private String moduleName = Module.UNKNOWN, moduleId = Module.UNKNOWN;
    private Filename filename;

    private ObjectLoader(@NonNull Context context) {
        this.context = Utils.requireNonNull(context, "context == null");
    }

    public ObjectLoader asChatModule(@NonNull String chatId) {
        return module(Module.CHAT, chatId);
    }

    public ObjectLoader asAlbumModule(@NonNull String albumId) {
        return module(Module.ALBUM, albumId);
    }

    public ObjectLoader asQuickService(@NonNull String id) {
        return module(Module.QUICK_SERVICE, id);
    }

    public ObjectLoader module(@Module String moduleName, String moduleId) {
        this.moduleName = Utils.requireNonNull(moduleName, "moduleName == null");
        this.moduleId = Utils.requireNonNull(moduleId, "moduleId == null");
        return this;
    }

    public ObjectLoader filename(@NonNull Filename filename) {
        this.filename = Utils.requireNonNull(filename, "filename == null");
        return this;
    }

    public ObjectLoader filename(String name, String type) {
        this.filename = Filename.of(name, type);
        return this;
    }

    public ObjectLoader filename(String filename) {
        this.filename = Filename.from(filename);
        return this;
    }

    public ObjectLoader load(@NonNull File file) {
        this.uri = Uri.fromFile(file);
        return this;
    }

    public ObjectLoader load(@NonNull String url) {
        this.uri = Uri.parse(url);
        return this;
    }

    public ObjectLoader load(@NonNull Uri uri) {
        this.uri = Utils.requireNonNull(uri, "uri == null");
        return this;
    }

    @NonNull
    public ImageLoader asImage() {
        FileOperator operator = resolve();
        return new ImageLoader(Glide.with(context), uri, operator, filename);
    }

    @NonNull
    public Downloader asDownloader() {
        FileOperator operator = resolve();
        String scheme = uri.getScheme();
        if (scheme == null || !scheme.toLowerCase().startsWith("http")) {
            throw new IllegalArgumentException("not http or https uri");
        }
        return new Downloader(uri, operator);
    }

    @NonNull
    public FileOperator resolve() {
        Utils.requireNonNull(uri, "uri == null");
        if (filename == null) {
            filename = Filename.fromUri(uri.toString());
        }
        return FINDER.get().requireFileOperator(moduleName, moduleId, filename);
    }
}
