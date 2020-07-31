package x.common.component.core;


import x.common.component.AutoRegistrable;
import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-07-22
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("UnusedReturnValue, unused")
@Core(AppCoreImpl.class)
public interface AppCore extends AutoRegistrable<AppStateListener> {
}
