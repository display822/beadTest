package com.dreamwallet.util;

import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.BaseEntity;
import com.dreamwallet.entity.ApplyRecordBean;
import com.dreamwallet.entity.BannerEntity;
import com.dreamwallet.entity.BorrowInfoBean;
import com.dreamwallet.entity.BorrowRecordEntity;
import com.dreamwallet.entity.CommentBean;
import com.dreamwallet.entity.DetailsEntity;
import com.dreamwallet.entity.ForumDetailsEntity;
import com.dreamwallet.entity.ForumEntity;
import com.dreamwallet.entity.HelpCenterBean;
import com.dreamwallet.entity.HomeInformationEntity;
import com.dreamwallet.entity.InformationEntity;
import com.dreamwallet.entity.InformationListEntity;
import com.dreamwallet.entity.LoansDetailsEntity;
import com.dreamwallet.entity.LoansEntity;
import com.dreamwallet.entity.LoansTitleEntity;
import com.dreamwallet.entity.LoginEntity;
import com.dreamwallet.entity.MyActivityEntity;
import com.dreamwallet.entity.MyFindEntity;
import com.dreamwallet.entity.PasswordStatusBean;
import com.dreamwallet.entity.PlatFormTrackEntity;
import com.dreamwallet.entity.StarProductEntity;
import com.dreamwallet.entity.UserBaseBean;
import com.dreamwallet.entity.UserBean;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by DOY on 2017/7/21.
 */
public interface UrlService {

    String VERSION_URL = Api.getBaseUrl() + "index/findAppDetail.do";



    //获取banner
    @GET("platform/togglr1")
    Observable<BaseEntity<String>> getSwift(@Query("appVersion") String appVersion, @Query("channel") String channel);

    /**
     * 首页接口
     */
    //获取banner
    @GET("index/getBannar")
    Observable<BaseEntity<List<BannerEntity>>> getBannar();

    //获取banner
    @GET("index/getBannar")
    Observable<BaseEntity<List<BannerEntity>>> getBannar(@Query("source") String source);

    //获取明星产品
    @GET("index/getStarProduct")
    Observable<BaseEntity<List<StarProductEntity>>> getStarProduct();

    //获取借款跑马灯
    @GET("index/getBorrowRecord")
    Observable<BaseEntity<List<BorrowRecordEntity>>> getBorrowRecord();

    //获取首页资讯信息
    @GET("index/getIndexNews")
    Observable<BaseEntity<List<HomeInformationEntity>>> getIndexNews();

    //获取活动信息
    @GET("index/getActivity")
    Observable<BaseEntity<List<MyActivityEntity>>> getActivity();

    //得到资讯详情/活动详情
    @GET("index/getCmsDesc")
    Observable<BaseEntity<DetailsEntity>> getCmsDesc(@Query("cmsId") int cmsId);

