package x.common.component.finder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * Author: cxx
 * Date: 2020-04-15
 */
@Deprecated
public class BitmapReader implements DataReader<Bitmap> {
    @Override
    public Bitmap read(@NonNull InputStream input) throws IOException {
        // TODO: 2020/4/16 待确定最大尺寸问题后修改
        return BitmapFactory.decodeStream(input);
    }
}
