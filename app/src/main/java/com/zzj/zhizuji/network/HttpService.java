package com.zzj.zhizuji.network;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;
import com.zzj.zhizuji.network.entity.IndexAdvert;
import com.zzj.zhizuji.network.entity.Notice;
import com.zzj.zhizuji.network.entity.SocialItem;
import com.zzj.zhizuji.network.entity.SocialTotal;
import com.zzj.zhizuji.network.entity.Tech;

/**
 * Created by shawn on 2017-02-07.
 */

public interface HttpService {

    /**
     * 首页广告
     * @param position
     * @param sign
     * @return
     */
    @GET("/getIndexAdvert")
    Observable<HttpResult<List<IndexAdvert>>> getIndexAdvert(@Query("position") int position, @Query("sign") String sign);

    /**
     * 登录
     * @param loginName
     * @param identifyingCode
     * @param sign
     * @return
     */
    @GET("/login")
    Observable<HttpResult<Object>> login(@Query("loginName") String loginName, @Query("identifyingCode") String identifyingCode, @Query("sign") String sign);

    /**
     * 注册
     * @param loginName
     * @param identifyingCode
     * @param regType 1 技师 0 用户
     * @param sign
     * @return
     */
    @GET("/register")
    Observable<HttpResult<Object>> register(@Query("loginName") String loginName, @Query("identifyingCode") String identifyingCode, @Query("regType") String regType, @Query("sign") String sign);

    /**
     * 首页动态
     * @param currentDate 当前日期 2017-02-21
     * @param sign
     * @return
     */
    Observable<HttpResult<List<Notice>>> getNotice(@Query("currentDate") String currentDate, @Query("sign") String sign);

    /**
     * 首页推荐技师
     * @param size 推荐人数控制
     * @param sign
     * @return
     */
    Observable<HttpResult<List<Tech>>> getRecommendTechs(@Query("size") int size, @Query("sign") String sign);

    /**
     * 首页搜索技师
     * @param currentPage 当前页码 初始1
     * @param size 查询人数控制
     * @param techName 待搜索的技师昵称名称（模糊搜索）
     * @param sign
     * @return
     */
    Observable<HttpResult<List<Tech>>> searchTechs(@Query("currentPage") int currentPage, @Query("size") int size, @Query("techName") String techName, @Query("sign") String sign);

    /**
     * 更新用户信息
     *  uuid 用户唯一标示
     *  nickName 用户昵称
     *  status 用户状态  0未启用 1启用
     *  userType 用户类型 1技师 2普通用户
     *  level 用户等级
     *  isRecommend 是否推荐技师  1推荐
     *  summary 简介
     *  headSculpture 头像
     *  sex 性别  1男 0女
     * @return
     */
    @GET("/setUserinfo")
    Observable<HttpResult<Object>> setUserinfo(
            @QueryMap Map<String, String> options
    );


    /**
     * 查看朋友圈列表
     * @param userUUID
     * @param page
     * @param rows
     * @param sign
     * @return
     */
    @GET("/getAllMoment")
    Observable<HttpResult<SocialTotal>> getSocialItems(@Query("userUUID") String userUUID, @Query("page") int page, @Query("rows") int rows, @Query("sign") String sign);
}
