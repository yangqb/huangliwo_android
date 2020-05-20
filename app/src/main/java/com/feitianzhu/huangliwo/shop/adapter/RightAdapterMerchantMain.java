package com.feitianzhu.huangliwo.shop.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.BaseGoodsListBean;
import com.feitianzhu.huangliwo.pushshop.bean.MerchantsModel;
import com.feitianzhu.huangliwo.shop.model.MerchantBean;
import com.feitianzhu.huangliwo.shop.model.ShopClassifyBean;

import java.util.List;

/**
 * @class name：com.feitianzhu.fu700.shop.adapter
 * @anthor yangqinbo
 * @email QQ:694125155
 * @Date 2019/11/20 0020 下午 2:53
 */
public class RightAdapterMerchantMain extends BaseQuickAdapter<MerchantBean, BaseViewHolder> {
    public Callback callback;

    public RightAdapterMerchantMain(List<MerchantBean> data) {
        super(R.layout.shop_right_item4, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MerchantBean item) {
        LinearLayout view = helper.getView(R.id.hot);
        if (item.hot != null && item.hot.size() > 0) {
            view.setVisibility(View.VISIBLE);
            helper.setText(R.id.title1, "热门商铺");
            RightAdapterMerchantChild rightAdapterShopChild = new RightAdapterMerchantChild(item.hot);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3);
            RecyclerView hotView = helper.getView(R.id.recyclerView);
            hotView.setLayoutManager(gridLayoutManager);
            hotView.setAdapter(rightAdapterShopChild);
            rightAdapterShopChild.notifyDataSetChanged();
            rightAdapterShopChild.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    MerchantsModel baseGoodsListBean = item.hot.get(position);
                    setMerchant(baseGoodsListBean);
                }
            });
        } else {
            view.setVisibility(View.GONE);
        }

        View view1 = helper.getView(R.id.boutique);
        if (item.veryGood != null && item.veryGood.size() > 0) {
            view1.setVisibility(View.VISIBLE);
            helper.setText(R.id.title2, "优质商家");
            RightAdapterMerchantChild rightAdapterBoutique = new RightAdapterMerchantChild(item.veryGood);
            GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 3);
            RecyclerView boutiqueView = helper.getView(R.id.recyclerView1);
            boutiqueView.setLayoutManager(gridLayoutManager1);
            boutiqueView.setAdapter(rightAdapterBoutique);
            rightAdapterBoutique.notifyDataSetChanged();
            rightAdapterBoutique.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    MerchantsModel baseGoodsListBean = item.veryGood.get(position);
                    setMerchant(baseGoodsListBean);

                }
            });
        } else {
            view1.setVisibility(View.GONE);

        }

        View view2 = helper.getView(R.id.recommend);

        if (item.recommendFor != null && item.recommendFor.size() > 0) {
            view2.setVisibility(View.VISIBLE);
            helper.setText(R.id.title3, "为您推荐");
            RightAdapterMerchantChild rightAdapterShopChild1 = new RightAdapterMerchantChild(item.recommendFor);
            GridLayoutManager gridLayoutManager2 = new GridLayoutManager(mContext, 3);
            RecyclerView recommendForView = helper.getView(R.id.recyclerView2);
            recommendForView.setLayoutManager(gridLayoutManager2);
            recommendForView.setAdapter(rightAdapterShopChild1);
            rightAdapterShopChild1.notifyDataSetChanged();
            rightAdapterShopChild1.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    MerchantsModel baseGoodsListBean = item.recommendFor.get(position);
                    setMerchant(baseGoodsListBean);

                }
            });
        } else {
            view2.setVisibility(View.GONE);

        }

    }

    private void setMerchant(MerchantsModel baseGoodsListBean) {
        if (callback != null) {
            callback.callBack(baseGoodsListBean);
        }
    }


    public interface Callback {
        public void callBack(MerchantsModel baseGoodsListBean);
    }
}
