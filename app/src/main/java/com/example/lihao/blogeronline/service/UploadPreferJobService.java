package com.example.lihao.blogeronline.service;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.bean.UserPrefer;
import com.example.lihao.blogeronline.broadcast.BatteryBroadCast;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.ui.user.LoginActivity;
import com.example.lihao.blogeronline.utils.NetworkUtil;
import com.example.lihao.blogeronline.utils.RequestUtils;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.RequestBody;

/**
 * Created by lihao on 17-12-5.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UploadPreferJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters params) {

        if (!BatteryBroadCast.BATTERY_TAG.equals(BatteryBroadCast.BATTERY_LOW)) {

            Log.e("上传偏好","...");
            //清空记录
            SPUtils.setSharedlongData(UIUtils.getContext(), "uploadPreferTime", System.currentTimeMillis()+1000*60*60*24);

            //上传用户偏好 1天上传一次?
            final UserManager userManager = UserManager.create(UIUtils.getContext());
            if(!userManager.getIsLogin()){
                return false;
            }
            //获取信息
            String userPrefer = SPUtils.getSharedStringData(UIUtils.getContext(),"UserPrefer","");

            //String preferJson = JSON.toJSONString(prefer);

            Map<String, RequestBody> para = new HashMap<>();

            para.put("uid", RequestUtils.toRequestBody(String.valueOf(userManager.getUid())));
            para.put("token",RequestUtils.toRequestBody(userManager.getToken()));
            para.put("prefer_json",RequestUtils.toRequestBody(userPrefer));

            MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());
            api.uploadPreferToServer(para)
                    .compose(MyNetwork.schedulersTransformer)
                    .subscribe(new Observer<DealCode>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(DealCode dealCode) {
                            if(dealCode.getReturnCode() == 1){
                                //清空
                                //重置信息
                                //建立用户偏好
                                //建立用户偏好
                                UserPrefer userPrefer = new UserPrefer();
                                userPrefer.setUid(userManager.getUid());
                                //转化为string
                                Gson gson = new Gson();
                                String json = gson.toJson(userPrefer);
                                SPUtils.setSharedStringData(UIUtils.getContext(),"UserPrefer",json);
                            }else {
                                //token错误
                                //重新登陆

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

        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }


}
