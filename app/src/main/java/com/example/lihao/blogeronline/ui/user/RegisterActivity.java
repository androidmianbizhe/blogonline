package com.example.lihao.blogeronline.ui.user;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.app.AppManager;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.bean.LoginInfo;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.ui.MainActivity;
import com.example.lihao.blogeronline.utils.MD5Utils;
import com.example.lihao.blogeronline.utils.MyTextUtils;
import com.example.lihao.blogeronline.utils.RequestUtils;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.example.lihao.blogeronline.utils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * Created by lihao on 17-12-10.
 */

public class RegisterActivity extends BaseActivity {


    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.input_cell)
    EditText inputCell;
    @BindView(R.id.et_check_num)
    EditText etCheckNum;
    @BindView(R.id.btn_get_check_num)
    Button btnGetCheckNum;
    @BindView(R.id.btn_sure)
    Button btnSure;

    //倒计时器

    MyCountDownTimer myCountDownTimer = new MyCountDownTimer(60000,1000);//时长 时间间隔
    private Intent intent;

    @Override
    protected void doBeforeSetContentView() {
        super.doBeforeSetContentView();
        intent = getIntent();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {

        btnGetCheckNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //手机号
                String cell = inputCell.getText().toString();
                if("".equals(cell)){

                    ToastUtils.showToast(RegisterActivity.this,"请输入手机号码");
                    return;
                }

                if(!MyTextUtils.isNumber(cell) || cell.length() != 11){

                    ToastUtils.showToast(RegisterActivity.this,"请输入正确的电话号码");
                    return;
                }


                //检测能否点击
                myCountDownTimer.start();

                //同志服务器发短信
                registerForUser(cell);
            }
        });

    }

    private void registerForUser(String cell) {

        Map<String, RequestBody> para = new HashMap<>();

        para.put("cell", RequestUtils.toRequestBody(cell));

        MyApi api = MyNetwork.getInstance().getApi(this);
        api.registerAcount(para).compose(MyNetwork.schedulersTransformer)
                .subscribe(new Observer<DealCode>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DealCode dealCode) {

                        if(dealCode.getReturnCode() == 1){

                            //发送验证码成功
                            btnSure.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //向服务器发送验证码
                                    checkCode();

                                }
                            });


                        }else if(dealCode.getReturnCode() == 2){

                            //空的

                        }else if(dealCode.getReturnCode() == 3){

                            //号码已被注册
                            ToastUtils.showToast(UIUtils.getContext(),"手机号已被注册");
                        }

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void checkCode() {

        //手机号
        String cell = inputCell.getText().toString();
        if("".equals(cell)){

            ToastUtils.showToast(RegisterActivity.this,"请输入手机号码");
            return;
        }

        if(!MyTextUtils.isNumber(cell) || cell.length() != 11){

            ToastUtils.showToast(RegisterActivity.this,"请输入正确的电话号码");
            return;
        }

        //手机号
        final String code = etCheckNum.getText().toString();
        if("".equals(code)){

            ToastUtils.showToast(RegisterActivity.this,"请输入验证码");
            return;
        }

        if(!MyTextUtils.isNumber(code) || code.length() != 6){

            ToastUtils.showToast(RegisterActivity.this,"请输入合法的验证码");
            return;
        }

        //验证验证码
        MyApi api = MyNetwork.getInstance().getApi(this);
        api.checkCodeByPhone(cell,code).compose(MyNetwork.schedulersTransformer)
                .subscribe(new Observer<DealCode>(){
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e("验证码===",code);
                    }

                    @Override
                    public void onNext(DealCode dealCode) {
                        if(dealCode.getReturnCode() == 1){
                            //正确 弹出对话框
                            inputPassword();
                        }else {

                            //错误
                            ToastUtils.showToast(UIUtils.getContext(),"验证码有误");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void inputPassword() {

        View view = View.inflate(UIUtils.getContext(),R.layout.layout_set_pwd,null);

        final EditText pwd1 = (EditText) view.findViewById(R.id.et_pwd1);
        final EditText pwd2 = (EditText) view.findViewById(R.id.et_pwd2);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("设置密码");
        dialog.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if("".equals(pwd1.getText()) || "".equals(pwd2.getText())){
                    ToastUtils.showToast(UIUtils.getContext(),"密码为空");
                    dialog.dismiss();
                    return;
                }
                if(!pwd1.getText().toString().equals(pwd2.getText().toString())){

                    ToastUtils.showToast(UIUtils.getContext(),"密码不一致");
                    dialog.dismiss();
                    return;
                }
                Log.e("手机",inputCell.getText().toString());
                Log.e("密码",pwd2.getText().toString());

                long phone = Long.parseLong(inputCell.getText().toString());

                String passwd = MD5Utils.MD5(String.valueOf(pwd1.getText()));

                Log.e("手机号=="+phone,"密码=="+passwd);
                //设置密码
                settingPwd(phone, passwd);

            }

        }).show();


    }

    private void settingPwd(long phone, String pwd) {

        MyApi api = MyNetwork.getInstance().getApi(this);
        api.setPasswd(phone,pwd).compose(MyNetwork.schedulersTransformer)
                .subscribe(new Observer<LoginInfo>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LoginInfo loginInfo) {
                        if (loginInfo.getReturnCode() == 2) {

                            UserManager.create(RegisterActivity.this).setToken(loginInfo.getToken());
                            UserManager.create(RegisterActivity.this).setUid(loginInfo.getUid());
                            UserManager.create(RegisterActivity.this).setDeadline();
                            UserManager.create(RegisterActivity.this).setISLogin();

                            SPUtils.setSharedlongData(RegisterActivity.this, "uploadPrefer", System.currentTimeMillis()+1000*60*60*24);


                            //结束Actvity
                            //跳转到Main

                            AppManager.getAppManager().finishActivity(LoginActivity.class);
                            AppManager.getAppManager().finishActivity(MainActivity.class);
                            startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                            finish();

                        } else if (loginInfo.getReturnCode() == 1) {
                            UIUtils.showToast("注册失败!");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    protected void onDestroy() {
        if(myCountDownTimer != null){
            myCountDownTimer.cancel();
        }
        super.onDestroy();
    }

    @Override
    protected void setPresenter() {

    }


    private class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //计时过程
        @Override
        public void onTick(long millisUntilFinished) {

            btnGetCheckNum.setClickable(false);
            btnGetCheckNum.setText(millisUntilFinished/1000+"s");

        }

        @Override
        public void onFinish() {

            //重新给Button设置文字
            btnGetCheckNum.setText("重新获取验证码");
            //设置可点击
            btnGetCheckNum.setClickable(true);

        }
    }

}
