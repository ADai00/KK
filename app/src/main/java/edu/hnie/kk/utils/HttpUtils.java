package edu.hnie.kk.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 网络工具类
 */
public class HttpUtils {
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
