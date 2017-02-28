package com.zzj.zhizuji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.util.DebugLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Subscriber;

/**
 * Created by shawn on 17/2/27.
 */

public class PostSocialActivity extends AppCompatActivity {
    @BindView(R.id.choose)
    Button btnChoose;
    @BindView(R.id.imv)
    NineGridImageView<String> imv;

    private ArrayList<String> photoPaths = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_social_activity);
        ButterKnife.bind(this);

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FilePickerBuilder.getInstance().setMaxCount(9)
                        .setSelectedFiles(photoPaths)
                        .pickPhoto(PostSocialActivity.this);
            }
        });

        imv.setAdapter(mAdapter);


    }

    String fileName;

    public void up(View view) {
        Network network = Network.getInstance();
        File file = new File(fileName);
        // 创建 RequestBody，用于封装构建RequestBody
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用image
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("headSculpture", file.getName(), requestFile);

        // 添加描述
        String descriptionString = "11";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);
        network.setUserInfo(description, body)
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("message:" + e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {

                    }
                });
    }


    private NineGridImageViewAdapter<String> mAdapter = new NineGridImageViewAdapter<String>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, String s) {
            Glide.with(PostSocialActivity.this).load(s)
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo)
                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<String> list) {
            super.onItemImageClick(context, index, list);
            fileName = list.get(index);
            Toast.makeText(PostSocialActivity.this, "posi:" + index + ",url:" + list.get(index), Toast.LENGTH_SHORT).show();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    imv.setImagesData(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS));

                }
                break;


        }
    }


}