    //得到产品集合
    @GET("platform/getPlatformList")
    Observable<BaseEntity<List<LoansEntity>>> getPlatformList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("sortInfoId") int sortInfoId);

    //得到产品title集合
    @GET("platform/getPlatformSortName")
    Observable<BaseEntity<List<LoansTitleEntity>>> getPlatformSortName();

    /**
     * 发现
     */

    //得到资讯
    @GET("information/getInformationList")
    Observable<BaseEntity<List<InformationEntity>>> getInformationList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    //资讯流量量
    @GET("information/cmsContentPv")
    Observable<BaseEntity<String>> informationCount(@Query("informationId") String informationId);


    //得到论坛
    @POST("post/list")
    Observable<BaseEntity<List<ForumEntity>>> getForumList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    //得到论坛全部评论
    @POST("comment/postComment")
    Observable<BaseEntity<List<CommentBean>>> getAllComment(@Query("pid") int pId, @Query("pageNum") int pageNum, @Query("pageSize") int pageSize);


    //资讯栏目列表
    @GET("information/getInformationBranchList")
    Observable<BaseEntity<List<InformationListEntity>>> getInformationBranchList(@Query("pageNum") int pageNum, @Query("pageSize") int pageSize, @Query("type") int type);

    //得到论坛帖子详情
    @POST("post/getPost")
    Observable<BaseEntity<ForumDetailsEntity>> getForumDetails(@Query("pid") int pid);

    //新增帖子
    @POST("post/add")
    Observable<BaseEntity<String>> addForum(@QueryMap Map<String, Object> map);

    //保存评论
    @POST("comment/add")
    Observable<BaseEntity<String>> addForumResult(@QueryMap Map<String, Object> map);

    //资讯banner
    @GET("information/getInformationBannar")
    Observable<BaseEntity<List<BannerEntity>>> getInformationBannar();

    //论坛banner
    @GET("information/getBbsBannar")
    Observable<BaseEntity<List<BannerEntity>>> getBbsBannar();


    //得到我的发现列表
    @GET("information/getCustomInformationVisit")
    Observable<BaseEntity<List<MyFindEntity>>> getCustomInformationVisit(@QueryMap Map<String, Object> map);

    //判断是否设置密码
    @POST("login/isSetPwd")
    Observable<BaseEntity<PasswordStatusBean>> isSetPwd(@QueryMap Map<String, Object> map);

    //获取申请借款的信息
    @POST("personal/getBorrowInfo")
    Observable<BaseEntity<BorrowInfoBean>> getBorrowInfo(@QueryMap Map<String, Object> map);

    //申请借款（保存借款信息）
    @POST("personal/saveBorrowInfo")
    Observable<BaseEntity<String>> saveBorrowInfo(@QueryMap Map<String, Object> map);

    //修改密码
    @POST("user/modifyPwd")
    Observable<BaseEntity<LoginEntity>> modifyPwd(@QueryMap Map<String, Object> map);

    //用户信息
    @POST("user/queryUser")
    Observable<BaseEntity<UserBean>> queryUser(@Query("token") String token);

    //用户信息
    @POST("user/queryUser")
    Observable<BaseEntity<String>> queryUser1(@Query("token") String token);

    //找回/重置密码
    @POST("user/resetPwd")
    Observable<BaseEntity<LoginEntity>> resetPwd(@QueryMap Map<String, Object> map);

    //我要反馈
    @POST("personal/saveCustomerFeedBack")
    Observable<BaseEntity<String>> saveCustomerFeedBack(@QueryMap Map<String, Object> map);

    //发送验证码
    @GET("sms/sendSmsEncryption")
    Observable<BaseEntity<String>> sendSmsEncryption(@QueryMap Map<String, Object> map);

    //帮助中心
    @POST("index/getHelpCenter")
    Observable<BaseEntity<List<HelpCenterBean>>> getHelpCenter(@QueryMap Map<String, Object> map);

    //密码登录
    @POST("user/login")
    Observable<BaseEntity<LoginEntity>> login(@QueryMap Map<String, Object> map);

    //验证码登录
    @POST("user/codeLogin")
    Observable<BaseEntity<LoginEntity>> codeLogin(@QueryMap Map<String, Object> map);

    //登出
    @POST("user/loginOut")
    Observable<BaseEntity<String>> loginOut(@Query("token") String token);

    //获取我的足迹
    @POST("personal/getCustomerPlatformVisitInfo")
    Observable<BaseEntity<List<PlatFormTrackEntity>>> getCustomerPlatformVisitInfo(@QueryMap Map<String, Object> map);

    //获取用户信息
    @POST("personal/saveCustomerBaseInfo")
    Observable<BaseEntity<UserBaseBean>> getCustomerBaseInfo(@QueryMap Map<String, Object> map);

    //保存用户信息
    @POST("personal/saveCustomerBaseInfo")
    Observable<BaseEntity<String>> saveCustomerBaseInfo(@QueryMap Map<String, Object> map);

    //获取借款平台详情
    @GET("platform/getPlatformDetail")
    Observable<BaseEntity<LoansDetailsEntity>> getPlatformDetail(@Query("status") String status, @Query("platformId") String platformId);

    //获取借款平台详情
    @GET("platform/getPlatformDetail")
    Observable<BaseEntity<LoansDetailsEntity>> getPlatformDetail(@Query("status") String status);

    //删除我的发现条目
    @POST("information/deleteInformationVisit")
    Observable<BaseEntity<String>> deleteInformationVisit(@QueryMap Map<String, Object> map);

    //获取申请记录
    @POST("personal/getApplyListPage")
    Observable<BaseEntity<List<ApplyRecordBean>>> getApplyRecord(@Query("token") String token,@Query("pageNum") int pageNum, @Query("pageSize") int pageSize);

    /**
     * 统计
     */
    //统计我的发现
    @POST("information/insertInformationVisit")
    Observable<BaseEntity<String>> insertInformationVisit(@Query("token") String token, @Query("visitType") int visitType, @Query("informationId") int informationId);

    //统计浏览量
    @POST("platform/visitCount")
    Observable<BaseEntity<String>> visitCount(@Query("token") String token, @QueryMap Map<String, Object> map);

    //统计产品界面
    @GET("platform/clickCount")
    Observable<BaseEntity<String>> clickCount(@Query("token") String token, @Query("platformId") String platformId);

    //首页浏览量
    @GET("platform/homePage")
    Observable<BaseEntity<String>> homePage();

    //首页浏览量
    @POST("personal/addApply")
    Observable<BaseEntity<String>> addApply(@Query("token") String token, @Query("productId") String productId);

}
