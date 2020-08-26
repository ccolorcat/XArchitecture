package x.common.component.network;

import androidx.annotation.NonNull;

import java.util.Map;

/**
 * Author: cxx
 * Date: 2020-08-26
 * GitHub: https://github.com/ccolorcat
 */
public interface CommonQueriesProvider {
    @NonNull
    Map<String, String> getCommonQueries();
}
