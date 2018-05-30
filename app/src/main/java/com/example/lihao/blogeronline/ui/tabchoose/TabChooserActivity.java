package com.example.lihao.blogeronline.ui.tabchoose;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.app.BlogerApplication;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.ArticleType;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.ui.MainActivity;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wenhuaijun.easytagdragview.EasyTipDragView;
import com.wenhuaijun.easytagdragview.bean.SimpleTitleTip;
import com.wenhuaijun.easytagdragview.widget.TipItemView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/4/11.
 */

public class TabChooserActivity extends BaseActivity {

    @BindView(R.id.tab_progress)
    ProgressBar tabProgress;
    @BindView(R.id.easy_tip_drag_view)
    com.wenhuaijun.easytagdragview.EasyTipDragView easyTipDragView;
    private Intent intent;

    @Override
    protected void doBeforeSetContentView() {
        intent = getIntent();
        super.doBeforeSetContentView();
    }

    @Override
    protected void initView() {
        easyTipDragView.setVisibility(View.INVISIBLE);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_tabchooser;
    }

    @Override
    protected void setPresenter() {

        int from_id = SPUtils.getSharedIntData(this, "from_id", 1);

        MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());
        api.getTypeByFromId(from_id,-1).compose(MyNetwork.schedulersTransformer)
        .subscribe(new Observer<ArticleType>(){
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(ArticleType articleTypes) {

                if(articleTypes == null){
                    ToastUtils.showToast(BlogerApplication.getContext(),"加载失败");
                    return;
                }
                dealUI(articleTypes.getArticle_type());
            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.showToast(BlogerApplication.getContext(),"网络异常");
            }

            @Override
            public void onComplete() {
                tabProgress.setVisibility(View.GONE);
                easyTipDragView.setVisibility(View.VISIBLE);
            }
        });


    }

    private void dealUI(final ArrayList<ArticleType.ArticleTypeBean> articleTypes) {

        //记录所有类型
        Thread t = new Thread(){

            @Override
            public void run() {

                Gson gson = new Gson();
                String json = gson.toJson(articleTypes);

                SPUtils.setSharedStringData(TabChooserActivity.this,"AllTabs",json);

            }
        };

        t.start();
        //已包含
        List<SimpleTitleTip> containList = new ArrayList<>();
        String containTabs = SPUtils.getSharedStringData(TabChooserActivity.this, "containTabs", "");
        if(containTabs.equals("")){

            containList.addAll(MainActivity.mTips);

        }else {

            Gson gson = new Gson();
            List<SimpleTitleTip> retList = gson.fromJson(containTabs,
                    new TypeToken<List<SimpleTitleTip>>() {}.getType());

            containList.addAll(retList);
        }

        //未包含
        List<SimpleTitleTip> notContainList = new ArrayList<>();

        for(int i=0;i<articleTypes.size();i++){

            ArticleType.ArticleTypeBean articleType = articleTypes.get(i);

            boolean isAdd = true;
            for(int j=0;j<containList.size();j++){

                if(articleType.getType_id() == containList.get(j).getId()
                        && articleType.getFrom_id() == containList.get(j).getFrom_id()){
                    isAdd = false;
                }

            }
            if(isAdd){
                SimpleTitleTip tip = new SimpleTitleTip();

                tip.setId(articleType.getType_id());
                tip.setTip(articleType.getArticle_type_name());
                tip.setUrl(articleType.getType_url());
                tip.setFrom_id(articleType.getFrom_id());

                notContainList.add(tip);
            }
        }

        //设置已包含的标签数据
        easyTipDragView.setAddData(notContainList);
        //设置可以添加的标签数据
        easyTipDragView.setDragData(containList);
        //在easyTipDragView处于非编辑模式下点击item的回调（编辑模式下点击item作用为删除item）
        easyTipDragView.setSelectedListener(new TipItemView.OnSelectedListener() {
            @Override
            public void onTileSelected(SimpleTitleTip entity, int position, View view) {
                //ToastUtils.showToast(TabChooserActivity.this,((SimpleTitleTip) entity).getTip());
            }
        });
        //设置每次数据改变后的回调（例如每次拖拽排序了标签或者增删了标签都会回调）
        easyTipDragView.setDataResultCallback(new EasyTipDragView.OnDataChangeResultCallback() {
            @Override
            public void onDataChangeResult(ArrayList<SimpleTitleTip> tips) {
                Log.i("heheda", tips.toString());
            }
        });
        //设置点击“确定”按钮后最终数据的回调
        easyTipDragView.setOnCompleteCallback(new EasyTipDragView.OnCompleteCallback() {
            @Override
            public void onComplete(ArrayList<SimpleTitleTip> tips) {

                Gson gson = new Gson();
                String json = gson.toJson(tips);

                SPUtils.setSharedStringData(TabChooserActivity.this,"containTabs",json);

                ToastUtils.showToast(TabChooserActivity.this,"添加成功");

                setResult(MainActivity.RESULT_CODE);

                finish();
            }

            @Override
            public void onCancel() {
                ToastUtils.showToast(TabChooserActivity.this,"取消添加");

                setResult(MainActivity.RESULT_CODE);

                finish();
            }
        });
        easyTipDragView.open();
    }

}
