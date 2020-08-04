package x.common.component.finder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;

import x.common.IClient;
import x.common.component.Hummingbird;
import x.common.component.schedule.IoXScheduler;
import x.common.component.schedule.XScheduler;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
final class FinderCoreImpl implements FinderCore {
    private static final long MAX_SIZE_PER_DIR = 500 * 1024 * 1024;

    private final IClient client;
    private final XScheduler scheduler;
    private Finder finder;
    private volatile boolean accountChanged = false;

    private FinderCoreImpl(@NonNull IClient client) {
        this.client = client;
        this.scheduler = Hummingbird.visit(IoXScheduler.class);
        this.finder = new Finder(scheduler, getRootDir(), MAX_SIZE_PER_DIR);
    }

    // TODO: 2020/8/4 需考虑用户切换账户登录等情况，故需要每次都对 Finder 的路径进行检查，另需要添加 uid, oid 作为子路径。
    private File getRootDir() {
        return Finder.getPath(client.getCacheDir(), "finder");
    }

    private void update() {
        if (accountChanged) {
            synchronized (this) {
                if (accountChanged) {
                    this.finder = new Finder(scheduler, getRootDir(), MAX_SIZE_PER_DIR);
                    accountChanged = false;
                }
            }
        }
    }

    @Nullable
    @Override
    public DirOperator getDirOperator(@Module String moduleName, String moduleId, @NonNull Filetype filetype) {
        String mn = Utils.nullElse(moduleName, Module.UNKNOWN);
        String mi = Utils.nullElse(moduleId, Module.UNKNOWN);
        update();
        return finder.getDirOperator(mn, mi, filetype.name());
    }
}
