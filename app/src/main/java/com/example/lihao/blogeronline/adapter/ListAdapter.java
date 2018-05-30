package com.example.lihao.blogeronline.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lihao.blogeronline.listener.OnItemClickListener;
import com.example.lihao.blogeronline.listener.OnItemLongClickListener;

import java.util.List;

/**
 * Created by lihao on 17-10-30.
 */

public abstract class ListAdapter<T,V extends IAdapterView> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected Context mContext;

    protected List<T> mData;


    protected OnItemClickListener mItemListener;

    protected OnItemLongClickListener mItemLongClickListener;

    public ListAdapter(Context context,List<T> data){
        this.mContext = context;
        this.mData = data;
    }


    public abstract V createView(Context mContext);


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = (View) createView(mContext);
        final RecyclerView.ViewHolder holder = new RecyclerView.ViewHolder(itemView) {};
        //点击监听
        if(mItemListener != null){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mItemListener.onClick(position);
                    }
                }
            });
        }
        //长按监听
        if(mItemLongClickListener != null){
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = holder.getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        mItemLongClickListener.onLongClick(position);
                    }
                    return false;
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        IAdapterView itemView = (V) holder.itemView;
        itemView.bind(getItem(position),position);
    }

    public T getItem(int position){
        return mData.get(position);
    }

    @Override
    public int getItemCount() {
        if(mData == null) return 0;
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData(){
        return mData;
    }

    public void setData(List<T> data){
        mData = data;
    }

    public void addData(List<T> data){
        if(mData == null){
            mData = data;
        }else {
            mData.addAll(data);
        }
    }

    public void clearData(){
        if(mData == null){
            mData.clear();
        }
    }

    public void setItemListener(OnItemClickListener mItemListener) {
        this.mItemListener = mItemListener;
    }

    public void setItemLongClickListener(OnItemLongClickListener mItemLongClickListener) {
        this.mItemLongClickListener = mItemLongClickListener;
    }
}
