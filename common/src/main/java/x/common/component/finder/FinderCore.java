package x.common.component.finder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import x.common.component.annotation.Core;

/**
 * Author: cxx
 * Date: 2020-08-04
 * GitHub: https://github.com/ccolorcat
 */
@Core(FinderCoreImpl.class)
public interface FinderCore {
    default boolean exists(@Module String moduleName, String moduleId, Filename filename) {
        if (filename == null) return false;
        FileOperator operator = getFileOperator(moduleName, moduleId, filename);
        return operator != null && operator.exists();
    }

    /**
     * 举例，文件完整名称："test.png"
     *
     * @param name 文件名称，不包含扩展名，"test"
     * @param type 文件扩展名，"png"，不包含 "."
     */
    @NonNull
    default FileOperator requireFileOperator(@Module String moduleName, String moduleId, String name, String type) {
        return requireFileOperator(moduleName, moduleId, Filename.of(name, type));
    }

    /**
     * 举例，文件完整名称："test.png"
     *
     * @param name 文件名称，不包含扩展名，"test"
     * @param type 文件扩展名，"png"，不包含 "."
     */
    @Nullable
    default FileOperator getFileOperator(@Module String moduleName, String moduleId, String name, String type) {
        return getFileOperator(moduleName, moduleId, Filename.of(name, type));
    }

    /**
     * @param type 文件扩展名，如 "png", "gif" 等，不包含 "."
     */
    @NonNull
    default DirOperator requireDirOperator(@Module String moduleName, String moduleId, String type) {
        return requireDirOperator(moduleName, moduleId, Filetype.byType(type));
    }

    /**
     * @param type 文件扩展名，如 "png", "gif" 等，不包含 "."
     */
    @Nullable
    default DirOperator getDirOperator(@Module String moduleName, String moduleId, String type) {
        return getDirOperator(moduleName, moduleId, Filetype.byType(type));
    }

    @NonNull
    default FileOperator requireFileOperator(@Module String moduleName, String moduleId, @NonNull Filename filename) {
        return requireDirOperator(moduleName, moduleId, filename.toFiletype()).getFileOperator(filename);
    }

    @Nullable
    default FileOperator getFileOperator(@Module String moduleName, String moduleId, @NonNull Filename filename) {
        DirOperator operator = getDirOperator(moduleName, moduleId, filename.toFiletype());
        return operator == null ? null : operator.getFileOperator(filename);
    }

    @NonNull
    default DirOperator requireDirOperator(@Module String moduleName, String moduleId, @NonNull Filename filename) {
        return requireDirOperator(moduleName, moduleId, filename.toFiletype());
    }

    @Nullable
    default DirOperator getDirOperator(@Module String moduleName, String moduleId, @NonNull Filename filename) {
        return getDirOperator(moduleName, moduleId, filename.toFiletype());
    }

    @NonNull
    default DirOperator requireDirOperator(@Module String moduleName, String moduleId, @NonNull Filetype filetype) {
        DirOperator operator = getDirOperator(moduleName, moduleId, filetype);
        if (operator == null) {
            throw new NullPointerException("getDirOperator failed, moduleName=" + moduleName
                    + ", moduleId=" + moduleId
                    + ", filetype=" + filetype
            );
        }
        return operator;
    }

    @Nullable
    DirOperator getDirOperator(@Module String moduleName, String moduleId, @NonNull Filetype filetype);
}
