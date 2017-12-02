package com.comtempwallet.http;

import com.example.skn.framework.http.BaseEntity;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by c on 2017/7/21.
 *
 */
public interface UrlService {

    //用户信息
    @POST("user/queryUser")
    Observable<BaseEntity<String>> queryUser1(@Query("token") String token);

}
