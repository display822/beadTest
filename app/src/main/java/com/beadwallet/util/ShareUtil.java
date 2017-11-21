package com.beadwallet.util;

import com.example.skn.framework.base.BaseActivity;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.beadwallet.Application;
import com.beadwallet.R;

/**
 * Created by DIY on 2017/9/9.
 */

public class ShareUtil {

    public static void ShareWeb(BaseActivity mActivity) {
        UMImage umImage = new UMImage(mActivity, R.drawable.ic_logo);
        UMWeb web = new UMWeb(Application.getShareUrl());
        web.setThumb(umImage);
        web.setTitle("水珠钱包-专注个人无抵押信用小额贷款的服务平台");
        web.setDescription("水珠钱包专注个人小额贷款，安全的一站式服务，快速满足您的资金需求");
        new ShareAction(mActivity)
                .withMedia(web)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.WEIXIN_FAVORITE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .setCallback(new ShareListener())
                .open();
    }


}
