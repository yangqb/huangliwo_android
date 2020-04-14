package com.feitianzhu.huangliwo.plane;

import android.content.Intent;

import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.model.CityModel;
import com.cretin.tools.cityselect.view.CitySelectView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CustomCityModel;
import com.feitianzhu.huangliwo.model.CustomPhoneCodeModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * package name: com.feitianzhu.huangliwo.plane
 * user: yangqinbo
 * date: 2020/4/14
 * time: 11:20
 * email: 694125155@qq.com
 */
public class SelectPhoneCodeActivity extends BaseActivity {
    private String cnJson;
    private List<CustomPhoneCodeModel> cnStatusLs = new ArrayList<>();
    private List<CityModel> cnAllCitys = new ArrayList<>();
    private List<CityModel> cnHotCitys = new ArrayList<>();
    private CityModel cnCurrentCity;
    public static final String CITY_DATA = "CITY_DATA";
    @BindView(R.id.cn_city_view)
    CitySelectView cnCitySelectView;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_country_code;
    }

    @Override
    protected void initView() {
        try {
            cnJson = readString(mContext.getAssets().open("area_phone_code.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置城市选择之后的事件监听
        cnCitySelectView.setOnCitySelectListener(new OnCitySelectListener() {
            @Override
            public void onCitySelect(CityModel cityModel) {
                //Toast.makeText(SelectPlaneCityActivity.this, "你点击了：" + cityModel.getCityName() + ":" + cityModel.getExtra().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra(CITY_DATA, cityModel);
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void onSelectCancel() {
                //Toast.makeText(SelectPlaneCityActivity.this, "你取消了城市选择", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });

        //设置点击重新定位之后的事件监听
        cnCitySelectView.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onLocation() {
                //这里模拟定位 两秒后给个随便的定位数据
                cnCitySelectView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cnCitySelectView.reBindCurrentCity(new CityModel("广州", "10000001"));
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData() {
        //设置所有城市数据
        Type jsonType = new TypeToken<List<CustomPhoneCodeModel>>() {
        }.getType();
        cnStatusLs = new Gson().fromJson(cnJson, jsonType);
        //设置热门城市列表 这都是瞎写的 哈哈哈
        for (int i = 0; i < cnStatusLs.size(); i++) {
            CityModel cityModel = new CityModel(cnStatusLs.get(i).zh, cnStatusLs.get(i).code);
            cnAllCitys.add(cityModel);
            /*if (!TextUtils.isEmpty(Constant.mCity)) {
                if (Constant.mCity.equals(statusLs.get(i).city)) {
                    //设置当前城市数据
                    currentCity = cityModel;
                    //绑定数据到视图 需要 所有城市列表 热门城市列表 和 当前城市列表 其中 所有城市列表是必传的 热门城市和当前城市是选填的 不传就不会显示对应的视图
                }
            }*/
        }

        cnCitySelectView.bindData(cnAllCitys, cnHotCitys, cnCurrentCity);
        //设置搜索框的文案提示
        cnCitySelectView.setSearchTips("请输入城市名称或者拼音");
        cnCitySelectView.setShowCityCode(true);
    }

    private String readString(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            StringWriter sw = new StringWriter();
            String line;
            while ((line = br.readLine()) != null) {
                sw.write(line);
            }
            br.close();
            sw.close();
            return sw.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return null;
    }

    @OnClick(R.id.left_button)
    public void onClick() {
        finish();
    }
}