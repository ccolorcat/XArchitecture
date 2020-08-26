package x.common;

import android.content.Context;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import x.common.component.Hummingbird;
import x.common.component.loader.DownloadListener;
import x.common.component.loader.ObjectLoader;
import x.common.component.log.Logger;
import x.common.util.StoreReader;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

//    {
//        XClient client = new XClient();
//        client.onCreate();
//    }

    public Context getContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("x.common.test", appContext.getPackageName());
    }

    @Test
    public void testStoreReader() throws Throwable {
        JSONObject object = new JSONObject();
        object.put("size", "123");
        StoreReader reader = StoreReader.fromJson(null);
        assertEquals(reader.getInt("size", 0), 123);
    }

    @Test
    public void testDownloader() {
        String url = "https://dldir1.qq.com/weixin/mac/WeChatMac.dmg";
        ObjectLoader.with(Hummingbird.getClient().asAppClient().getApplication())
                .load(url)
                .asDownloader()
                .fetch(new DownloadListener() {
                    @Override
                    public void onProgress(long finished, long total, int percent) {
                        Logger.getDefault().v("onProgress: %d, %d, %d", finished, total, percent);
                    }
                });
    }

    @Test
    public void generateTest() {

    }
}