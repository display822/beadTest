package com.example.skn.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewStubCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.skn.framework.R;

/**
 * Created by hf
 */
abstract public class BaseFragment extends Fragment {


    protected AppCompatActivity mActivity;
    private View mEmptyView;
    private ViewStubCompat mEmptyStub;
    private View.OnClickListener onClickListener;
    private boolean isInitEmptyView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (BaseActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }


    public void initEmptyOrNetErrorView(View rootView, int emptyOrNetErrorRes, View.OnClickListener onClickListener) {
        View emptyView = rootView.findViewById(emptyOrNetErrorRes);
        if (emptyView instanceof ViewStubCompat) {
            mEmptyStub = (ViewStubCompat) emptyView;
        } else {
            mEmptyView = emptyView;
        }
        this.onClickListener = onClickListener;
        isInitEmptyView = true;
    }

    public void updateEmptyOrNetErrorView(boolean hasData, boolean network) {
        if (isInitEmptyView) {
            if (!hasData) {
                if (mEmptyView == null && mEmptyStub != null) {
                    mEmptyView = mEmptyStub.inflate();
                }
                if (mEmptyView != null) {
                    View ll_net_error = mEmptyView.findViewById(R.id.ll_net_error);
                    View empty = mEmptyView.findViewById(R.id.tv_empty);
                    if (!network) {
                        View viewById = mEmptyView.findViewById(R.id.tv_reload);
                        if (viewById != null) {
                            viewById.setOnClickListener(onClickListener);
                        }
                        ll_net_error.setVisibility(View.VISIBLE);
                        empty.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                        ll_net_error.setVisibility(View.GONE);
                    }
                    mEmptyView.setVisibility(View.VISIBLE);

                }
            } else {
                if (mEmptyView != null) {
                    mEmptyView.setVisibility(View.GONE);
                }
            }
        }
    }


}
