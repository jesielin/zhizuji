package com.zzj.zhizuji.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zzj.zhizuji.PostSocialActivity;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.SetInfoResult;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.GlideCircleTransform;
import com.zzj.zhizuji.util.SharedPreferenceUtils;
import com.zzj.zhizuji.util.UIHelper;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.MediaBean;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.BaseResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageRadioResultEvent;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by shawn on 17/3/5.
 */

public class RegisterSecondFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_second;
    }

    @Override
    protected String getTitle() {
        return "基本信息";
    }

    private static final String MALE = "1";
    private static final String FEMALE = "0";

    private String gender = MALE;
    @OnClick({R.id.male,R.id.female})
    public void switchGender(View view){
        switch (view.getId()){
            case R.id.male:
                gender = MALE;
                break;
            case R.id.female:
                gender = FEMALE;
                break;
        }
    }

    @BindView(R.id.name)
    TextInputEditText etName;
    @OnClick(R.id.complete)
    public void complete(View view){

        final ProgressDialog progressDialog = UIHelper.showProgressDialog(getActivity(),"正在注册...");

        Network network = Network.getInstance();
        MultipartBody.Part avatorPart = null;
        if (!TextUtils.isEmpty(avatorPath)) {
            File file = new File(avatorPath);
            // 创建 RequestBody，用于封装构建RequestBody
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part  和后端约定好Key，这里的partName是用image
            avatorPart=
                    MultipartBody.Part.createFormData("headSculpture", file.getName(), requestFile);

        }
        //添加UUID
        String uuidText = SharedPreferenceUtils.getValue("UUID");
        RequestBody uuid =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), uuidText);
        // 添加nickname
        String nickNameText = etName.getText().toString();
        RequestBody nickName =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), nickNameText);
        // 添加sex
        String sexText = gender;
        RequestBody sex =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), sexText);
        network.setUserInfo(uuid,nickName, sex,avatorPart)
                .subscribe(new Subscriber<SetInfoResult>() {
                    @Override
                    public void onCompleted() {
                        progressDialog.cancel();
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("message:" + e.getMessage());
                        progressDialog.cancel();
                    }

                    @Override
                    public void onNext(SetInfoResult setInfoResult) {
                        SharedPreferenceUtils.setStringValue("AVATOR",setInfoResult.headSculpture);
                        SharedPreferenceUtils.setStringValue("NICKNAME",setInfoResult.nickName);
                        progressDialog.setMessage("注册成功");
                        getActivity().finish();
                    }
                });
//                .subscribe(new Subscriber<Object>() {
//                    @Override
//                    public void onCompleted() {
//                        progressDialog.cancel();
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        DebugLog.e("message:" + e.getMessage());
//                        progressDialog.cancel();
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                    }
//                });


    }

    @BindView(R.id.avator)
    ImageView ivAvator;

    private String avatorPath = "";
    @OnClick(R.id.avator)
    public void setAvator(View view){
        RxGalleryFinal
                .with(getActivity())
                .image()
                .radio()

                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<ImageRadioResultEvent>() {
                    @Override
                    protected void onEvent(ImageRadioResultEvent imageRadioResultEvent) throws Exception {
//                        Toast.makeText(getBaseContext(), imageRadioResultEvent.getResult().getOriginalPath(), Toast.LENGTH_SHORT).show();
                        avatorPath = imageRadioResultEvent.getResult().getOriginalPath();
                        Glide.with(getActivity()).load(avatorPath)
                            .diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(getActivity()))
                            .into(ivAvator);

                    }
                })
                .openGallery();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

//        Glide.with(getActivity()).load(R.drawable.avator_placeholder)
//                .diskCacheStrategy(DiskCacheStrategy.ALL).transform(new GlideCircleTransform(getActivity()))
//                .into(ivAvator);

        return mContentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
