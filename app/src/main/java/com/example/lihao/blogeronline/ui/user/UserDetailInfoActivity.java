package com.example.lihao.blogeronline.ui.user;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.BuildConfig;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.app.Constants;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.DealCode;
import com.example.lihao.blogeronline.bean.DetailUserInfo;
import com.example.lihao.blogeronline.ui.SplashActivity;
import com.example.lihao.blogeronline.ui.user.model.imple.UserModelImple;
import com.example.lihao.blogeronline.ui.user.presenter.DetailUserPresenter;
import com.example.lihao.blogeronline.ui.user.presenter.imple.DetailUserPresenterImple;
import com.example.lihao.blogeronline.ui.user.view.DetailUserView;
import com.example.lihao.blogeronline.utils.FormatUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by lihao on 17-11-1.
 */

public class UserDetailInfoActivity extends BaseActivity implements DetailUserView {

    private static final int SELECT_PIC = 0;

    @BindView(R.id.iv_user_icon)
    ImageView ivUserIcon;
    @BindView(R.id.tv_accounts)
    TextView tvAccounts;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_tag)
    TextView tvTag;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.ll_nickname)
    LinearLayout llNickname;
    @BindView(R.id.ll_tag)
    LinearLayout llTag;
    @BindView(R.id.ll_sex)
    LinearLayout llSex;
    @BindView(R.id.tv_user_rank)
    TextView tvUserRank;
    @BindView(R.id.ll_user_rank)
    LinearLayout llUserRank;
    @BindView(R.id.tv_user_type)
    TextView tvUserType;
    @BindView(R.id.iv_user_type_icon)
    ImageView ivUserTypeIcon;
    @BindView(R.id.ll_user_type)
    LinearLayout llUserType;
    @BindView(R.id.tv_user_birthday)
    TextView tvUserBirthday;
    @BindView(R.id.ll_user_birthday)
    LinearLayout llUserBirthday;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private long uid;

    private boolean isSelf = true;

    private DetailUserPresenter mPresenter;

    @Override
    protected void doBeforeSetContentView() {

        uid = getIntent().getLongExtra("uid", 0);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_update_user_info;
    }

    @Override
    protected void setPresenter() {

        new DetailUserPresenterImple(this, this)
                .getDetailUserInfo(UserManager.create(this).getUid(), uid);

    }

    protected void initView() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (uid == 0) {

            actionBar.setTitle("修改资料");
            //本人的
            uid = UserManager.create(UIUtils.getContext()).getUid();

            isSelf = true;

        } else {

            actionBar.setTitle("查看资料");

            isSelf = false;
        }

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

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {

            mPresenter.getDetailUserInfo(UserManager.create(this).getUid(), uid);
        }
    }

    @Override
    public void setPresenter(DetailUserPresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void detailUserInfoLoaded(DetailUserInfo info) {

        if (info != null) {

            DetailUserInfo.UserBean user = info.getUser();
            Glide.with(this)
                    .load(Constants.RESOURCE_URL + user.getU_icon())
                    .into(ivUserIcon);


            tvAccounts.setText(user.getUid() + "");
            tvNickname.setText(user.getNickname());
            if (user.getContent() != null) {
                tvTag.setText(user.getContent());
            }
            tvSex.setText(user.getSex());
            tvUserRank.setText(user.getRank_level() + "");
            tvUserType.setText(user.getUser_type_name());

            Glide.with(this)
                    .load(Constants.RESOURCE_URL + user.getUser_type_icon())
                    .into(ivUserTypeIcon);


            tvUserBirthday.setText(FormatUtils.stampToDate(user.getCreate_time()));
        }
    }

    @Override
    public void handleError(Throwable throwable) {
        UIUtils.showToast(throwable.getMessage());
    }


    @OnClick({R.id.iv_user_icon, R.id.ll_nickname, R.id.ll_tag, R.id.ll_sex, R.id.ll_user_rank, R.id.ll_user_type, R.id.ll_user_birthday})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_icon:
                if (isSelf) {
                    dealWithUserIcon();
                }
                break;
            case R.id.ll_nickname:
                if (isSelf) {
                    updateUserInfo("修改昵称", 1);
                }
                break;
            case R.id.ll_tag:
                if (isSelf) {
                    updateUserInfo("更新签名", 2);
                }
                break;
            case R.id.ll_sex:
                if (isSelf) {
                    updateUserInfo("修改性别", 3);
                }
                break;
            case R.id.ll_user_rank:
                //调转到会员界面
                UIUtils.showToast("会员界面");
                break;
            case R.id.ll_user_type:
                //调转到会员界面
                UIUtils.showToast("会员界面");
                break;
            case R.id.ll_user_birthday:
                if (isSelf) {
                    updateUserInfo("出生日期", 4);
                }
                break;
        }
    }

    private void updateUserInfo(String title, int i) {

        final EditText et = new EditText(this);
        et.setGravity(Gravity.CENTER);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title);

        switch (i) {
            case 1:
                et.setText(tvNickname.getText());
                dialog.setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (!input.equals("")) {
                                    //上传昵称
                                    uploadUserNickname(input);
                                } else {
                                    UIUtils.showToast("输入为空");
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                break;
            case 2:
                //更新签名
                et.setText(tvTag.getText());
                dialog.setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (!input.equals("")) {
                                    //上传签名
                                    uploadUserTag(input);
                                } else {
                                    UIUtils.showToast("输入为空");
                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case 3:
                //性别
                int choice = tvSex.getText().equals("男") ? 0 : 1;
                dialog.setSingleChoiceItems(new String[]{"男", "女"}, choice, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (which == 0) {
                            uploadUserSex("男");
                        } else {
                            uploadUserSex("女");
                        }

                        dialog.dismiss();
                    }
                }).show();
                break;
            case 4:
                //出生日期
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String date = year + "-" + month + "-" + dayOfMonth;

                        uploadUserBirth(FormatUtils.dateToStamp(date));
                    }
                }, 2000, 1, 2).show();//年月日
                break;
        }
    }

    private void uploadUserBirth(final long l) {

        new UserModelImple()
                .updateUserBirth(UserManager.create(UIUtils.getContext()).getUid(), l)
                .subscribe(new Observer<DealCode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DealCode dealCode) {

                        //0 重复 1 成功 2 空的 3重新登陆
                        if (dealCode.getReturnCode() == 0) {
                            UIUtils.showToast("更新生日失败");
                        } else if (dealCode.getReturnCode() == 1) {
                            UIUtils.showToast("更新生日成功");
                            tvUserBirthday.setText(FormatUtils.stampToDate(l));
                        } else if (dealCode.getReturnCode() == 2) {
                            UIUtils.showToast("uid为空");
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

    private void uploadUserSex(final String sex) {

        new UserModelImple()
                .updateUserSex(UserManager.create(UIUtils.getContext()).getUid(), sex)
                .subscribe(new Observer<DealCode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DealCode dealCode) {

                        //0 重复 1 成功 2 空的 3重新登陆
                        if (dealCode.getReturnCode() == 0) {
                            UIUtils.showToast("更新性别失败");
                        } else if (dealCode.getReturnCode() == 1) {
                            UIUtils.showToast("更新性别成功");
                            tvSex.setText(sex);
                        } else if (dealCode.getReturnCode() == 2) {
                            UIUtils.showToast("uid为空");
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

    private void uploadUserTag(final String input) {

        new UserModelImple()
                .updateUserTag(UserManager.create(UIUtils.getContext()).getUid(), input)
                .subscribe(new Observer<DealCode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DealCode dealCode) {

                        //0 重复 1 成功 2 空的 3重新登陆
                        if (dealCode.getReturnCode() == 0) {
                            UIUtils.showToast("更新签名失败");
                        } else if (dealCode.getReturnCode() == 1) {
                            UIUtils.showToast("更新签名成功");
                            tvTag.setText(input);
                        } else if (dealCode.getReturnCode() == 2) {
                            UIUtils.showToast("uid为空");
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

    private void uploadUserNickname(final String nickname) {


        new UserModelImple()
                .updateUserNickname(UserManager.create(UIUtils.getContext()).getUid(), nickname)
                .subscribe(new Observer<DealCode>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DealCode dealCode) {

                        //0 重复 1 成功 2 空的 3重新登陆
                        if (dealCode.getReturnCode() == 0) {
                            UIUtils.showToast("昵称已被注册");
                        } else if (dealCode.getReturnCode() == 1) {
                            UIUtils.showToast("昵称更改成功");
                            tvNickname.setText(nickname);
                        } else if (dealCode.getReturnCode() == 2) {
                            UIUtils.showToast("uid为空");
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

    private void dealWithUserIcon() {

        //弹出窗口
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("更换图像");

        dialog.setItems(R.array.user_icon, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                switch (which) {
                    case 0:
                        //更换图像
//                        goAlbums();
                        SelectImg();

                        break;

                    case 1:
                        //查看图像
                        takePhoto();
                        break;
                }
            }
        });
        dialog.show();
    }

    private ImageCaptureManager captureManager;

    private void takePhoto() {

        //权限
        RxPermissions rxPermissions = new RxPermissions(UserDetailInfoActivity.this);
        rxPermissions
                .request(Manifest.permission.CAMERA) //写
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Boolean value) {

                        if(value){

                            try {
                                if (captureManager == null) {
                                    captureManager = new ImageCaptureManager(UserDetailInfoActivity.this);
                                }
                                Intent intent = captureManager.dispatchTakePictureIntent();
                                startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                            } catch (IOException e) {
                                UIUtils.showToast(UIUtils.getString(R.string.msg_no_camera));
                                e.printStackTrace();
                            }

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

    private void SelectImg() {
        PhotoPickerIntent intent = new PhotoPickerIntent(UserDetailInfoActivity.this);
        intent.setSelectModel(SelectModel.SINGLE);
        intent.setShowCarema(true); // 是否显示拍照， 默认false
        // intent.setImageConfig(config);
        startActivityForResult(intent, SELECT_PIC);
    }

    /**
     * 调用相册
     */
    private void goAlbums() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_PIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case SELECT_PIC:
                    ArrayList<String> extra = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Intent intent = new Intent(this, CutPhotoActivity.class);
                    File file = new File(extra.get(0));
                    intent.setData(Uri.fromFile(file));
                    Log.e("路径", Uri.parse(extra.get(0)).toString());
                    startActivity(intent);
                    break;
                // 拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        // 照片地址
                        String photoPath = captureManager.getCurrentPhotoPath();
                        Intent in = new Intent(this, CutPhotoActivity.class);
                        File f = new File(photoPath);
                        in.setData(Uri.fromFile(f));
                        startActivity(in);
                        break;
                    }
                    break;
            }
        }
    }

}
