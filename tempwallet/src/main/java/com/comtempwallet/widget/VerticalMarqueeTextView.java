package com.comtempwallet.widget;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.comtempwallet.R;
import com.example.skn.framework.util.AppUtil;

import java.util.List;

/**
 * Created by DIY on 2017/5/23.
 */

public class VerticalMarqueeTextView extends LinearLayout {

    private Context mContext;
    private List<String> strs;
    private View mView;

    private OnTextClickListener mOnTextClickListener;
    private ViewFlipper mViewFlipper;


    public VerticalMarqueeTextView(Context context) {
        this(context, null);
        this.mContext = context;
    }

    public VerticalMarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    /**
     * 用于外界向里面传值,并且初始化控件中的ViewFipper
     *
     * @param str
     * @param onTextClickListener
     */
    public void setData(List<String> str, OnTextClickListener onTextClickListener) {
        this.strs = str;
        this.mOnTextClickListener = onTextClickListener;
        initViewFipper();
    }


    private ViewFlipper initBasicView() {
        if (getChildCount() > 0) {
            removeAllViews();
        }
        mView = LayoutInflater.from(mContext).inflate(R.layout.layout_viewfipper, null);
        mViewFlipper = (ViewFlipper) mView.findViewById(R.id.viewFlipper);

        mViewFlipper.setInAnimation(mContext, R.anim.flipper_in);  //进来的动画
        mViewFlipper.setOutAnimation(mContext, R.anim.flipper_out);  //出去的动画

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(mView, params);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(3000);
        mViewFlipper.startFlipping();
        return mViewFlipper;
    }

    /**
     * 定义销毁的方法
     */
    public void clearViewFlipper() {
        if (mView != null) {
            if (mViewFlipper != null) {
                mViewFlipper.stopFlipping();
                mViewFlipper.removeAllViews();
                mViewFlipper = null;
            }
            mView = null;
        }
    }


    /**
     * 初始化viewFipper中的自孩子视图
     */
    private void initViewFipper() {
        if (strs == null || strs.size() == 0) {
            return;
        }
        mViewFlipper = initBasicView();
        int i = 0;
        while (i < strs.size()) {   //循环
            final TextView tv = new TextView(mContext);
            int index = strs.get(i).lastIndexOf("款");
            SpannableStringBuilder builder = new SpannableStringBuilder(strs.get(i));
            if (index > 0) {
                builder.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.borrow_color)), index + 1, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            if (builder.length() > 6) {
                builder.setSpan(new UnderlineSpan(), 2, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            tv.setText(builder);
            tv.setTextSize(12f);
            tv.setPadding(0, AppUtil.dip2px(8), 0, AppUtil.dip2px(8));
            tv.setSingleLine();
            tv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnTextClickListener != null) {
                        mOnTextClickListener.onClick(tv);
                    }
                }
            });
            mViewFlipper.addView(tv);
            i++;
        }
    }

    public interface OnTextClickListener {
        void onClick(TextView tv);
    }

}