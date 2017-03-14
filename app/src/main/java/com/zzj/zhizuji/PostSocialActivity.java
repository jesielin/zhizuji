package com.zzj.zhizuji;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jaeger.ninegridimageview.NineGridImageView;
import com.jaeger.ninegridimageview.NineGridImageViewAdapter;
import com.zzj.zhizuji.base.BaseActivity;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.UIHelper;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.bean.ImageCropBean;
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
 * Created by shawn on 17/2/27.
 */

public class PostSocialActivity extends AppCompatActivity {
    @BindView(R.id.choose)
    Button btnChoose;
    @BindView(R.id.imv)
    NineGridImageView<String> imv;
    @BindView(R.id.et)
    EditText etMessage;

    private ArrayList<MediaBean> photos = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_post_social_activity);
        ButterKnife.bind(this);
        imv.setAdapter(mediaAdapter);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RxGalleryFinal
                        .with(PostSocialActivity.this)
                        .image()
                        .radio()
                        .multiple()
                        .maxSize(9)
                        .imageLoader(ImageLoaderType.GLIDE)
                        .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                            @Override
                            protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {
                                photos.clear();
                                photos.addAll(imageMultipleResultEvent.getResult());

                                imv.setImagesData(photos);
                                for (MediaBean bean : photos){
                                    DebugLog.e("result:"+bean.toString());

                                }
                            }
                        })
                        .openGallery();
            }
        });



        UIHelper.setTitle( "发表知脊圈",this);

    }

    String fileName;

    @OnClick(R.id.complete)
    public void complete(View view){
        DebugLog.e("complete");
        Intent intent = new Intent();
        intent.putExtra("MESSAGE",etMessage.getText().toString());
        intent.putParcelableArrayListExtra("PHOTOS",photos);
        setResult(1,intent);
        finish();

    }






    private NineGridImageViewAdapter<MediaBean> mediaAdapter = new NineGridImageViewAdapter<MediaBean>() {
        @Override
        protected void onDisplayImage(Context context, ImageView imageView, MediaBean mediaBean) {
            Glide.with(PostSocialActivity.this).load(mediaBean.getOriginalPath())
                    .diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.color.bg_no_photo)
                    .into(imageView);
        }

        @Override
        protected ImageView generateImageView(Context context) {
            return super.generateImageView(context);
        }

        @Override
        protected void onItemImageClick(Context context, int index, List<MediaBean> list) {
            super.onItemImageClick(context, index, list);
            fileName = list.get(index).getOriginalPath();
            Toast.makeText(PostSocialActivity.this, "posi:" + index + ",url:" + list.get(index), Toast.LENGTH_SHORT).show();
        }
    };

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




}
