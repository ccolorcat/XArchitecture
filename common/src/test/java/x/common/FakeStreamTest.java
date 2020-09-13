package x.common;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import x.common.util.function.Func0;
import x.common.util.stream.FakeStream;

/**
 * Author: cxx
 * Date: 2020-09-11
 * GitHub: https://github.com/ccolorcat
 */
public class FakeStreamTest {

    @Test
    public void testCompute() {
        Object result = FakeStream.of("this", "is", "a", "test")
                .map(String::length)
                .peek(System.out::println)
                .compute(Integer::sum);
        System.out.println(result);
    }

    @Test
    public void testConcat() {
        FakeStream.of("this", "is", "a", "test")
                .concat(FakeStream.of("hello", "world"))
                .forEach(System.out::println);
    }

    @Test
    public void testMerge() {
        FakeStream.of("this", "is", "a", "test")
                .sort(String::compareTo)
                .merge(Arrays.asList("abc", "hello", "world"), String::compareTo)
                .forEach(System.out::println);
    }

    @Test
    public void testZip() {
        FakeStream.of("this", "is", "a", "test")
//                .zip(Arrays.asList(12, 90, 3, 56, 19, 31), (s, integer) -> s + ": " + integer)
                .zip(Arrays.asList(12, 90), (s, integer) -> s + ": " + integer)
                .forEach(System.out::println);
    }

    @Test
    public void testGroup() {
        Map<Character, List<String>> result = FakeStream.of("this", "is", "apple", "test", "abc", "the", "i")
                .filter(s -> s.length() > 1)
                .group(s -> s.charAt(0));
        System.out.println(result);
    }

    @Test
    public void testMax() {
        String max = FakeStream.of("this", "is", "apple", "test", "abc", "the", "i")
                .max(new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.length() - o2.length();
                    }
                });
        System.out.println(max);
    }

    @Test
    public void testTakeCount() {
        List<String> result = FakeStream.of("this", "is", "apple", "test", "abc", "the", "i")
                .take(3);
        System.out.println(result);
    }

    @Test
    public void testTimer() {
        FakeStream.timer(TimeUnit.MILLISECONDS, 2000L, new Func0<Integer>() {
            private int number = 0;

            @Override
            public Integer apply() {
                return number++;
            }
        }).forEach(System.out::println);
    }

    @Test
    public void testDownload() throws Throwable {
        File save = new File(System.getProperty("user.home"), "WeChat.dmg");
        OutputStream output = new FileOutputStream(save);
        FakeStream.just("https://dldir1.qq.com/weixin/mac/WeChatMac.dmg")
                .map(url -> new URL(url).openConnection().getInputStream())
                .flatMap(FakeStream::from)
                .onEnd(() -> {
                    try {
                        output.flush();
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })
                .forEach(output::write);
    }
}
