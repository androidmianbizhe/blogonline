package com.example.lihao.blogeronline.ui;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.adapter.MainFragmentAdapter;
import com.example.lihao.blogeronline.app.Constants;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.base.BaseFragment;
import com.example.lihao.blogeronline.bean.Article;
import com.example.lihao.blogeronline.bean.ArticleType;
import com.example.lihao.blogeronline.bean.HtmlParseReg;
import com.example.lihao.blogeronline.bean.SimpleUserInfo;
import com.example.lihao.blogeronline.bean.UserPrefer;
import com.example.lihao.blogeronline.broadcast.BatteryBroadCast;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.service.UploadArticleJobService;
import com.example.lihao.blogeronline.service.UploadPreferJobService;
import com.example.lihao.blogeronline.ui.common.MyBasePageFragment;
import com.example.lihao.blogeronline.ui.tabchoose.FromActivity;
import com.example.lihao.blogeronline.ui.tabchoose.TabChooserActivity;
import com.example.lihao.blogeronline.ui.user.LoginActivity;
import com.example.lihao.blogeronline.ui.user.UserDetailInfoActivity;
import com.example.lihao.blogeronline.ui.user.model.UserModel;
import com.example.lihao.blogeronline.ui.user.model.imple.UserModelImple;
import com.example.lihao.blogeronline.utils.CleanMessageUtil;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenhuaijun.easytagdragview.bean.SimpleTitleTip;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lihao on 17-11-17.
 */

public class MainActivity extends BaseActivity {

    public static ArrayList<SimpleTitleTip> mTips = new ArrayList<SimpleTitleTip>();

