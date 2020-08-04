/*
 * Copyright 2018 cxx
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package x.common.component.finder;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Author: cxx
 * Date: 2020-4-15
 */
final class ProgressInputStream extends FilterInputStream {
    static InputStream of(@NonNull File file, ProgressListener listener) throws FileNotFoundException {
        return of(new FileInputStream(file), file.length(), listener);
    }

    static InputStream of(@NonNull InputStream input, long contentLength, ProgressListener listener) {
        if (contentLength > 0L && listener != null) {
            return new ProgressInputStream(input, contentLength, listener);
        }
        return input;
    }


    private final long contentLength;
    private final ProgressListener listener;

    private ProgressInputStream(InputStream in, long contentLength, ProgressListener listener) {
        super(in);
        this.contentLength = contentLength;
        this.listener = listener;
    }

    @Override
    public int read() throws IOException {
        int nextByte = in.read();
        if (nextByte != -1) {
            updateProgress(1);
        }
        return nextByte;
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int count = in.read(b, off, len);
        if (count != -1) {
            updateProgress(count);
        }
        return count;
    }

    private long finished = 0L;
    private int lastPercent = 0;

    private void updateProgress(int readCount) {
        finished += readCount;
        int currentPercent = (int) (finished * 100L / contentLength);
        if (currentPercent > lastPercent) {
            lastPercent = currentPercent;
            listener.onChanged(finished, contentLength, currentPercent);
        }
    }
}
