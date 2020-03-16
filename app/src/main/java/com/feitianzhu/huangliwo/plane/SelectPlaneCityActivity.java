package com.feitianzhu.huangliwo.plane;

import android.widget.Toast;

import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.model.CityModel;
import com.cretin.tools.cityselect.view.CitySelectView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * package name: com.feitianzhu.huangliwo.plane
 * user: yangqinbo
 * date: 2020/3/10
 * time: 17:57
 * email: 694125155@qq.com
 */
public class SelectPlaneCityActivity extends BaseActivity {
    @BindView(R.id.city_view)
    CitySelectView citySelectView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void initView() {
        //设置所有城市数据
        final List<CityModel> allCitys = new ArrayList<>();
        allCitys.add(new CityModel("深圳", "10000000"));
        allCitys.add(new CityModel("深圳", "10000000"));
        allCitys.add(new CityModel("深圳", "10000000"));
        allCitys.add(new CityModel("深圳", "10000000"));
        //设置热门城市列表 这都是瞎写的 哈哈哈
        final List<CityModel> hotCitys = new ArrayList<>();
        hotCitys.add(new CityModel("深圳", "10000000"));
        hotCitys.add(new CityModel("广州", "10000001"));
        hotCitys.add(new CityModel("北京", "10000002"));
        hotCitys.add(new CityModel("天津", "10000003"));
        hotCitys.add(new CityModel("武汉", "10000004"));

        //设置当前城市数据
        final CityModel currentCity = new CityModel("深圳", "10000000");
        //绑定数据到视图 需要 所有城市列表 热门城市列表 和 当前城市列表 其中 所有城市列表是必传的 热门城市和当前城市是选填的 不传就不会显示对应的视图
        citySelectView.bindData(allCitys, hotCitys, currentCity);
        //设置搜索框的文案提示
        citySelectView.setSearchTips("请输入城市名称或者拼音");

        //设置城市选择之后的事件监听
        citySelectView.setOnCitySelectListener(new OnCitySelectListener() {
            @Override
            public void onCitySelect(CityModel cityModel) {
                Toast.makeText(SelectPlaneCityActivity.this, "你点击了：" + cityModel.getCityName() + ":" + cityModel.getExtra().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSelectCancel() {
                Toast.makeText(SelectPlaneCityActivity.this, "你取消了城市选择", Toast.LENGTH_SHORT).show();
            }
        });

        //设置点击重新定位之后的事件监听
        citySelectView.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onLocation() {
                //这里模拟定位 两秒后给个随便的定位数据
                citySelectView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        citySelectView.reBindCurrentCity(new CityModel("广州", "10000001"));
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData() {

    }
}
