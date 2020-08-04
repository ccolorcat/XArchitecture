package x.common.component.finder;

import androidx.annotation.NonNull;

import java.net.URI;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-20
 */
public final class Filename {
    static final String EMPTY = "";
    public static final Filename UNKNOWN = new Filename(EMPTY, EMPTY);

//    @NonNull
//    public static Filename fromUri(String uri) {
//        try {
//            return fromUri(Uri.parse(uri));
//        } catch (Throwable e) {
//            return UNKNOWN;
//        }
//    }

//    @NonNull
//    public static Filename fromUri(Uri uri) {
//        if (uri == null) return UNKNOWN;
//        String u = uri.toString();
//        if (Utils.isEmpty(u) || u.endsWith("/")) return UNKNOWN;
//        return from(uri.getLastPathSegment());
//    }

    @NonNull
    public static Filename fromUri(String uri) {
        try {
            return fromUri(URI.create(uri));
        } catch (Throwable t) {
            return UNKNOWN;
        }
    }

    @NonNull
    public static Filename fromUri(URI uri) {
        String path;
        if (uri == null || (path = uri.getRawPath()) == null) return UNKNOWN;
        int index = path.lastIndexOf('/');
        if (index == -1) return from(path);
        if (index == path.length() - 1) return UNKNOWN;
        return from(path.substring(index + 1));
    }

    /**
     * @param filename 包含扩展名的文件名称
     */
    @NonNull
    public static Filename from(String filename) {
        if (Utils.isEmpty(filename) || ".".equals(filename)) return UNKNOWN;
        int index = filename.lastIndexOf('.');
        if (index == -1) return new Filename(filename, EMPTY);
        if (index == 0) return new Filename(EMPTY, filename.substring(1));
        if (index == filename.length() - 1) return new Filename(filename.substring(0, index), EMPTY);
        return new Filename(filename.substring(0, index), filename.substring(index + 1));
    }

    public static Filename of(String name, String type) {
        String n = Utils.nullElse(name, EMPTY);
        String t = Utils.nullElse(type, EMPTY);
        if (EMPTY.equals(n) && EMPTY.equals(t)) return UNKNOWN;
        return new Filename(n, t);
    }

    @NonNull
    public final String name;
    @NonNull
    public final String type;
    private Filetype filetype;

    private Filename(@NonNull String name, @NonNull String type) {
        this.name = Utils.requireNonNull(name, "name == null");
        this.type = Utils.requireNonNull(type, "type == null");
    }

    @NonNull
    public Filetype toFiletype() {
        if (filetype == null) {
            filetype = Filetype.byType(type);
        }
        return filetype;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Filename filename = (Filename) o;

        if (!name.equals(filename.name)) return false;
        return type.equals(filename.type);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return name + '.' + type;
    }
}
