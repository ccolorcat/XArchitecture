package x.common.component.network;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
final class Urls {
    static String getUrlTrunk(@NonNull String url) {
        String result = url.replaceAll("//(.)+@", "//");
        int index = result.indexOf('?');
        if (index == -1) index = result.indexOf('#');
        if (index != -1) result = result.substring(0, index);
        return result;
    }

    private Urls() {
        throw new AssertionError("no instance");
    }
}
