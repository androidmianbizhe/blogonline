package com.example.lihao.blogeronline.adapter;

/**
 * Created by lihao on 17-10-30.
 */

public interface IAdapterView<T> {
    void bind(T item, int position);
}
