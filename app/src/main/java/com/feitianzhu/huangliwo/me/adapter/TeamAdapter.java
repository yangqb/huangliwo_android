package com.feitianzhu.huangliwo.me.adapter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.TeamModel;
import com.feitianzhu.huangliwo.utils.DateUtils;
import com.feitianzhu.huangliwo.utils.MathUtils;
import com.feitianzhu.huangliwo.view.CircleImageView;

import java.util.List;

/**
 * package name: com.feitianzhu.huangliwo.me.adapter
 * user: yangqinbo
 * date: 2020/3/7
 * time: 17:32
 * email: 694125155@qq.com
 */
public class TeamAdapter extends BaseQuickAdapter<TeamModel, BaseViewHolder> {
    public TeamAdapter(@Nullable List<TeamModel> data) {
        super(R.layout.item_team, data);
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, TeamModel item) {
        Glide.with(mContext).load(item.headImg).apply(new RequestOptions().error(R.mipmap.b08_01touxiang).placeholder(R.mipmap.b08_01touxiang)).into((CircleImageView) helper.getView(R.id.head));
        helper.setText(R.id.name, item.nickName);
        helper.setText(R.id.time, "注册时间：" + DateUtils.getFormatedDateTime("yyyy-MM-dd", item.registeDate));
        helper.setText(R.id.recommendCount, "直推会员：" + item.subordinateCount);
        helper.setText(R.id.contributionAmount, "贡献收入：¥" + MathUtils.subZero(String.valueOf(item.totalConsume)));
        helper.setText(R.id.phone, "电话：" + item.phone);
        if (item.accountType == 0) {
            helper.setText(R.id.grade, "消费者");
        } else if (item.accountType == 3) {
            helper.setText(R.id.grade, "");
//            helper.setText(R.id.grade, "合伙人");
        } else if (item.accountType == 4) {
            helper.setText(R.id.grade, "超级会员");
        } else if (item.accountType == 5) {
            helper.setText(R.id.grade, "优选会员");
        } else {
            helper.setText(R.id.grade, "");
        }
    }
}
