package x.common;

import org.junit.Test;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import x.common.component.Hummingbird;
import x.common.component.finder.DownloadWriter;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.finder.FinderCore;
import x.common.component.finder.Module;
import x.common.component.log.Logger;
import x.common.component.schedule.BackgroundXScheduler;
import x.test.TestInitializer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final Object LOCK = new Object();
    private static final Logger LOGGER;

    static {
        TestInitializer.init();
        LOGGER = Logger.getLogger("TEST");
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testApiModel() throws IOException {
        TestApiModel model = Hummingbird.visit(TestApiModel.class);
        String result = model.search("测试").execute();
//        LOGGER.v(result);
        assertNotNull(result);
    }

    @Test
    public void testMoocApiModel() throws IOException {
        TestMoocApi model = Hummingbird.visit(TestMoocApi.class);
        String result;
//        result = model.testCourses("api", "teacher", 4, 30).execute();
//        result = model.listCourses(4, 30).execute();
        result = model.listCourses(4, 30).execute();
//        String result = model.getCourses("http://www.imooc.com/api/teacher", 4, 30).execute();
//        String result = model.getCourses(null, 4, 30).execute();
        LOGGER.v(result);
        assertNotNull(result);
    }

    @Test
    public void testThreadSafe() {
        Map<Object, Object> sets = new ConcurrentHashMap<>();
        for (int i = 0; i < 100; ++i) {
            final int non = i;
            new Thread(() -> {
                TestStoreModel result = Hummingbird.visit(TestStoreModel.class);
                sets.put(result, "");
                System.out.println(non + " visit: " + result + ", " + sets.size());
            }).start();
            new Thread(() -> {
                Object result = Hummingbird.visit(FinderCore.class);
                sets.put(result, "");
                System.out.println(non + " visit: " + result + ", " + sets.size());
            }).start();
        }
//        pause();
    }

    @Test
    public void testStoreModel() {
        List<Person> original = new ArrayList<>();
        original.add(new Person("John", 34, true));
        original.add(new Person("Tom", 3, false));
        TestStoreModel model = Hummingbird.visit(TestStoreModel.class);
        assertTrue(model.savePersons(original));
        List<Person> persons = model.loadPersons();
        assertEquals(original, persons);

        assertTrue(model.removePersons());
        assertNull(model.loadPersons());

        assertTrue(model.savePersons(original));
        List<Person> persons2 = model.loadPersons();
        assertEquals(original, persons2);

        User user = new User("Tome");
        model.saveUser(user);
        User u2 = model.readUser();
        assertEquals(user, u2);
    }

    @Test
    public void testFinderCore() {
        String url = "https://dldir1.qq.com/weixin/mac/WeChatMac.dmg";
        FinderCore core = Hummingbird.visit(FinderCore.class);
        FileOperator operator = core.requireFileOperator(Module.ALBUM, "001", Filename.fromUri(url));
        Logger.getDefault().v("path: " + operator.getUri());
        boolean result = operator.quietWrite(url, DownloadWriter.of((finished, total, percent) ->
                Logger.getDefault().v("finished: " + finished + " total: " + total + " percent: " + percent))
        );
        assertTrue(result);
    }

    @Test
    public void testIoXScheduler() {
        BackgroundXScheduler scheduler = Hummingbird.visit(BackgroundXScheduler.class);
        scheduler.scheduleWithFixedDelay(new Runnable() {
            private int count = 0;

            @Override
            public void run() {
                LOGGER.v("BackgroundXScheduler: " + (count++));
            }
        }, 0, 2, TimeUnit.SECONDS);
        pause();
    }

    @Test
    public void genericTest() {
        String url = "https://haha.cxx@dldir1.qq.com:80/weixin/mac/WeChatMac.dmg?k1=v1#ha";
        System.out.println(url);
        HttpUrl httpUrl = HttpUrl.get(url);
        System.out.println(httpUrl);
        System.out.println(httpUrl.redact());
        URI uri = httpUrl.uri();
        System.out.println(uri.getPath());
        System.out.println(uri.getAuthority());
        System.out.println(uri.getHost());
        System.out.println("-------------");

        HttpUrl hurl = httpUrl.newBuilder().username("").password("").build();
        System.out.println(hurl.toString());
        System.out.println(hurl.host());

        System.out.println("-------path---------");
//        String newPath = "/{cxx}/{user}/haha";
        String newPath = "/cxx/{user}/haha";
        String path = newPath.replaceAll("\\{((?!/).)+}", "((?!/).)+");
        System.out.println(newPath);
        System.out.println(path);

        String test = "https://dldir1.qq.com:80/cxx/userqqqqq/haha";
        URI newUri = hurl.uri().resolve(path);
        System.out.println(test);
        System.out.println(newUri);
        System.out.println(test.matches(newUri.toString()));
        System.out.println(test.matches(test));
    }

    @Test
    public void testUrlEncoder() throws UnsupportedEncodingException {
        System.out.println(URLEncoder.encode("this is", "UTF-8"));
    }

    private void pause() {
        synchronized (LOCK) {
            try {
                LOCK.wait();
            } catch (InterruptedException ignore) {
            }
        }
    }

    private void goon() {
        synchronized (LOCK) {
            LOCK.notify();
        }
    }
}