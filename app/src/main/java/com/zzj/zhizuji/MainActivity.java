package com.zzj.zhizuji;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.fragment.HomeFragment;
import com.zzj.zhizuji.fragment.MeFragment;
import com.zzj.zhizuji.fragment.MessageFragment;
import com.zzj.zhizuji.fragment.NewsFragment;
import com.zzj.zhizuji.fragment.SocialFragment;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.SharedPreferenceUtils;
import com.zzj.zhizuji.util.ViewUtils;

public class MainActivity extends AppCompatActivity {

    private FragmentManager mFragmentManager;
    private BaseFragment mCurrentFragment;
    @BindView(R.id.bottom_nav)
    BottomNavigationView mBottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);


//        EMClient.getInstance().login(SharedPreferenceUtils.getValue("UUID"),"123456",new EMCallBack() {//回调
//            @Override
//            public void onSuccess() {
//                EMClient.getInstance().groupManager().loadAllGroups();
//                EMClient.getInstance().chatManager().loadAllConversations();
//                DebugLog.d( "登录聊天服务器成功！");
//            }
//
//            @Override
//            public void onProgress(int progress, String status) {
//
//            }
//
//            @Override
//            public void onError(int code, String message) {
//                DebugLog.d( "登录聊天服务器失败！");
//            }
//        });


        mFragmentManager = getSupportFragmentManager();

        DebugLog.e("share:"+SharedPreferenceUtils.getValue("NICKNAME"));


        mCurrentFragment = (BaseFragment) mFragmentManager.findFragmentById(R.id.container);
        if (mCurrentFragment == null) {
            mCurrentFragment = ViewUtils.createFragment(HomeFragment.class);
            mFragmentManager.beginTransaction().add(R.id.container, mCurrentFragment).commit();
        }
        final FragmentTransaction trans = mFragmentManager.beginTransaction();
        if (null != savedInstanceState) {
            List<Fragment> fragments = mFragmentManager.getFragments();
            for (int i = 0; i < fragments.size(); i++) {
                trans.hide(fragments.get(i));
            }
        }
        trans.show(mCurrentFragment).commitAllowingStateLoss();
        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Class<?> clazz = null;
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        clazz = HomeFragment.class;
                        break;
                    case R.id.social:
                        clazz = SocialFragment.class;

                        break;
                    case R.id.news:
                        clazz = NewsFragment.class;
                        break;
                    case R.id.message:
                        clazz = MessageFragment.class;
                        break;
                    case R.id.me:
                        clazz = MeFragment.class;
                        break;
                }

                if (item.getItemId() == R.id.social || item.getItemId() == R.id.message || item.getItemId() == R.id.me){
                    if (!SharedPreferenceUtils.isLogin()){
                        startActivity(new Intent(MainActivity.this,LoginActivity.class));
                        return false;
                    }

                }

                switchFragment(clazz);
                return true;
            }
        });
    }
    private void switchFragment(Class<?> clazz) {
        if (clazz == null) return;

        BaseFragment to = ViewUtils.createFragment(clazz);
        if (to.isAdded()) {
            mFragmentManager.beginTransaction().hide(mCurrentFragment).show(to).commit();
        } else {
            to.setUserVisibleHint(true);
            mFragmentManager.beginTransaction().hide(mCurrentFragment).add(R.id.container, to).commit();
        }
        mCurrentFragment = to;
    }
}
