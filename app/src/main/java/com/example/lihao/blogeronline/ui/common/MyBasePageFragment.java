package com.example.lihao.blogeronline.ui.common;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.adapter.MyBasePageAdapter;
import com.example.lihao.blogeronline.app.Constants;
import com.example.lihao.blogeronline.app.UserManager;
import com.example.lihao.blogeronline.base.BaseFragment;
import com.example.lihao.blogeronline.bean.Article;
import com.example.lihao.blogeronline.data.model.UploadArticle;
import com.example.lihao.blogeronline.listener.OnItemClickListener;
import com.example.lihao.blogeronline.ui.BlogDetailActivity;
import com.example.lihao.blogeronline.ui.common.presenter.BasePresenter;
import com.example.lihao.blogeronline.ui.common.presenter.imple.BasePresenterImple;
import com.example.lihao.blogeronline.ui.common.view.MyBaseView;
import com.example.lihao.blogeronline.ui.view.LoadMoreRecyclerView;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.ToastUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.wenhuaijun.easytagdragview.bean.SimpleTitleTip;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by lihao on 17-11-22.
 */

public class MyBasePageFragment extends BaseFragment implements MyBaseView<BasePresenter>, SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.OnLoadMoreListener {

    @BindView(R.id.layout_success)
    protected View pageSuuccess;
    @BindView(R.id.layout_empty)
    protected View pageEmpty;
    @BindView(R.id.layout_error)
    protected View pageError;
    @BindView(R.id.layout_loading)
    protected View pageLOading;
    @BindView(R.id.layout_unlogin)
    protected View pageUnlogin;

    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout mSwipeLayout;

    protected MyBasePageAdapter mAdapter;

    BasePresenter mPresenter;

