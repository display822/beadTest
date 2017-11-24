package com.dreamwallet.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**

 *
 */
public class TimeButton extends TextView implements OnClickListener {
    private int count = 60;
    private OnLoadDataListener onLoadDataListener;

    public void setOnLoadDataListener(OnLoadDataListener onLoadDataListener) {
        this.onLoadDataListener = onLoadDataListener;
    }

    public TimeButton(Context context) {
        this(context, null);
    }

    public TimeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setText("获取验证码");
        this.setOnClickListener(this);
    }

    public void time() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(count + 1)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return count - aLong;
                    }
                }).map(new Func1<Long, String>() {
            @Override
            public String call(Long aLong) {
                return aLong + "秒";
            }
        }).subscribeOn(Schedulers.computation())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        TimeButton.this.setEnabled(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        TimeButton.this.setEnabled(true);
                        TimeButton.this.setText("获取验证码");
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        TimeButton.this.setText(s);
                    }
                });
    }

    @Override
    public void onClick(View view) {
        onLoadDataListener.load(TimeButton.this);
    }

    public interface OnLoadDataListener

    {
        void load(TimeButton timeButton);
    }
}