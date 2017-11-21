package com.example.skn.framework.http;

import android.text.TextUtils;
import android.util.Log;

import com.example.skn.framework.util.SpUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Api {
//    private static ApiService apiService;

    private static String baseUrl;


    public static void setBaseUrl(String url) {
        baseUrl = url;
    }

    public  static String getBaseUrl() {
        return baseUrl;
    }

    public static <T> T getDefault(Class<T> tClass) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request.Builder builder = request.newBuilder();
                    String cookie = SpUtil.getStringData("cookie");
                    if (!TextUtils.isEmpty(cookie)) {
                        builder.addHeader("cookie", cookie);
                    }
                    return chain.proceed(builder.build());
                })
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    okhttp3.Response response = chain.proceed(request);
                    if (!response.headers("set-cookie").isEmpty()) {
                        String cookie = saveCookie(response.headers("set-cookie"));
                        SpUtil.setData("cookie", cookie);
                    }
                    return response;
                })
                .connectTimeout(15, TimeUnit.SECONDS).build();
        return new Retrofit.Builder().baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build().create(tClass);


    }

    public static <T> Observable.Transformer<BaseEntity<T>, T> handlerResult() {
        return baseEntityObservable -> baseEntityObservable.flatMap(tBaseEntity -> {
            if (tBaseEntity != null) {
                if (tBaseEntity.getCode().equals("000")) {
                    return Observable.create(new Observable.OnSubscribe<T>() {
                        @Override
                        public void call(Subscriber<? super T> subscriber) {
                            try {
                                subscriber.onNext(tBaseEntity.getData());
                                Log.e("success", "---------->" + tBaseEntity.getMesg());
                                subscriber.onCompleted();
                            } catch (Exception e) {
                                subscriber.onError(e);
                            }
                        }
                    });
                } else {
                    return Observable.error(new ApiException(tBaseEntity.getMesg(), tBaseEntity.getCode()));
                }
            } else {
                return Observable.error(new ApiException("服务器数据异常"));
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 对于cookie的持久化操作
     */
    private static String saveCookie(List<String> newData) {
        String cookie = SpUtil.getStringData("cookie");
        List<String> oldData = Arrays.asList(cookie.split(";"));
        Map<String, String> oldMap = new HashMap<>();
        for (String item : oldData) {
            String[] strings = item.split(";");
            String str = strings[0];
            if (str.contains("=")) {
                String key = str.substring(0, str.indexOf("=") + 1);
                String value = str.substring(str.indexOf("=") + 1, str.length());
                oldMap.put(key, value + ";");
            }
        }
        Map<String, String> newMap = new HashMap<>();
        for (String item : newData) {
            String[] strings = item.split(";");
            String str = strings[0];
            if (str.contains("=")) {
                String key = str.substring(0, str.indexOf("=") + 1);
                String value = str.substring(str.indexOf("=") + 1, str.length());
                newMap.put(key, value + ";");
            }
        }
        oldMap.putAll(newMap);
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> item : oldMap.entrySet()) {
            sb.append(item.getKey()).append(item.getValue());
        }
        return sb.toString();
    }

}