    int page = 1;
    private SimpleTitleTip tip;
    private boolean isRecommand;
    private String url;
    private int from_id;
    private String base_url;
    private int max_items;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_page;
    }

    @Override
    protected void doSomethingBeforeSetContentView() {

        tip = (SimpleTitleTip) getArguments().getSerializable("key");
        from_id = SPUtils.getSharedIntData(UIUtils.getContext(),"from_id",1);
        base_url = SPUtils.getSharedStringData(UIUtils.getContext(),"base_url",Constants.CSDN_URL);
        max_items = SPUtils.getSharedIntData(UIUtils.getContext(),"max_page_items",30);
    }

    @Override
    protected void initView() {

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(UIUtils.getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new MyBasePageAdapter(UIUtils.getContext(), null);
        recyclerView.setAdapter(mAdapter);
        //添加Android自带的分割线
        recyclerView.addItemDecoration(new DefaultDividerDecoration());
        recyclerView.setOnLoadMoreListener(this);
        mSwipeLayout.setOnRefreshListener(this);

        mAdapter.setItemListener(new OnItemClickListener() {
            @Override
            public void onClick(int position) {

                List<Article> data = mAdapter.getData();
                Article article = data.get(position);

                UploadArticle uploadArticle = new UploadArticle();

                uploadArticle.setArticle_type_id(tip.getId());
                uploadArticle.setArticle_link(article.getLink());
                uploadArticle.setArticle_auther(article.getUsername());
                uploadArticle.setArticle_title(article.getTitle());
                uploadArticle.setArticle_from_id(from_id);

                Intent intent = new Intent(getActivity(), BlogDetailActivity.class);

                intent.putExtra("uploadArticle", uploadArticle);
                intent.putExtra("isRecommend", isRecommend());

                startActivity(intent);

            }
        });

    }

    @Override
    public void initData() {
        isRecommand = tip.getId() == -1;
        if(isRecommand){
            //加载推荐
            if(UserManager.create(UIUtils.getContext()).getIsLogin()){

                new BasePresenterImple(UIUtils.getContext(),this).getRecDataFromPage(1,from_id);
            }else {
                //请登录
                pageLOading.setVisibility(View.GONE);
                pageUnlogin.setVisibility(View.VISIBLE);
            }

        } else {
            url = tip.getUrl().replace(base_url, "");
            new BasePresenterImple(UIUtils.getContext(), this).getDataFromPage(url,1);
        }

    }

    @Override
    public void setPresenter(BasePresenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void startLoadMore() {

        mAdapter.setStatus(MyBasePageAdapter.LoadStatus.IS_LOAD_MORE);
        mAdapter.updateFooterView();

    }

    @Override
    public void infoLoaded(ArrayList<Article> s) {
        recyclerView.setLoading(false);
        if (s.isEmpty()) {

            pageEmpty.setVisibility(View.VISIBLE);
            pageLOading.setVisibility(View.GONE);
            pageError.setVisibility(View.GONE);
            mAdapter.setStatus(MyBasePageAdapter.LoadStatus.IS_NO_MORE);

        } else {
            pageEmpty.setVisibility(View.GONE);
            pageLOading.setVisibility(View.GONE);
            pageError.setVisibility(View.GONE);
            pageSuuccess.setVisibility(View.VISIBLE);

            if(s.size() < max_items){
                mAdapter.setStatus(MyBasePageAdapter.LoadStatus.IS_NO_MORE);
            }else {
                mAdapter.setStatus(MyBasePageAdapter.LoadStatus.NORMAL);
            }
            mAdapter.addData(s);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.updateFooterView();

    }

    @Override
    public void loadMoreFinished() {

    }

    @Override
    public void refreshed(ArrayList<Article> s) {

        mSwipeLayout.setRefreshing(false);

        if (s.isEmpty()) {

            pageEmpty.setVisibility(View.VISIBLE);
            pageLOading.setVisibility(View.GONE);
            pageError.setVisibility(View.GONE);
            mAdapter.setStatus(MyBasePageAdapter.LoadStatus.IS_NO_MORE);

        } else {

            pageEmpty.setVisibility(View.GONE);
            pageLOading.setVisibility(View.GONE);
            pageError.setVisibility(View.GONE);
            pageSuuccess.setVisibility(View.VISIBLE);

            if(s.size() < 30){
                mAdapter.setStatus(MyBasePageAdapter.LoadStatus.IS_NO_MORE);
            }else {
                mAdapter.setStatus(MyBasePageAdapter.LoadStatus.NORMAL);
            }

            mAdapter.setData(s);
            mAdapter.notifyDataSetChanged();
        }
        mAdapter.updateFooterView();
    }

    @Override
    public void handError(Throwable e) {
        ToastUtils.showToast(UIUtils.getContext(), e.getMessage());
    }

    @Override
    public void emptyView() {
        mSwipeLayout.setRefreshing(false);

        ToastUtils.showToast(getActivity(), "没有更多了");
        pageLOading.setVisibility(View.GONE);
        pageError.setVisibility(View.GONE);
        pageSuuccess.setVisibility(View.GONE);
    }

    @Override
    public void interNetException() {
        mSwipeLayout.setRefreshing(false);
        pageEmpty.setVisibility(View.GONE);
        pageLOading.setVisibility(View.GONE);
        pageError.setVisibility(View.VISIBLE);
        pageSuuccess.setVisibility(View.GONE);
    }

    @Override
    public void loadingView() {
        mSwipeLayout.setRefreshing(false);

        pageLOading.setVisibility(View.VISIBLE);
        pageEmpty.setVisibility(View.GONE);
        pageError.setVisibility(View.GONE);
        pageSuuccess.setVisibility(View.GONE);
    }

    @Override
    public void onRefresh() {
        mPresenter.getRefresh(isRecommand,url,from_id);
    }

    @Override
    public void onLoad() {

        if(mAdapter.getStatus() == MyBasePageAdapter.LoadStatus.NORMAL){
            page++;
            if(isRecommand){
                //推荐
                mPresenter.getRecDataFromPage(page,from_id);
            }else {
                mPresenter.getDataFromPage(url,page);
            }
        }
    }

    public boolean isRecommend() {
        return isRecommand;
    }
}
