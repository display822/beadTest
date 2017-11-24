package com.bead.dreamwallet.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bead.dreamwallet.R;

/**
 * Created by DOY on 2017/7/19.
 */
public class DialogHelper {

    private Context context;
    private String title;
    private String msg;
    private String sureText;
    private String cancelText;
    private OnClickListener sureListener;
    private OnClickListener cancelListener;
    private boolean flag;

    private DialogHelper(Context context, String title, String msg, String sureText, String cancelText, OnClickListener sureListener, OnClickListener cancelListener, boolean flag) {
        this.context = context;
        this.title = title;
        this.msg = msg;
        this.sureText = sureText;
        this.cancelText = cancelText;
        this.sureListener = sureListener;
        this.cancelListener = cancelListener;
        this.flag = flag;
    }

    public static class Builder {
        private Context context;
        private String title;
        private String msg;
        private String sureText;
        private String cancelText;
        private OnClickListener sureListener;
        private OnClickListener cancelListener;
        private boolean flag;

        public Builder setFlag(boolean flag) {
            this.flag = flag;
            return this;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public Builder setSureText(String sureText) {
            this.sureText = sureText;
            return this;
        }

        public Builder setCancelText(String cancelText) {
            this.cancelText = cancelText;
            return this;
        }

        public Builder setSureListener(OnClickListener sureListener) {
            this.sureListener = sureListener;
            return this;
        }

        public Builder setCancelListener(OnClickListener cancelListener) {
            this.cancelListener = cancelListener;
            return this;
        }

        public DialogHelper build() {
            return new DialogHelper(context, title, msg, sureText, cancelText, sureListener, cancelListener, flag);
        }
    }

    public Dialog showCommentSuccess() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_comment_success, null);
        final Dialog dialog = new Dialog(context, com.example.skn.framework.R.style.current_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(flag);
        dialog.show();
        return dialog;
    }

    public Dialog showIconDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_icon, null);
        final Dialog dialog = new Dialog(context, com.example.skn.framework.R.style.current_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(flag);
        ImageView imgClose = view.findViewById(R.id.img_close);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        TextView tvSure = view.findViewById(R.id.tv_sure);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        imgClose.setOnClickListener(view1 -> dialog.dismiss());
        tvMsg.setText(msg);
        tvSure.setText(sureText);
        tvCancel.setText(cancelText);
        tvSure.setOnClickListener(v -> {
            if (sureListener != null) sureListener.onclick(dialog, v);
        });
        tvCancel.setOnClickListener(v -> {
            if (cancelListener != null) cancelListener.onclick(dialog, v);
        });
        dialog.show();
        return dialog;
    }

    public Dialog showTitleDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_title, null);
        final Dialog dialog = new Dialog(context, com.example.skn.framework.R.style.current_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(flag);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        ImageView imgClose = view.findViewById(R.id.img_close);
        TextView tvMsg = view.findViewById(R.id.tv_msg);
        TextView tvSure = view.findViewById(R.id.tv_sure);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        imgClose.setOnClickListener(view1 -> dialog.dismiss());
        tvTitle.setText(title);
        tvMsg.setText(msg);
        tvSure.setText(sureText);
        tvCancel.setText(cancelText);
        tvSure.setOnClickListener(v -> {
            if (sureListener != null) sureListener.onclick(dialog, v);
        });
        tvCancel.setOnClickListener(v -> {
            if (cancelListener != null) cancelListener.onclick(dialog, v);
        });
        dialog.show();
        return dialog;
    }

    public Dialog showTitleViewDialog() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_title_view, null);
        final Dialog dialog = new Dialog(context, com.example.skn.framework.R.style.current_dialog);
        dialog.setContentView(view);
        dialog.setCancelable(flag);
        TextView tvTitle = view.findViewById(R.id.tv_title);
        ImageView imgClose = view.findViewById(R.id.img_close);
        TextView tvSure = view.findViewById(R.id.tv_sure);
        TextView tvCancel = view.findViewById(R.id.tv_cancel);
        imgClose.setOnClickListener(view1 -> dialog.dismiss());
        tvTitle.setText(title);
        tvSure.setText(sureText);
        tvCancel.setText(cancelText);
        tvSure.setOnClickListener(v -> {
            if (sureListener != null) sureListener.onclick(dialog, v);
        });
        tvCancel.setOnClickListener(v -> {
            if (cancelListener != null) cancelListener.onclick(dialog, v);
        });

        TextView tvSubmited = view.findViewById(R.id.tv_submited);
        TextView tvNoApply = view.findViewById(R.id.tv_no_apply);
        tvSubmited.setOnClickListener(v -> {
            tvSubmited.setSelected(true);
            tvNoApply.setSelected(false);
        });
        tvNoApply.setOnClickListener(v -> {
            tvNoApply.setSelected(true);
            tvSubmited.setSelected(false);
        });
        dialog.show();
        return dialog;
    }

    public interface OnClickListener {
        void onclick(Dialog dialog, View view);
    }
}
