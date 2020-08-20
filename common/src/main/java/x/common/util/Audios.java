package x.common.util;

import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import java.io.File;

import x.common.component.log.Logger;

/**
 * Author: cxx
 * Date: 2020-08-20
 * GitHub: https://github.com/ccolorcat
 */
public final class Audios {
    public static long parseDuration(@NonNull File path) {
        if (path.exists()) {
            return parseDuration(path.getAbsolutePath());
        }
        return -1L;
    }

    public static long parseDuration(@NonNull String path) {
        Utils.requireNonNull(path, "path == null");
        MediaPlayer mp = null;
        try {
            mp = new MediaPlayer();
            mp.setDataSource(path);
            mp.prepare();
            return mp.getDuration();
        } catch (Throwable t) {
            Logger.getDefault().e(t);
        } finally {
            if (mp != null) {
                mp.release();
            }
        }
        return -1L;
    }

    private Audios() {
        throw new AssertionError("no instance");
    }
}
