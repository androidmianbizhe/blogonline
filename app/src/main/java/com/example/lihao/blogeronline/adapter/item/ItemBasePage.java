package com.example.lihao.blogeronline.adapter.item;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lihao.blogeronline.R;
import com.example.lihao.blogeronline.adapter.IAdapterView;
import com.example.lihao.blogeronline.bean.Article;

/**
 * Created by lihao on 17-11-21.
 */

public class ItemBasePage extends LinearLayout implements IAdapterView<Article> {

    private TextView tvTitle;
    private TextView tvTime;
    private TextView tvUsername;


    public ItemBasePage(Context context) {
        super(context);

        View view = View.inflate(context,R.layout.item_base_page,this);

        //findViewById
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tvUsername = (TextView) view.findViewById(R.id.tv_username);

    }

    @Override
    public void bind(Article item, int position) {

        //推荐
        if(item.getType_id() == -1){
            tvTitle.setText("["+item.getType()+"]"+item.getTitle());
        }else {
            tvTitle.setText(item.getTitle());
        }

        tvTime.setText(item.getTime());
        tvUsername.setText(item.getUsername());

    }
}
