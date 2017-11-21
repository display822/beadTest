package com.example.skn.framework.util;

import android.content.Context;
import android.os.Looper;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Created by DOY on 2017/7/20.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private Thread.UncaughtExceptionHandler defaultHandler;
    private static CrashHandler instance;
    private Context context;

    public static CrashHandler getInstance(Context context) {
        if (instance == null)
            instance = new CrashHandler(context);
        return instance;
    }

    private CrashHandler(Context context) {
        this.context = context;
        defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable == null && defaultHandler != null) {
            defaultHandler.uncaughtException(thread, null);
        } else {
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    ToastUtil.show("不好意思，程序炸了！");
                    Looper.loop();
                }
            }.start();
            if (throwable != null) {
                Writer writer = new StringWriter();
                PrintWriter printWriter = new PrintWriter(writer);
                throwable.printStackTrace(printWriter);
                Throwable cause = throwable.getCause();
                while (cause != null) {
                    cause.printStackTrace(printWriter);
                    cause = cause.getCause();
                }
                printWriter.close();
                String result = writer.toString();
                Log.e("CrashHandler", result);
            }
            new Thread(() -> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    AppUtil.destroyApp();
                }
            }).start();


        }
    }
}
