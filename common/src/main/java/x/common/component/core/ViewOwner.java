package x.common.component.core;

import androidx.annotation.NonNull;

import x.common.component.annotation.Core;
import x.common.contract.IBase;


/**
 * Author: cxx
 * Date: 2020-07-30
 * GitHub: https://github.com/ccolorcat
 */
@Core(ViewOwnerImpl.class)
public interface ViewOwner {
    @NonNull
    <V extends IBase.View> V require(@NonNull Object tag);

    <V extends IBase.View> V get(@NonNull Object tag);

    <V extends IBase.View> void add(@NonNull Object tag, @NonNull V view);

    void remove(@NonNull Object tag);
}
