package x.common.component.finder;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-08-11
 * GitHub: https://github.com/ccolorcat
 */
public final class FileInfo {
    @NonNull
    public static FileInfo resolve(@NonNull ContentResolver resolver, @NonNull Uri uri) {
        FileInfo info = byCursor(resolver, uri);
        if (info == null) {
            Filename filename = resolveFilename(resolver, uri);
            long fileSize = resolveFileSize(resolver, uri);
            info = new FileInfo(filename, fileSize, uri);
        }
        return info;
    }

    @NonNull
    private static Filename resolveFilename(@NonNull ContentResolver resolver, @NonNull Uri uri) {
        Filename filename = Filename.fromUri(uri.toString());
        if (TextUtils.equals(Filename.UNKNOWN.type, filename.type)) {
            String mimeType = resolver.getType(uri);
            String[] types;
            if (mimeType != null && (types = mimeType.split("/")).length == 2) {
                String temp = types[1].trim().toLowerCase();
                if (Filetype.byType(temp) != Filetype.other) {
                    filename = Filename.of(filename.name, temp);
                }
            }
        }
        return filename;
    }

    private static long resolveFileSize(@NonNull ContentResolver resolver, @NonNull Uri uri) {
        try (ParcelFileDescriptor descriptor = resolver.openFileDescriptor(uri, "r")) {
            return descriptor != null ? descriptor.getStatSize() : -1L;
        } catch (IOException e) {
            Logger.getDefault().e(e);
        }
        return -1L;
    }

    @Nullable
    private static FileInfo byCursor(@NonNull ContentResolver resolver, @NonNull Uri uri) {
        try (Cursor cursor = resolver.query(uri, null, null, null, null)) {
            if (cursor == null) return null;
            int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            if (nameIndex == -1 || sizeIndex == -1) return null;
            Filename filename = Filename.from(cursor.getString(nameIndex));
            long fileSize = cursor.getLong(sizeIndex);
            return new FileInfo(filename, fileSize, uri);
        } catch (Throwable e) {
            Logger.getDefault().e(e);
        }
        return null;
    }


    public final Filename filename;
    public final long fileSize;
    private final Uri uri;

    private FileInfo(Filename filename, long fileSize, Uri uri) {
        this.filename = filename;
        this.fileSize = fileSize;
        this.uri = uri;
    }

    @Nullable
    public InputStream openInputStream(@NonNull ContentResolver resolver) throws FileNotFoundException {
        return resolver.openInputStream(uri);
    }

    @SuppressWarnings("EqualsReplaceableByObjectsCall")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileInfo fileinfo = (FileInfo) o;

        if (fileSize != fileinfo.fileSize) return false;
        return filename != null ? filename.equals(fileinfo.filename) : fileinfo.filename == null;
    }

    @Override
    public int hashCode() {
        int result = filename != null ? filename.hashCode() : 0;
        result = 31 * result + (int) (fileSize ^ (fileSize >>> 32));
        return result;
    }

    @NonNull
    @Override
    public String toString() {
        return "Fileinfo{" +
                "filename=" + filename +
                ", fileSize=" + fileSize +
                '}';
    }
}
