package com.example.lihao.blogeronline.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.app.Constants;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.SplashJson;
import com.example.lihao.blogeronline.utils.BitmapUtils;
import com.example.lihao.blogeronline.utils.FileUtils;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends BaseActivity {

    private String SPLASH_FILE_NAME = "splash";

    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    @BindView(R.id.tv_copyright)
    TextView tvCopyright;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            //跳转
            startActivity(new Intent(SplashActivity.this,MainActivity.class));
            finish();

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void doBeforeSetContentView() {

        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void initView() {
        mHandler.sendEmptyMessageDelayed(0,3000);
        checkPermission();
    }

    private void showSplash() {

        File splashImg = new File(FileUtils.getSplashDir(this), SPLASH_FILE_NAME);
        if (splashImg.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(splashImg.getPath());
            ivSplash.setImageBitmap(bitmap);
        }
    }

    @Override
    protected void setPresenter() {

    }

    private void checkPermission() {

        RxPermissions rxPermissions = new RxPermissions(SplashActivity.this);
        rxPermissions
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE //写
                        ,Manifest.permission.READ_EXTERNAL_STORAGE //读
                )
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {

                        if(value){

                            ToastUtils.showToast(getApplicationContext(),"授权成功");

                            showSplash();
                            loadSplash();

                        }else {

                            ToastUtils.showToast(getApplicationContext(),"授权失败");

                            //弹出对话框 提示用户 然后推出
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

    private void loadSplash() {

        //下载
        //创建okHttpClient对象
        OkHttpClient mOkHttpClient = new OkHttpClient();
        //创建一个Request
        final Request request = new Request.Builder()
                .url(Constants.SPLASH_URL)
                .build();
        //new call
        Call call = mOkHttpClient.newCall(request);
        //请求加入调度
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                //ToastUtils.showToast(SplashActivity.this,e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                Log.e("splash",json);
                Gson gson = new Gson();
                SplashJson splashJson = gson.fromJson(json, SplashJson.class);

                //主线程
                saveBitmap("http://cn.bing.com"+splashJson.getImages().get(0).getUrl());
            }
        });

    }

    private void saveBitmap(final String s) {

        //判断链接是否相同
        if(s.equals(SPUtils.getSharedStringData(SplashActivity.this,"splash_url",""))){
            //相同则不加载
            return;
        }

        new Thread(){

            @Override
            public void run() {

                try {
                    Bitmap bitmap = Glide.with(getApplicationContext())
                            .load(s)
                            .asBitmap()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();

                    if (bitmap != null){
                        // 在这里执行图片保存方法
                        SPUtils.setSharedStringData(SplashActivity.this,"splash_url",s);

                        BitmapUtils.saveBitmapFile(bitmap,FileUtils.getSplashDir(SplashActivity.this)+SPLASH_FILE_NAME);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:

                mHandler.removeCallbacksAndMessages(null);
                mHandler.sendEmptyMessage(0);

                break;

        }

        return super.onTouchEvent(event);
    }
}
