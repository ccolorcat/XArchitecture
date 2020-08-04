package x.common.component.finder;

import androidx.annotation.NonNull;

/**
 * Author: cxx
 * Date: 2020-04-15
 */
public interface DirOperator extends DataOperator {
    @NonNull
    FileOperator getFileOperator(String filename);

    @NonNull
    FileOperator getFileOperator(@NonNull Filename filename);

    @NonNull
    FileOperator getFileOperator(@NonNull String name, @NonNull String type);

    long maxSize();
}
