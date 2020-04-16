package com.feitianzhu.huangliwo.plane;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cretin.tools.cityselect.callback.OnCitySelectListener;
import com.cretin.tools.cityselect.callback.OnLocationListener;
import com.cretin.tools.cityselect.model.CityModel;
import com.cretin.tools.cityselect.view.CitySelectView;
import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.common.Constant;
import com.feitianzhu.huangliwo.me.base.BaseActivity;
import com.feitianzhu.huangliwo.model.CustomCityModel;
import com.feitianzhu.huangliwo.model.Province;
import com.feitianzhu.huangliwo.utils.ToastUtils;
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
 * date: 2020/3/10
 * time: 17:57
 * email: 694125155@qq.com
 */
public class SelectPlaneCityActivity extends BaseActivity {
    public static final String CITY_TYPE = "search_type";
    public static final String CITY_DATA = "CITY_DATA";
    public int type = 0;//国内0,国际1
    private List<CustomCityModel> cnStatusLs = new ArrayList<>();
    private List<CustomCityModel> interStatusLs = new ArrayList<>();
    private List<CityModel> cnHotCitys = new ArrayList<>();
    private List<CityModel> interHotCitys = new ArrayList<>();
    private String cnJson;
    private String interJson;
    private List<CityModel> cnAllCitys = new ArrayList<>();
    private List<CityModel> interAllCitys = new ArrayList<>();
    private CityModel cnCurrentCity;
    private CityModel interCurrentCity;
    @BindView(R.id.cn_city_view)
    CitySelectView cnCitySelectView;
    @BindView(R.id.inter_city_view)
    CitySelectView interCitySelectView;
    @BindView(R.id.btn_domestic)
    TextView btnDomestic;
    @BindView(R.id.btn_international)
    TextView btnInternational;
    @BindView(R.id.line1)
    View line1;
    @BindView(R.id.line2)
    View line2;
    @BindView(R.id.right_text)
    TextView rightText;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_select_city;
    }

    @Override
    protected void initView() {
        rightText.setText("确定");
        rightText.setVisibility(View.GONE);
        try {
            cnJson = readString(mContext.getAssets().open("cn.json"));
            interJson = readString(mContext.getAssets().open("internation.json"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //设置城市选择之后的事件监听
        cnCitySelectView.setOnCitySelectListener(new OnCitySelectListener() {
            @Override
            public void onCitySelect(CityModel cityModel) {
                //Toast.makeText(SelectPlaneCityActivity.this, "你点击了：" + cityModel.getCityName() + ":" + cityModel.getExtra().toString(), Toast.LENGTH_SHORT).show();
                if ("中国香港".equals(cityModel.getCountryName()) || "中国澳门".equals(cityModel.getCountryName()) || "中国台湾".equals(cityModel.getCountryName())) {
                    type = 1;
                }
                Intent intent = new Intent();
                intent.putExtra(CITY_TYPE, type);
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

        //设置城市选择之后的事件监听
        interCitySelectView.setOnCitySelectListener(new OnCitySelectListener() {
            @Override
            public void onCitySelect(CityModel cityModel) {
                //Toast.makeText(SelectPlaneCityActivity.this, "你点击了：" + cityModel.getCityName() + ":" + cityModel.getExtra().toString(), Toast.LENGTH_SHORT).show();
                if ("中国香港".equals(cityModel.getCountryName()) || "中国澳门".equals(cityModel.getCountryName()) || "中国台湾".equals(cityModel.getCountryName())) {
                    type = 1;
                }
                Intent intent = new Intent();
                intent.putExtra(CITY_TYPE, type);
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
                        cnCitySelectView.reBindCurrentCity(new CityModel("广州", "中国", "10000001"));
                    }
                }, 2000);
            }
        });

        //设置点击重新定位之后的事件监听
        interCitySelectView.setOnLocationListener(new OnLocationListener() {
            @Override
            public void onLocation() {
                //这里模拟定位 两秒后给个随便的定位数据
                interCitySelectView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        interCitySelectView.reBindCurrentCity(new CityModel("广州", "中国", "10000001"));
                    }
                }, 2000);
            }
        });
    }

    public void initCnCity() {
        //设置所有城市数据
        Type jsonType = new TypeToken<List<CustomCityModel>>() {
        }.getType();
        cnStatusLs = new Gson().fromJson(cnJson, jsonType);
        //设置热门城市列表 这都是瞎写的 哈哈哈
        for (int i = 0; i < cnStatusLs.size(); i++) {
            CityModel cityModel = new CityModel(cnStatusLs.get(i).city, cnStatusLs.get(i).country, cnStatusLs.get(i).szm);
            cnAllCitys.add(cityModel);
            if ("北京".equals(cnStatusLs.get(i).city) || "上海".equals(cnStatusLs.get(i).city) || "广州".equals(cnStatusLs.get(i).city) || "深圳".equals(cnStatusLs.get(i).city) || "武汉".equals(cnStatusLs.get(i).city)) {
                cnHotCitys.add(cityModel);
            }

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
        cnCitySelectView.setShowCityCode(false);
    }

    public void initInterCity() {
        //设置所有城市数据
        Type jsonType = new TypeToken<List<CustomCityModel>>() {
        }.getType();
        interStatusLs = new Gson().fromJson(interJson, jsonType);
        //设置热门城市列表 这都是瞎写的 哈哈哈
        for (int i = 0; i < interStatusLs.size(); i++) {
            CityModel cityModel = new CityModel(interStatusLs.get(i).city, interStatusLs.get(i).country, interStatusLs.get(i).szm);
            interAllCitys.add(cityModel);

            if (interStatusLs.get(i).id == 5391 || interStatusLs.get(i).id == 47 ||
                    interStatusLs.get(i).id == 111 || interStatusLs.get(i).id == 5387 || (interStatusLs.get(i).id == 31 || interStatusLs.get(i).id == 789)) {
                interHotCitys.add(cityModel);

            }
            /*if (!TextUtils.isEmpty(Constant.mCity)) {
                if (Constant.mCity.equals(statusLs.get(i).city)) {
                    //设置当前城市数据
                    currentCity = cityModel;
                    //绑定数据到视图 需要 所有城市列表 热门城市列表 和 当前城市列表 其中 所有城市列表是必传的 热门城市和当前城市是选填的 不传就不会显示对应的视图
                }
            }*/
        }

        interCitySelectView.bindData(interAllCitys, interHotCitys, interCurrentCity);
        //设置搜索框的文案提示
        interCitySelectView.setSearchTips("请输入城市名称或者拼音");
        interCitySelectView.setShowCityCode(false);
    }

    @OnClick({R.id.btn_domestic, R.id.left_button, R.id.btn_international, R.id.right_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_domestic:
                type = 0;
                cnCitySelectView.setVisibility(View.VISIBLE);
                interCitySelectView.setVisibility(View.GONE);
                btnDomestic.setTextColor(getResources().getColor(R.color.color_333333));
                btnInternational.setTextColor(getResources().getColor(R.color.color_666666));
                line1.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                line2.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case R.id.btn_international:
                cnCitySelectView.setVisibility(View.GONE);
                interCitySelectView.setVisibility(View.VISIBLE);
                type = 1;
                btnDomestic.setTextColor(getResources().getColor(R.color.color_666666));
                btnInternational.setTextColor(getResources().getColor(R.color.color_433D36));
                line1.setBackgroundColor(getResources().getColor(R.color.white));
                line2.setBackgroundColor(getResources().getColor(R.color.color_fed228));
                break;
            case R.id.left_button:
                finish();
                break;
            case R.id.right_button:

                break;
        }
    }

    @Override
    protected void initData() {
        initCnCity();
        initInterCity();
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
}
