package x.common;

import android.content.Context;
import android.net.Uri;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.URI;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("x.common.test", appContext.getPackageName());
    }

    @Test
    public void testUri() {
        Uri uri = new Uri.Builder().scheme("ol").authority("object.loader").path("/drawable").appendQueryParameter("id", String.valueOf(12)).build();
        String sUri = uri.toString();
        System.out.println(sUri);
    }

    @Test
    public void generateTest() {
        String url = "https://dldir1.qq.com/weixin/mac/WeChatMac.dmg";
        Object jUri = URI.create(url).toString();
        Object aUri = Uri.parse(url).toString();
        System.out.println("jUri: " + jUri);
        System.out.println("aUri: " + aUri);
    }
}