    private static final int TITLE_REQUEST_CODE = 101;
    private static final int LOGIN_REQUEST_CODE = 102;
    private static final int FROM_REQUEST_CODE = 103;
    public static final int RESULT_CODE = 2;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.tabs)
    TabLayout tabs;

    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.navigationView)
    NavigationView navigationView;
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    private ActionBarDrawerToggle toggle;

    private MainFragmentAdapter mFragmentAdapter;
    private ImageView ivUserType;
    private TextView tvUserName;
    private TextView tvTag;
    private ImageView ivUser;

    private JobScheduler mJobScheduler;
    private BatteryBroadCast mBatInfoReveiver;
    private List<SimpleTitleTip> retList;

    int tabIndex = 0;


    @Override
    protected void doBeforeSetContentView() {
        super.doBeforeSetContentView();
        //保持长连接。
        //如果是false就仅支持一次链接。
        //true支持多次连接。
        //System.setProperty("http.keepAlive", "false");
    }

    @Override
    protected void initView() {

        initToolBar();

        initUser();

        initViewPagerAndData();

        initNavigationView();

        initBatteryBroadcast();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if(UserManager.create(this).getIsLogin()){
                initJob();
            }
        }
    }

    private void initViewPagerAndData() {

        //获取from_id
        int from_id = SPUtils.getSharedIntData(this, "from_id", 1);
        MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());
        api.getTypeByFromId(from_id,2).compose(MyNetwork.schedulersTransformer)
                .subscribe(new Observer<ArticleType>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArticleType articleTypes) {

                        mTips.clear();
                        for (int i=0;i<articleTypes.getArticle_type().size();i++){

                            ArticleType.ArticleTypeBean typeBean = articleTypes.getArticle_type().get(i);
                            mTips.add(new SimpleTitleTip(
                                    typeBean.getType_id(),
                                    typeBean.getArticle_type_name(),
                                    typeBean.getType_url(),
                                    typeBean.getFrom_id()));

                        }
                        //保存最大条数
                        SPUtils.setSharedIntData(UIUtils.getContext(),"max_page_items",articleTypes.getF_page_max_item());
                        SPUtils.setSharedStringData(UIUtils.getContext(),"article_body",articleTypes.getArticle_body());

                        //加载解析方式
                        loadRegxById();

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void loadRegxById() {

        //获取from_id
        int from_id = SPUtils.getSharedIntData(this, "from_id", 1);
        MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());
        api.getRegxById(from_id).compose(MyNetwork.schedulersTransformer)
                .subscribe(new Observer<HtmlParseReg>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HtmlParseReg htmlParseReg) {
                        Gson gson = new Gson();
                        String toJson = gson.toJson(htmlParseReg);
                        SPUtils.setSharedStringData(UIUtils.getContext(),"regx",toJson);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                        Log.e("initViewPager","====");
                        initViewPager();
                    }
                });




    }

    private void initViewPager() {

        initTabs();

        vpContent.setAdapter(null);

        List<BaseFragment> mNewsFragmentList = new ArrayList<>();
        //获取json
        //(name,url)
        //加入到list中
        for(int i=0;i<retList.size();i++){

            BaseFragment fragment = new MyBasePageFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("key", retList.get(i));
            fragment.setArguments(bundle);
            mNewsFragmentList.add(fragment);
        }

        /**
         BaseFragment mobileDeveFragment = new MobileDeveFragment();
         BaseFragment cloudCacuFragment = new CloudCacuFragment();

         mNewsFragmentList.add(mobileDeveFragment);
         mNewsFragmentList.add(cloudCacuFragment);
         */
        if (mFragmentAdapter == null) {

            mFragmentAdapter = new MainFragmentAdapter(getSupportFragmentManager(), mNewsFragmentList, retList);
        } else {
            //刷新fragment
            mFragmentAdapter.setFragments(getSupportFragmentManager(), mNewsFragmentList, retList);
        }


        vpContent.setAdapter(mFragmentAdapter);
        vpContent.setOffscreenPageLimit(retList.size()-1);
        tabs.setupWithViewPager(vpContent);

        setIndicator(tabs,10,10);

        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabIndex = position;
                toolbar.setTitle(retList.get(position).getTip());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(tabIndex > 0 && tabIndex < vpContent.getChildCount()){

            vpContent.setCurrentItem(tabIndex);
        }
    }

    private void initTabs() {

        //判断已包含的标签
        String containTabs = SPUtils.getSharedStringData(this, "containTabs", "");

        if(!containTabs.equals("")){

            Gson gson = new Gson();
            retList = gson.fromJson(containTabs,
                    new TypeToken<List<SimpleTitleTip>>() {}.getType());
            retList.add(0,new SimpleTitleTip(-1,"推荐","",-1));


        }else {
            retList = new ArrayList<>();
            retList.add(new SimpleTitleTip(-1,"推荐","",-1));
            retList.addAll(mTips);

        }
    }


    private void initBatteryBroadcast() {

        mBatInfoReveiver = new BatteryBroadCast();

        registerReceiver(mBatInfoReveiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }


    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initJob() {

        mJobScheduler = (JobScheduler)
                getSystemService(Context.JOB_SCHEDULER_SERVICE );

        //上传文章的服务
        ComponentName uploadArticleJob = new ComponentName(this, UploadArticleJobService.class);

        JobInfo.Builder uploadArticleBuilder =
                new JobInfo.Builder(1,
                        uploadArticleJob);

        //唤醒job条件
        uploadArticleBuilder.setOverrideDeadline(1000);
        uploadArticleBuilder.setRequiresDeviceIdle(true);//设备空闲时

        if( mJobScheduler.schedule(uploadArticleBuilder.build()) <= 0 ) {
            //If something goes wrong
            Log.e("uploadJob","执行出错");
        }else {
            Log.e("uploadJob","执行成功");
        }

        //判断
        long uploadPrefer = SPUtils.getSharedlongData(this, "uploadPreferTime", System.currentTimeMillis());
        if(System.currentTimeMillis() > uploadPrefer){

            //上传用户偏好的服务
            ComponentName uploadPreferJob = new ComponentName(this, UploadPreferJobService.class);

            JobInfo.Builder uploadPreferBuilder = new JobInfo.Builder(2,
                                                            uploadPreferJob);

            //唤醒job条件
            uploadPreferBuilder.setOverrideDeadline(6000);

            if( mJobScheduler.schedule(uploadPreferBuilder.build()) <= 0 ) {
                //If something goes wrong
                Log.e("uploadPreferJob","执行出错");
            }else {
                Log.e("uploadPreferJob","执行成功");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUser();
    }

    private void initNavigationView() {

        View headerView = navigationView.getHeaderView(0);

        ivUser = headerView.findViewById(R.id.iv_user);
        ivUserType = headerView.findViewById(R.id.iv_user_type);
        tvUserName = headerView.findViewById(R.id.tv_user_name);
        tvTag = headerView.findViewById(R.id.tv_tag);

        ivUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(getApplicationContext(),"登陆");
                //判断是否登录
                checkLogin();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.mn_cache :
                        try {
                            ToastUtils.showToast(getApplicationContext(),"已清理缓存"+ CleanMessageUtil.getTotalCacheSize(MainActivity.this));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        CleanMessageUtil.clearAllCache(MainActivity.this);
                        break;
                    case R.id.mn_out :
                        clearUser();
                        break;
                    case R.id.mn_skin :
                        //接换模式
                        alterMode();
                        break;
                    case R.id.mn_type :
                        Intent intent = new Intent(MainActivity.this,TabChooserActivity.class);
                        startActivityForResult(intent, TITLE_REQUEST_CODE);
                        break;
                    case R.id.mn_from :
                        Intent from_intent = new Intent(MainActivity.this,FromActivity.class);
                        startActivityForResult(from_intent, FROM_REQUEST_CODE);
                        break;
                }

                return true;
            }
        });

        //菜单
        boolean isLight = SPUtils.getSharedBooleanData(UIUtils.getContext(),"alter_mode_light",true);
        //变为夜间
        MenuItem item = navigationView.getMenu().findItem(R.id.mn_skin);
        item.setTitle(!isLight?"白天模式":"夜间模式");
        item.setIcon(!isLight?R.drawable.sun:R.drawable.moon);
    }


    private void alterMode() {

        boolean isLight = SPUtils.getSharedBooleanData(UIUtils.getContext(),"alter_mode_light",true);
        if(isLight){

            //变为夜间
            MenuItem item = navigationView.getMenu().findItem(R.id.mn_skin);
            item.setTitle("白天模式");
            item.setIcon(R.drawable.sun);

            SPUtils.setSharedBooleanData(UIUtils.getContext(),"alter_mode_light",false);

            //切换
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);//切换夜间模式
            recreate();//重新启动当前activity

        }else {

            MenuItem item = navigationView.getMenu().findItem(R.id.mn_skin);
            item.setTitle("夜间模式");
            item.setIcon(R.drawable.moon);

            SPUtils.setSharedBooleanData(UIUtils.getContext(),"alter_mode_light",true);

            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);//切换日间模式
            recreate();//重新启动当前activity

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CODE){
            if(requestCode == TITLE_REQUEST_CODE){
                //刷新viewpager
                initViewPagerAndData();
            }else if(requestCode == LOGIN_REQUEST_CODE){
                initUser();
                initViewPagerAndData();
            } else if(requestCode == FROM_REQUEST_CODE){
                //刷新viewpager
                initViewPagerAndData();
            }
        }
    }

    private void initUser() {

        if(!UserManager.create(this).getIsLogin()){
            return;
        }

        //加载user
        //获取userid 加载信息
        long uid = UserManager.create(getApplicationContext()).getUid();
        UserModel model = new UserModelImple();
        model.getSimpleUserInfo(uid).subscribe(new Observer<SimpleUserInfo>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SimpleUserInfo info) {

                if(info.getReturnCode() == 1){

                    SimpleUserInfo.UserBean user = info.getUser();

                    Glide.with(UIUtils.getContext())
                            .load(Constants.RESOURCE_URL + user.getU_icon())
                            .bitmapTransform(new CropCircleTransformation(UIUtils.getContext()))
                            .crossFade(1000).into(ivUser);

                    Glide.with(UIUtils.getContext())
                            .load(Constants.RESOURCE_URL + user.getUser_type_icon())
                            .into(ivUserType);

                    ivUserType.setVisibility(View.VISIBLE);
                    tvUserName.setText(user.getNickname());
                    tvTag.setText(user.getContent());

                } else if(info.getReturnCode() == 2){
                    //重新登陆
                    clearUser();

                } else {

                    ToastUtils.showToast(getApplicationContext(),"服务器异常");
                }

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToast(getApplicationContext(),e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

        String userPreferStr = SPUtils.getSharedStringData(UIUtils.getContext(),"UserPrefer","");

        if(userPreferStr.equals("")){
            //建立用户偏好
            UserPrefer userPrefer = new UserPrefer();
            userPrefer.setUid(uid);

            for(int i=0;i<mTips.size();i++){
                userPrefer.getPrefer().add(new UserPrefer.Prefer(mTips.get(i).getId(),0,1));
            }

            //转化为string
            Gson gson = new Gson();
            String json = gson.toJson(userPrefer);
            SPUtils.setSharedStringData(UIUtils.getContext(),"UserPrefer",json);
        }

    }

    private void clearUser() {

        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setTitle("注销用户");
        dialog.setMessage("确定要注销用户吗?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                drawer.closeDrawer(GravityCompat.START);

                UserManager.create(getApplicationContext()).clearUser();

                ivUser.setImageResource(R.drawable.u);
                ivUserType.setVisibility(View.INVISIBLE);
                tvUserName.setText("未登录");
                tvTag.setText("暂无签名");

                //((MyBasePageFragment)mFragmentAdapter.getItem(0)).initData();
                initViewPagerAndData();

            }
        }).setNegativeButton("取消",null).show();

    }

    private void checkLogin() {

        if (UserManager.create(this).getIsLogin()) {

            //登陆了点击 就是上传图像
            long deadline = UserManager.create(this).getDeadline();
            if (System.currentTimeMillis() > deadline) {
                //过期
                UserManager.create(this).clearUser();

            } else {

                //详情界面
                startActivity(new Intent(UIUtils.getContext(), UserDetailInfoActivity.class));
            }

        }else {
            Intent intent = new Intent(this, LoginActivity.class);
            //调转到登录界面
            startActivityForResult(intent,LOGIN_REQUEST_CODE);
        }

    }

    private void initToolBar() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //绑定侧边栏
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.drawer_open, R.string.drawer_close);
        toggle.syncState();//初始化状态
        //设置监听
        drawer.addDrawerListener(toggle);

    }

    @Override
    protected int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setPresenter() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                toggle.onOptionsItemSelected(item);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {

        if(mJobScheduler != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mJobScheduler.cancel(1);//注销上传博客
            }
        }

        if(mBatInfoReveiver != null){
            unregisterReceiver(mBatInfoReveiver);//注销
        }

        super.onDestroy();
    }

    public void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }

}
