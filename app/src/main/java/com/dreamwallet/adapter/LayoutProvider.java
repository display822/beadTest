package com.dreamwallet.adapter;

/**
 * Created by DB on 2016/5/18 0018.
 */
public interface LayoutProvider<T> {

    int getLayout(int type);

    int getLayoutType(int position, T t);

    int getLayoutCount();
}