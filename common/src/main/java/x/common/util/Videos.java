package x.common.util;

import android.media.MediaMetadataRetriever;

import androidx.annotation.NonNull;

import java.io.File;

import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-08-20
 * GitHub: https://github.com/ccolorcat
 */
public final class Videos {
    @NonNull
    public static Size parseSize(@NonNull File path) {
        if (path.exists()) {
            return parseSize(path.getAbsolutePath());
        }
        return new Size(0, 0);
    }

    @NonNull
    public static Size parseSize(@NonNull String path) {
        Utils.requireNonNull(path, "path == null");
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            mmr.setDataSource(path);
            String w = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
            String h = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
            return new Size(Integer.parseInt(w), Integer.parseInt(h));
        } catch (Throwable t) {
            Logger.getDefault().e(t);
            return new Size(0, 0);
        }
    }

    public static long parseDuration(@NonNull File path) {
        if (path.exists()) {
            return parseDuration(path.getAbsolutePath());
        }
        return -1L;
    }

    public static long parseDuration(@NonNull String path) {
        Utils.requireNonNull(path, "path == null");
        try (MediaMetadataRetriever mmr = new MediaMetadataRetriever()) {
            mmr.setDataSource(path);
            String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            return Long.parseLong(duration);
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        }
        return -1L;
    }

    private Videos() {
        throw new AssertionError("no instance");
    }
}
