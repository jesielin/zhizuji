package com.zzj.zhizuji.network;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.Result;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.zzj.zhizuji.exception.NetworkException;
import com.zzj.zhizuji.network.entity.SocialItem;
import com.zzj.zhizuji.network.entity.SocialTotal;


/**
 * Created by shawn on 2017-02-07.
 */

public class Network {

    public static final String BASE_URL = "http://101.201.155.115:8086";

    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    private HttpService httpService;

    //构造方法私有
    private Network() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(logging);
        httpClientBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);

        retrofit = new Retrofit.Builder()
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        httpService = retrofit.create(HttpService.class);
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final Network INSTANCE = new Network();
    }

    //获取单例
    public static Network getInstance() {
        return SingletonHolder.INSTANCE;
    }


    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, Observable<T>> {
        @Override
        public Observable<T> call(final HttpResult<T> tHttpResult) {
            return Observable.create(new Observable.OnSubscribe<T>() {
                @Override
                public void call(Subscriber<? super T> subscriber) {
                    if (tHttpResult != null) {
                        if ("error".equals(tHttpResult.result))
                            subscriber.onError(new NetworkException(tHttpResult.msg));
                        else if (tHttpResult.data == null) {
                            subscriber.onError(new NetworkException("子数据为空"));
                        } else {
                            subscriber.onNext(tHttpResult.data);
                        }
                    } else {
                        subscriber.onError(new NetworkException("总数据为空"));
                    }
                    subscriber.onCompleted();
                }
            });
        }
    }


    private static String sign = "123";

    //    public Observable<HttpResult<List<IndexAdvert>>> getIndexAdvert(int position){
//        return compose(httpService.getIndexAdvert(position, sign));
//    }
//
//    public Observable<HttpResult<Object>> login(String loginName, String identifyingCode){
//        return compose(httpService.login(loginName,identifyingCode,sign));
//    }
//

    public Observable<Object> register(String loginName,String verifyCode,String type){
        return compose(httpService.register(loginName,verifyCode,type,sign));
    }

    public Observable<SocialTotal> getSocialItems(String userUUID, int page, int rows) {
        return compose(httpService.getSocialItems(userUUID, page, rows, sign));
    }

    public Observable<Object> postSocial(String owner, String message) {
        return compose(httpService.sendMoment(owner, message, sign));
    }

    public Observable<Object> sendComment(String momentsID, String ownerUUID, String commenterUUID, String friendUUID, String message) {
        return compose(httpService.sendComment(momentsID,ownerUUID,commenterUUID,friendUUID,message,sign));
    }



    public Observable<Object> setUserInfo(RequestBody uuid, MultipartBody.Part avator){

        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), sign);
        return compose(httpService.setUserinfo(uuid,avator,description));
    }

    public Observable<Object> sendSms(String mobile){
        return compose(httpService.sendSms(mobile,sign));
    }



    private  <T> Observable<T> compose(Observable<HttpResult<T>> o){
        return o.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new HttpResultFunc<T>())
                .observeOn(AndroidSchedulers.mainThread())
                ;
    }



}
