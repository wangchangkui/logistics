package com.ruoyi.system.utils.http;

import com.ruoyi.system.utils.thread.MyThreadPool;
import okhttp3.*;
import okhttp3.Request.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author wck
 * @version 1.0.0
 * @Description TODO
 * @createTime 2022年02月08日 15:46:00
 */
public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequest.class);

    /**
     * 封装同步的Get请求
     *
     * @param url        请求url
     * @param properties 参数
     * @return Object
     * @throws ExecutionException   线程异常
     * @throws InterruptedException 线程阻塞异常
     */
    public static Object getRequest(String url, Map<String, String> properties) throws ExecutionException, InterruptedException {
        StringBuilder stringBuilder = new StringBuilder();
        properties.forEach((i, v) -> stringBuilder.append(i).append("=").append(v).append("&"));
        // 没有参数则直接输出url
        if (stringBuilder.length() >= 0) {
            stringBuilder.replace(stringBuilder.length() - 1, stringBuilder.length(), "");
            url = url + "?" + stringBuilder;
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        Call call = okHttpClient.newCall(request);
        ThreadPoolExecutor threadPool = MyThreadPool.getThreadPool();
        return threadPool.submit(() -> {
            Response execute;
            try {
                execute = call.execute();
                return Objects.requireNonNull(execute.body()).string();
            } catch (IOException e) {
               logger.error(e.getMessage());
            }
            // 给他干个默认值
            return "null";
        }).get();
    }


    /**
     * 封装一个post请求
     * @param url url地址
     * @param properties 参数
     * @param httpHeader 请求头
     * @return Object
     * @throws ExecutionException 线程异常
     * @throws InterruptedException 阻塞异常
     */
    public static Object postRequest(String url, Map<String, String> properties, HttpHeader httpHeader) throws ExecutionException, InterruptedException {
        FormBody.Builder body = new FormBody.Builder();
        properties.forEach(body::add);
        FormBody build = body.build();
        Request post = new Builder().url(url).header(httpHeader.getHeader(), httpHeader.getValue()).post(build).build();
       return okRequest(post);
    }

    /**
     * 多header封装
     * @param url url地址
     * @param properties 参数
     * @param header 请求头集合
     * @return 返回结果
     * @throws ExecutionException 线程异常
     * @throws InterruptedException 阻塞异常
     */
    public static String postRequest(String url, Map<String, String> properties, Map<String, String> header) throws ExecutionException, InterruptedException {
        FormBody.Builder body = new FormBody.Builder();
        properties.forEach(body::add);
        FormBody build = body.build();
        Builder builder = new Builder().url(url);
        header.forEach(builder::addHeader);
        Request post = builder.post(build).build();
       return okRequest(post);
    }

    /**
     * 使用json字符串 请求
     * @param url 请求地址
     * @param json 参数json
     * @param header 请求头
     * @return 结果
     * @throws ExecutionException 线程异常
     * @throws InterruptedException 阻塞异常
     */
    public static String postRequest(String url,String json,Map<String, String> header) throws ExecutionException, InterruptedException {
        RequestBody bode=RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json);
        Builder url1 = new Builder().post(bode).url(url);
        header.forEach(url1::addHeader);
        Request build = url1.build();
        return okRequest(build);
    }


    public static String okRequest(Request request) throws ExecutionException, InterruptedException {
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        ThreadPoolExecutor threadPool = MyThreadPool.getThreadPool();
        return threadPool.submit(() -> {
            try {
                Response execute = call.execute();
                return Objects.requireNonNull(execute.body()).string();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
            return "null";
        }).get();
    }
}

