package x.common.component.finder;

import androidx.annotation.NonNull;

import java.util.Arrays;
import java.util.Comparator;

import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-04-14
 * <p>
 * 需求中音频含 rm 与视频中的 rm 冲突。
 */
public enum Filetype {
    image("jpg", "jpeg", "png", "bmp", "tiff", "gif", "tga", "tps", "psd"),
    doc("txt", "rtf", "doc", "docx", "pages", "xls", "xlsx", "numbers", "ppt", "pptx", "key", "pdf", "mmap", "xmind", "mindly", "mindnode", "rp"),
    video("mp4", "avi", "wmv", "swf", "mov", "rm", "rmvb", "mkv", "flv", "3gp"),
    audio("mp3", "wav", "wma", "ogg", "ape", "flac", "aac", "mpeg", "ra", "rmx"),
    compress("zip", "rar", "7z", "tar", "iso"),
    other("");

    private static final Comparator<String> COMPARATOR = String::compareTo;
    String[] types;

    Filetype(String... types) {
        this.types = types;
        Arrays.sort(this.types, String::compareTo);
    }

    @Override
    public String toString() {
        return "Filetype{" +
                "name=" + name() + ',' +
                "types=" + Arrays.toString(types) +
                '}';
    }

    @NonNull
    public static Filetype byUri(String uri) {
        return Filename.fromUri(uri).toFiletype();
    }

    @NonNull
    public static Filetype byFilename(String filename) {
        return Filename.from(filename).toFiletype();
    }

    @NonNull
    public static Filetype byType(String type) {
        if (Utils.isEmpty(type)) return other;
        String lowercase = type.toLowerCase();
        for (Filetype filetype : Filetype.values()) {
            if (Arrays.binarySearch(filetype.types, lowercase, COMPARATOR) >= 0) {
                return filetype;
            }
        }
        return other;
    }
}
