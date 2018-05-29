package com.example.lihao.blogeronline.service;

import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.broadcast.BatteryBroadCast;
import com.example.lihao.blogeronline.data.AppRepository;
import com.example.lihao.blogeronline.data.UploadArticleDaoImple;
import com.example.lihao.blogeronline.data.db.LiteOrmHelper;
import com.example.lihao.blogeronline.data.model.UploadArticle;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.utils.NetworkUtil;
import com.example.lihao.blogeronline.utils.RequestUtils;
import com.example.lihao.blogeronline.utils.UIUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import okhttp3.RequestBody;

/**
 * Created by lihao on 17-11-27.
 */

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class UploadArticleJobService extends JobService {

    public static final int QUERY_LENGTH = 4;

//    private Handler mJobHandler = new Handler(new Handler.Callback() {
//
//        @Override
//        public boolean handleMessage( Message msg ) {
//            Toast.makeText(getApplicationContext(),
//                    "JobService task running", Toast.LENGTH_SHORT )
//                    .show();
//            jobFinished( (JobParameters) msg.obj, false );
//
//            return true;
//        }
//
//    } );


    //返回值是false,系统假设这种方法返回时任务已经运行完成。
    //假设返回值是true,那么系统假定这个任务正要被运行
    @Override
    public boolean onStartJob(JobParameters params) {

        //1.检测是否链接wifi 并且是否可用
        //3.检测是否电量过低
        //4.检测是否上传数据

        if(NetworkUtil.isWifi(UIUtils.getContext())){

            if(!BatteryBroadCast.BATTERY_TAG.equals(BatteryBroadCast.BATTERY_LOW)){
                Log.e("上传博客","...");
                //检测是否含有数据
                AppRepository.getInstance().select(QUERY_LENGTH)
                        .map(new Function<List<UploadArticle>, String>() {
                            @Override
                            public String apply(List<UploadArticle> articleList) throws Exception {

                                if (articleList.size() >= 4) {

                                    AppRepository.getInstance().delete(articleList)
                                            .subscribe();

                                    return JSON.toJSONString(articleList);
                                }

                                return "";
                            }
                        })
                        .compose(MyNetwork.schedulersTransformer)
                        .subscribe(new Observer() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(Object o) {

                                if(!o.toString().equals("")){

                                    //上传博客
                                    UserManager userManager = UserManager.create(UIUtils.getContext());
                                    long uid = userManager.getUid();
                                    String token = userManager.getToken();

                                    //配置参数
                                    Map<String, RequestBody> para = new HashMap<>();

                                    para.put("uid", RequestUtils.toRequestBody(String.valueOf(uid)));
                                    para.put("token",RequestUtils.toRequestBody(token));
                                    para.put("blog_json",RequestUtils.toRequestBody(o.toString()));

                                    MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());
                                    api.uploadBlogToServer(para)
                                            .compose(MyNetwork.schedulersTransformer)
                                            .subscribe();

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
        }


//        mJobHandler.sendMessage( Message.obtain( mJobHandler, 1, params ) );

        return true;
    }

    /**
     * 当收到取消请求时，该方法是系统用来取消挂起的任务的。
     * 如果onStartJob()返回false，则系统会假设没有当前运行的任务，故不会调用该方法。
     */
    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
