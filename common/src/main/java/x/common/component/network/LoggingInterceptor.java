package x.common.component.network;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import x.common.component.log.Logger;
import x.common.util.Utils;

/**
 * Author: cxx
 * Date: 2020-08-21
 * GitHub: https://github.com/ccolorcat
 */
public class LoggingInterceptor implements Interceptor {
    private static final String LINE = buildString(94, '-');
    private static final String HALF_LINE = buildString(38, '-');
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final Logger logger = Logger.getLogger("XOkHttp");
    private final Charset charsetIfAbsent;
    private final boolean deUnicode;


    public LoggingInterceptor() {
        this(StandardCharsets.UTF_8, false);
    }

    public LoggingInterceptor(boolean deUnicode) {
        this(StandardCharsets.UTF_8, deUnicode);
    }

    public LoggingInterceptor(@NonNull Charset charsetIfAbsent, boolean deUnicode) {
        this.charsetIfAbsent = Utils.requireNonNull(charsetIfAbsent, "charsetIfAbsent == null");
        this.deUnicode = deUnicode;
    }


    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        final Request request = chain.request();
        final long start = System.currentTimeMillis();
        Response response = chain.proceed(request);
        final long elapse = System.currentTimeMillis() - start;

        final StringBuilder builder = new StringBuilder()
                .append(" \n").append(HALF_LINE)
                .append(' ').append(request.method())
                .append(" (").append(elapse).append(" millis) ")
                .append(HALF_LINE).append('>')
                .append("\nrequest url --> ").append(request.url());
        appendHeaders(builder, "request header --> ", request.headers());
        RequestBody requestBody;
        MediaType requestType;
        if (HttpUtils.needBody(request.method())
                && (requestBody = request.body()) != null
                && (requestType = requestBody.contentType()) != null
                && isText(requestType)) {
            Charset charset = requestType.charset(charsetIfAbsent);
            assert charset != null;
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);
            builder.append("\nrequest body --> \n").append(buffer.readString(charset));
        }

        builder.append("\n\nresponse <-- ").append(response.code()).append("--").append(response.message());
        appendHeaders(builder, "response header <-- ", response.headers());
        ResponseBody responseBody = response.body();
        MediaType responseType;
        if (responseBody != null && (responseType = responseBody.contentType()) != null && isText(responseType)) {
            String rawBody = responseBody.string();
            builder.append("\nresponse body <-- \n").append(formatBody(deUnicode ? decode(rawBody) : rawBody, responseType));
            ResponseBody newBody = ResponseBody.create(rawBody, responseType);
            response = response.newBuilder()
                    .header("Content-Length", Long.toString(newBody.contentLength()))
                    .body(newBody)
                    .build();
        }
        builder.append("\n<").append(LINE);

        logger.d(builder.toString());

        return response;
    }

    protected boolean isText(@NonNull MediaType contentType) {
        return contentType.toString().toLowerCase().matches(".*(charset|text|html|htm|json|urlencoded)+.*");
    }

    protected String formatBody(String content, MediaType contentType) {
        try {
            return gson.toJson(JsonParser.parseString(content));
        } catch (Throwable ignore) {
            return content;
        }
    }


    private static void appendHeaders(StringBuilder builder, String prefix, Headers headers) {
        for (int i = 0, size = headers.size(); i < size; ++i) {
            builder.append('\n').append(prefix)
                    .append(headers.name(i)).append('=').append(headers.value(i));
        }
    }

    @NonNull
    private static String buildString(int count, char c) {
        StringBuilder builder = new StringBuilder(count);
        for (int i = 0; i < count; ++i) {
            builder.append(c);
        }
        return builder.toString();
    }

    private static String decode(String unicode) {
        StringBuilder builder = new StringBuilder(unicode.length());
        Matcher matcher = Pattern.compile("\\\\u[0-9a-fA-F]{4}").matcher(unicode);
        int last = 0;
        for (int start, end = 0; matcher.find(end); last = end) {
            start = matcher.start();
            end = matcher.end();
            builder.append(unicode.substring(last, start))
                    .append((char) Integer.parseInt(unicode.substring(start + 2, end), 16));
        }
        return builder.append(unicode.substring(last)).toString();
    }
}
