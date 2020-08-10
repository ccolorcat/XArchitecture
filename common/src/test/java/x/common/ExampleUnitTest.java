package x.common;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import x.common.component.Hummingbird;
import x.common.component.finder.DownloadWriter;
import x.common.component.finder.FileOperator;
import x.common.component.finder.Filename;
import x.common.component.finder.FinderCore;
import x.common.component.finder.Module;
import x.common.component.log.Logger;
import x.common.component.schedule.BackgroundXScheduler;
import x.common.test.TestManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
        TestManager.init();
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
        LOGGER.v(result);
        assertNotNull(result);
    }

    @Test
    public void testStoreModel() {
        List<Person> original = new ArrayList<>();
        original.add(new Person("John", 34, true));
        original.add(new Person("Tom", 3, true));
        TestStoreModel model = Hummingbird.visit(TestStoreModel.class);
        assertTrue(model.savePersons(original));
        List<Person> persons = model.loadPersons();
        assertEquals(original, persons);

        assertTrue(model.savePersons(original));
        List<Person> persons2 = model.loadPersons();
        assertEquals(original, persons2);
    }

    @Test
    public void testFinderCore() {
        String url = "https://dldir1.qq.com/weixin/mac/WeChatMac.dmg";
        FinderCore core = Hummingbird.visit(FinderCore.class);
        FileOperator operator = core.requireFileOperator(Module.ALBUM, "001", Filename.fromUri(url));
        Logger.getDefault().v("path: " + operator.getUri());
        boolean result = operator.quietWrite(url, DownloadWriter.of((finished, total, percent) ->
                Logger.getDefault().v("finished: %d, total: %d, percent: %d", finished, total, percent))
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
                LOGGER.v("IoXScheduler: " + (count++));
            }
        }, 0, 2, TimeUnit.SECONDS);
        pause();
    }

    @Test
    public void genericTest() {

    }

    private void pause() {
        synchronized (LOCK) {
            try {
                LOCK.wait();
            } catch (InterruptedException ignore) {
            }
        }
    }
}