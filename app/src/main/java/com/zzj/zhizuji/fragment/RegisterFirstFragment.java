package com.zzj.zhizuji.fragment;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzj.zhizuji.R;
import com.zzj.zhizuji.RegisterActivity;
import com.zzj.zhizuji.base.BaseFragment;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.ViewUtils;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;

/**
 * Created by shawn on 17/3/5.
 */

public class RegisterFirstFragment extends BaseFragment {
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_first;
    }

    @Override
    protected String getTitle() {
        return "注册";
    }

    @OnClick(R.id.next)
    public void next(View view){

        if(etTel.getText().toString().length() != 11){
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etVerify.getText().toString().length() != 6){
            Toast.makeText(getActivity(), "请输入正确的验证码", Toast.LENGTH_SHORT).show();
            return;
        }

//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container,ViewUtils.createFragment(RegisterSecondFragment.class)).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE).addToBackStack("second").commit();
        Network.getInstance().register(etTel.getText().toString(),etVerify.getText().toString(),String.valueOf(type))
                .subscribe(new Subscriber<Object>() {
                    @Override
                    public void onCompleted() {
                        DebugLog.e("complete");
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("error:"+e.getMessage());
                    }

                    @Override
                    public void onNext(Object o) {
                        DebugLog.e("json:"+new Gson().toJson(o));
                    }
                });
    }

    int type = 0;

    @OnClick({R.id.operator,R.id.single})
    public void typeCheck(View view){
        switch (view.getId()){
            case R.id.operator:
                type = 1;
                break;
            case R.id.single:
                type = 0;
                break;
        }
    }

    @BindView(R.id.tel)
    TextInputEditText etTel;
    @BindView(R.id.verify)
    TextInputEditText etVerify;
    @BindView(R.id.getverify)
    Button getVerify;

    String tel = "";
    @OnClick(R.id.getverify)
    public void getverify(View view){
        if(etTel.getText().toString().length() != 11){
            Toast.makeText(getActivity(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return;
        }

        Message message = handler.obtainMessage(PERIOD,60);
        handler.sendMessageDelayed(message,1000);
        getVerify.setEnabled(false);
        Network.getInstance().sendSms(etTel.getText().toString()).subscribe(new Subscriber<Object>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                DebugLog.e("error:"+e.getMessage());
            }

            @Override
            public void onNext(Object o) {
                DebugLog.e("verfy:"+new Gson().toJson(o));
            }
        });

    }


    private static final int PERIOD = 0x0001;
    private static final int FINISH = 0x0002;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PERIOD:
//                    DebugLog.e("msg:"+msg);
                    int time = (int) msg.obj;
                    time--;
                    getVerify.setText(time+"秒");
                    if (time>0)
                        sendMessageDelayed(handler.obtainMessage(PERIOD,time),1000);
                    else{
                        getVerify.setText("重新获取验证码");
                        getVerify.setEnabled(true);
                    }
                break;
            }}
    };

}
