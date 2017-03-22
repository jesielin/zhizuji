package com.zzj.zhizuji;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zzj.zhizuji.network.Network;
import com.zzj.zhizuji.network.entity.LoginResult;
import com.zzj.zhizuji.util.DebugLog;
import com.zzj.zhizuji.util.SharedPreferenceUtils;
import com.zzj.zhizuji.util.UIHelper;
import com.zzj.zhizuji.util.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit2.http.Part;
import rx.Subscriber;

/**
 * Created by shawn on 17/3/5.
 */

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        UIHelper.setTitle("登录",this);


    }
    @BindView(R.id.name)
    TextInputEditText etTel;
    @BindView(R.id.verify)
    TextInputEditText etVerify;
    @BindView(R.id.getverify)
    Button getVerify;
    @BindView(R.id.login)
    Button btnLogin;
    @OnClick(R.id.login)
    public void login(View view){
        //TODO:
//        if(etTel.getText().toString().length() != 11){
//            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (etVerify.getText().toString().length() != 6){
//            Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
//            return;
//        }
        final ProgressDialog progressDialog = UIHelper.showProgressDialog(this, "正在登录...");
        btnLogin.setEnabled(false);
        Network.getInstance().login(etTel.getText().toString(),etVerify.getText().toString())
                .subscribe(new Subscriber<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        btnLogin.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        DebugLog.e("error:"+e.getMessage());
                        progressDialog.cancel();
                        btnLogin.setEnabled(true);
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        if (loginResult != null) {
                            SharedPreferenceUtils.setLoginLogin(loginResult.uuid,loginResult.loginName,loginResult.nickName,loginResult.headSculpture);
                        }
                        progressDialog.cancel();
                        btnLogin.setEnabled(true);
                        finish();
                    }
                });
    }

    @OnClick(R.id.getverify)
    public void getverify(View view){
        //TODO:
//        if(etTel.getText().toString().length() != 11){
//            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
//            return;
//        }

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

    @Override
    public void onStop() {
        super.onStop();
        handler.removeMessages(PERIOD);

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
                case FINISH:
                    getVerify.setText("获取验证码");
                    getVerify.setEnabled(true);
                    break;
            }}
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreferenceUtils.isLogin())
            finish();
    }



    @OnClick(R.id.register)
    public void register(View view){
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }
}
