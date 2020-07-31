package x.common.component.core;


import x.common.component.AutoRegistrable;
import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-07-21
 * GitHub: https://github.com/ccolorcat
 */
@SuppressWarnings("UnusedReturnValue, unused")
@Core(AccountCoreImpl.class)
public interface AccountCore extends AutoRegistrable<AccountStateListener> {

}
