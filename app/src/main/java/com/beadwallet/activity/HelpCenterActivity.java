package com.beadwallet.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.skn.framework.base.BaseActivity;
import com.example.skn.framework.http.Api;
import com.example.skn.framework.http.RequestCallBack;
import com.example.skn.framework.dialog.DialogUtil;
import com.example.skn.framework.util.ToastUtil;
import com.example.skn.framework.util.ToolBarUtil;
import com.beadwallet.R;
import com.beadwallet.adapter.MyBaseExpandableAdapter;
import com.beadwallet.adapter.ViewHolder;
import com.beadwallet.entity.HelpCenterBean;
import com.beadwallet.util.UrlService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 帮助中心
 */
public class HelpCenterActivity extends BaseActivity {

    private ExpandableListView listView;
    private MyBaseExpandableAdapter<HelpCenterBean, HelpCenterBean.ContentListBean> adapter;
    private List<HelpCenterBean> dataList = new ArrayList<>();

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, HelpCenterActivity.class));
    }


    @Override
    protected void initVar() {
        setContentView(R.layout.activity_help_center);
    }

    @Override
    protected void init() {
        ToolBarUtil.getInstance(this).setTitle("帮助中心").build();

        listView = (ExpandableListView) findViewById(R.id.listView);
        adapter = new MyBaseExpandableAdapter<HelpCenterBean, HelpCenterBean.ContentListBean>(this, dataList, R.layout.item_help_center, R.layout.item_item_help_center) {
            @Override
            public HelpCenterBean.ContentListBean getChild(int groupPosition, int childPosition) {
                return dataList.get(groupPosition).getContentList().get(childPosition);
            }

            @Override
            public void bindGroupData(ViewHolder viewHolder, HelpCenterBean item, int groupPosition, boolean isExpanded) {
                viewHolder.setText(R.id.tv_item_title, item.getChannel());
                listView.expandGroup(groupPosition);
            }

            @Override
            public void bindChildData(ViewHolder viewHolder, HelpCenterBean.ContentListBean item, int groupPosition, int childPosition) {
                viewHolder.setText(R.id.tv_item, (childPosition + 1) + ". " + item.getTitle());
                TextView tv_item = viewHolder.getView(R.id.tv_item);
                TextView tv_item_detail = viewHolder.getView(R.id.tv_item_detail);
                tv_item_detail.setText(item.getDetail());
                Drawable drawable1 = getResources().getDrawable(R.drawable.ic_spinner_hide);
                drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
                tv_item.setCompoundDrawables(null, null, drawable1, null);
                viewHolder.getView(R.id.tv_item).setOnClickListener(view -> {
                    if (!tv_item.isSelected()) {
                        tv_item.setSelected(true);
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_spinner_show);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        tv_item.setCompoundDrawables(null, null, drawable, null);
                        tv_item_detail.setVisibility(View.VISIBLE);
                    } else {
                        tv_item.setSelected(false);
                        tv_item_detail.setVisibility(View.GONE);
                        Drawable drawable = getResources().getDrawable(R.drawable.ic_spinner_hide);
                        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                        tv_item.setCompoundDrawables(null, null, drawable, null);
                    }
                });
            }

            @Override
            public int getChildrenCount(int i) {
                return dataList.get(i).getContentList().size();
            }
        };
        listView.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        DialogUtil.showLoading(this);
        Api.getDefault(UrlService.class).getHelpCenter(new HashMap<>()).compose(Api.handlerResult())
                .subscribe(new RequestCallBack<List<HelpCenterBean>>(mActivity,true) {
                    @Override
                    public void onSuccess(List<HelpCenterBean> helpCenterBeen) {
                        dataList.clear();
                        if (helpCenterBeen != null) {
                            dataList.addAll(helpCenterBeen);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String code, String errorMsg) {
                    }
                });

    }
}
