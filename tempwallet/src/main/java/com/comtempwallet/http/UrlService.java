package com.comtempwallet.http;

import com.comtempwallet.entity.BannerEntity;
import com.comtempwallet.entity.BorrowRecordEntity;
import com.example.skn.framework.http.BaseEntity;

import java.util.List;

import retrofit2.http.GET;
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

    //获取banner
    @GET("index/getBannar")
    Observable<BaseEntity<List<BannerEntity>>> getBannar();

    //获取借款跑马灯
    @GET("index/getBorrowRecord")
    Observable<BaseEntity<List<BorrowRecordEntity>>> getBorrowRecord();

}
