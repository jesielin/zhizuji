package com.zzj.zhizuji;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by shawn on 17/3/19.
 */

public class TalkActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talk);

        String uuid = getIntent().getStringExtra("UUID");
        //new出EaseChatFragment或其子类的实例
//        EaseChatFragment chatFragment = new EaseChatFragment();
//        传入参数
//        Bundle args = new Bundle();
//        args.putInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
//        args.putString(EaseConstant.EXTRA_USER_ID, "zw123");
//        chatFragment.setArguments(args);
//        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }
}
