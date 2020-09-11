package x.common.component;

import androidx.annotation.NonNull;

import x.common.IClient;

/**
 * Author: cxx
 * Date: 2020-09-11
 * GitHub: https://github.com/ccolorcat
 */
public interface ModuleInitializer {
    void initialize(@NonNull IClient client);
}
