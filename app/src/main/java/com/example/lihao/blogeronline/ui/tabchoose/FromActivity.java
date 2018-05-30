package com.example.lihao.blogeronline.ui.tabchoose;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.adapter.MyBasePageAdapter;
import com.example.lihao.blogeronline.base.BaseActivity;
import com.example.lihao.blogeronline.bean.From;
import com.example.lihao.blogeronline.http.MyApi;
import com.example.lihao.blogeronline.http.MyNetwork;
import com.example.lihao.blogeronline.utils.SPUtils;
import com.example.lihao.blogeronline.utils.UIUtils;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2018/4/16.
 */

public class FromActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tab_progress)
    ProgressBar tabProgress;
    @BindView(R.id.lv_froms)
    ListView lvFrom;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.btn_sure)
    Button btnSure;


    private Intent intent;
    ArrayList<From> mFroms;

    public int from_id = 0;
    private int index;

    @Override
    protected void initView() {

        btnCancel.setOnClickListener(this);
        btnSure.setOnClickListener(this);
    }

    @Override
    protected void doBeforeSetContentView() {
        intent = getIntent();
        super.doBeforeSetContentView();
        from_id = SPUtils.getSharedIntData(this,"from_id",1);
    }

    @Override
    protected int setLayout() {
        return R.layout.activity_from;
    }

    @Override
    protected void setPresenter() {

        //加载数据
        MyApi api = MyNetwork.getInstance().getApi(UIUtils.getContext());
        api.getFroms().compose(MyNetwork.schedulersTransformer)
                .subscribe(new Observer<ArrayList<From>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ArrayList<From> froms) {

                        if (froms != null) {

                            mFroms = froms;

                            initFroms(froms);

                            saveFroms(froms);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        tabProgress.setVisibility(View.GONE);
                        lvFrom.setVisibility(View.VISIBLE);
                        btnCancel.setVisibility(View.VISIBLE);
                        btnSure.setVisibility(View.VISIBLE);

                    }
                });
    }

    private void saveFroms(ArrayList<From> froms) {

        Gson gson = new Gson();
        String toJson = gson.toJson(froms);
        SPUtils.setSharedStringData(this, "froms", toJson);
    }

    private void initFroms(ArrayList<From> froms) {

        final MyListAdapter myListAdapter = new MyListAdapter(froms);
        lvFrom.setAdapter(myListAdapter);
        lvFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                from_id = myListAdapter.getItem(position).getFid();
                index = position;
                myListAdapter.notifyDataSetChanged();

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btn_cancel :

                setResult(2);
                finish();
                break;
            case R.id.btn_sure :
                saveSureFroms();
                break;

        }


    }

    private void saveSureFroms() {

        SPUtils.setSharedIntData(this,"from_id", mFroms.get(index).getFid());
        SPUtils.setSharedStringData(this,"base_url",mFroms.get(index).getF_url());
        SPUtils.setSharedStringData(this,"containTabs","");

        setResult(2);
        finish();

    }

    class MyListAdapter extends BaseAdapter {

        ArrayList<From> mFroms;

        public MyListAdapter(ArrayList<From> froms){
            this.mFroms = froms;
        }

        @Override
        public int getCount() {
            return mFroms.size();
        }

        @Override
        public From getItem(int position) {
            return mFroms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mFroms.get(position).getFid();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = null;
            final ViewHolder holder;

            if(convertView == null){

                view = View.inflate(UIUtils.getContext(),R.layout.item_from,null);

                holder = new ViewHolder();

                holder.iv_choosed = view.findViewById(R.id.iv_choosed);
                holder.tv_name = view.findViewById(R.id.tv_name);

                convertView = view;

                convertView.setTag(holder);

            } else {

                holder = (ViewHolder) convertView.getTag();
            }

            //更新
            holder.tv_name.setText(getItem(position).getF_name());
            if(getItem(position).getFid() == from_id){
                holder.iv_choosed.setVisibility(View.VISIBLE);
            }else {
                holder.iv_choosed.setVisibility(View.INVISIBLE);
            }

            return convertView;
        }
    }


    static class ViewHolder {

        ImageView iv_choosed;

        TextView tv_name;
    }

}
