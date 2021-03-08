package x.common.component.finder;

import androidx.annotation.StringDef;

/**
 * Author: cxx
 * Date: 2020-04-14
 */
@StringDef({Module.ACCOUNT, Module.UNKNOWN})
public @interface Module {
    String ACCOUNT = "account";
    String UNKNOWN = "unknown";
}
