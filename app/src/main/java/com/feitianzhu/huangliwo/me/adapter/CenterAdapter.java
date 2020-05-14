package com.feitianzhu.huangliwo.me.adapter;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.me.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/19 0019 下午 7:27
 */
public class CenterAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {

    public CenterAdapter(List<Integer> list) {
        super(R.layout.center_img_item, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        ImageView imageView = helper.getView(R.id.image);
        loadImg(item, imageView);
        TextView textView = helper.getView(R.id.name);
        /*if (helper.getAdapterPosition() == 8) {
            helper.getView(R.id.item).setVisibility(View.INVISIBLE);
        } else {
            helper.getView(R.id.item).setVisibility(View.VISIBLE);
        }*/
        switch (helper.getAdapterPosition()) {
            case 0:
                textView.setText("购物车");
                break;
            case 1:
                textView.setText("地址管理");
                break;
            case 2:
                textView.setText("实名认证");
                break;
            case 3:
                textView.setText("绑定账号");
                break;
            case 4:
                textView.setText("我的收藏");
                break;
            case 5:
                textView.setText("推店赚钱");
                break;
            case 6:
                textView.setText("分享");
                break;
            case 7:
                textView.setText("我的团队");
                break;
            case 8:
                textView.setText("帮助中心");
                break;
        }
    }

    private void loadImg(int resid, ImageView imageView) {
        Glide.with(mContext).load(resid).apply(RequestOptions.placeholderOf(R.mipmap.pic_fuwutujiazaishibai)).into(imageView);
    }
}
