package com.example.lihao.blogeronline.ui.user;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.ui.user.model.UserModel;
import com.example.lihao.blogeronline.ui.user.model.imple.UserModelImple;
import com.example.lihao.blogeronline.utils.BitmapUtils;
import com.example.lihao.blogeronline.utils.RequestUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.oginotihiro.cropview.CropView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by lihao on 17-11-2.
 */

public class CutPhotoActivity extends BaseActivity {

    @BindView(R.id.cropView)
    CropView cropView;
    @BindView(R.id.resultIv)
    ImageView resultIv;
    @BindView(R.id.cancelBtn)
    Button cancelBtn;
    @BindView(R.id.doneBtn)
    Button doneBtn;
    @BindView(R.id.btnlay)
    LinearLayout btnlay;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private Uri data;

    @Override
    protected void doBeforeSetContentView() {
        data = getIntent().getData();
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_cutphoto;
    }

    @Override
    protected void setPresenter() {

    }

    protected void initView() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setTitle("更换图像");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void initData() {


        if (data != null) {
            Log.e("data", data.toString());

            cropView.of(data).asSquare().initialize(this);
        } else {
            Log.e("data", "null");
        }
//                .withAspect(150, 150)
//                .withOutputSize(150, 150)
//                .initialize(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.cancelBtn, R.id.doneBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancelBtn:
                UIUtils.showToast("取消上传");
                finish();
                break;
            case R.id.doneBtn:
                //上传
                uploadHeadIcon();

                break;
        }
    }

    private void uploadHeadIcon() {

        Bitmap output = cropView.getOutput();
        String filename = UIUtils.getContext().getCacheDir()
                + "/crop_" + System.currentTimeMillis() + ".png";

        File file = BitmapUtils.saveBitmapFile(output, filename);

        Map<String, RequestBody> para = new HashMap<>();

        para.put("uid", RequestUtils.toRequestBody(String.valueOf(UserManager.create(this).getUid())));
        para.put("token", RequestUtils.toRequestBody(UserManager.create(this).getToken()));
        para.put("pic\"; filename=\"" + file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));

        UserModel userModel = new UserModelImple();
        userModel.uploadUserIcon(para)
                .subscribe(new Observer<DealCode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DealCode dealCode) {

                        // 0:uid空的
                        // 1:上欢失败
                        // 2:重新登陆
                        // 3:上传成功
                        // 4:上传失败

                        if (dealCode.getReturnCode() == 0) {
                            UIUtils.showToast("uid为空");
                        } else if (dealCode.getReturnCode() == 1 || dealCode.getReturnCode() == 4) {
                            UIUtils.showToast("上传失败");
                        } else if (dealCode.getReturnCode() == 2) {
                            reLogin();
                        } else {
                            UIUtils.showToast("上欢成功");
                            finish();
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

    private void reLogin() {

        UIUtils.showToast("重新登录");

    }

}
