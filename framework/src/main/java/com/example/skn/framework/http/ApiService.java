package com.example.skn.framework.http;

import com.example.skn.framework.update.UpdateEntity;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by DOY on 2017/8/30.
 */
public interface ApiService {

    @POST()
    Observable<UpdateEntity> update(@Url String url);

    @Streaming()//下载大文件
    @GET()
    Observable<ResponseBody> downLoadFile(@Header("RANGE") String range, @Url String url);

    @Streaming()//下载大文件
    @GET()
    Observable<ResponseBody> downLoadFile(@Url String url);
}
