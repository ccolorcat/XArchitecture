package x.common.component.finder;

import androidx.annotation.StringDef;

/**
 * Author: cxx
 * Date: 2020-04-14
 */
@StringDef({Module.CHAT, Module.ALBUM, Module.QUICK_SERVICE, Module.UNKNOWN})
public @interface Module {
    String CHAT = "chat";
    String ALBUM = "album";
    String QUICK_SERVICE = "quickService";
    String UNKNOWN = "unknown";
}
