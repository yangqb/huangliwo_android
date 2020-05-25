package com.feitianzhu.huangliwo.shop.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.feitianzhu.huangliwo.R;
import com.feitianzhu.huangliwo.model.Province;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * package name: com.feitianzhu.huangliwo.shop.ui.dialog
 * user: yangqinbo
 * date: 2020/1/4
 * time: 11:26
 * email: 694125155@qq.com
 */
public class ProvinceDialog2 extends DialogFragment {
    public static final int PROVINCE_CITY = 1;
    public static final int PROVINCE_CITY_AREA = 2;
    private int type = 1;
    @BindView(R.id.wv_province)
    WheelView mWvProvince;
    @BindView(R.id.wv_city)
    WheelView mWvCity;
    @BindView(R.id.wv_area)
    WheelView mWvArea;

    private int maxsize = 24;
    private int minsize = 14;
    /**
     * 省份列表
     */
    private ArrayList<String> arrProvinces = new ArrayList<String>();
    /**
     * 城市列表
     */
    private ArrayList<String> arrCitys = new ArrayList<>();
    /*
     * 区列表
     * */
    private ArrayList<String> arrAreas = new ArrayList<>();
    private ProvinceDialog2.AddressTextAdapter provinceAdapter;
    private ProvinceDialog2.CityAddressTextAdapter cityAdapter;
    private ProvinceDialog2.AreaAddressTextAdapter areaAdapter;
    private String strProvince = "北京市";
    private String strCity = "北京市";
    private String strArea = "东城区";
    private int selColor;
    private int unSelColor;
    private ProvinceCallBack mListener;
    private static WeakReference<ProvinceDialog2> dialogWeakReference;
    /**
     * 省份列表
     */
    private List<Province> provinceCityLists;
    /*
     * 市列表
     * */
    private List<Province.CityListBean> cityLists;

    private List<Province.AreaListBean> areaLists;

    public void setSelectOnListener(ProvinceCallBack listener) {
        this.mListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.custom_dialog); //dialog全屏
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_province, container, false);
        ButterKnife.bind(this, view);
        this.getDialog().setCanceledOnTouchOutside(true);
        this.getDialog().setCancelable(true);
        Window window = this.getDialog().getWindow();
        window.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        wlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(wlp);
        if (type == PROVINCE_CITY) {
            mWvArea.setVisibility(View.GONE);
        } else {
            mWvArea.setVisibility(View.VISIBLE);
        }
        return view;
    }

    public static ProvinceDialog2 newInstance() {
        ProvinceDialog2 dialog = new ProvinceDialog2();
        return dialog;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        selColor = getResources().getColor(R.color.color_wheel_sel);
        unSelColor = getResources().getColor(R.color.color_wheel_unsel);
        provinceAdapter = new ProvinceDialog2.AddressTextAdapter(getActivity(), arrProvinces, getProvinceItem(strProvince), maxsize, minsize);
        mWvProvince.setVisibleItems(5);
        mWvProvince.setViewAdapter(provinceAdapter);
        mWvProvince.setCurrentItem(getProvinceItem(strProvince));
        mWvProvince.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        mWvProvince.setWheelBackground(R.drawable.province_wheel_bg);
        mWvProvince.setWheelForeground(R.drawable.province_wheel_val);
        setTextviewSize(strProvince, provinceAdapter);

        initCitys(strProvince);
        cityAdapter = new ProvinceDialog2.CityAddressTextAdapter(getActivity(), arrCitys, getCityItem(strCity), maxsize, minsize);
        mWvCity.setVisibleItems(5);
        mWvCity.setViewAdapter(cityAdapter);
        mWvCity.setCurrentItem(getCityItem(strCity));
        mWvCity.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        mWvCity.setWheelBackground(R.drawable.province_wheel_bg);
        mWvCity.setWheelForeground(R.drawable.province_wheel_val);
        setCityTextviewSize(strCity, cityAdapter);

        initAreas(strCity);
        areaAdapter = new ProvinceDialog2.AreaAddressTextAdapter(getActivity(), arrAreas, getAreaItem(strArea), maxsize, minsize);
        mWvArea.setVisibleItems(5);
        mWvArea.setViewAdapter(areaAdapter);
        mWvArea.setCurrentItem(getAreaItem(strArea));
        mWvArea.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        mWvArea.setWheelBackground(R.drawable.province_wheel_bg);
        mWvArea.setWheelForeground(R.drawable.province_wheel_val);
        setAreaTextviewSize(strArea, areaAdapter);

        initListener();
    }

    public void initListener() {
        mWvProvince.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                strProvince = currentText;
                setTextviewSize(currentText, provinceAdapter);
                initCitys(currentText);
                cityAdapter = new ProvinceDialog2.CityAddressTextAdapter(getActivity(), arrCitys, 0, maxsize, minsize);
                mWvCity.setVisibleItems(5);
                mWvCity.setCurrentItem(0);
                String currentCityText = (String) cityAdapter.getItemText(mWvCity.getCurrentItem());
                setCityTextviewSize(currentCityText, cityAdapter);
                mWvCity.setViewAdapter(cityAdapter);
                initAreas(arrCitys.get(0));
                areaAdapter = new ProvinceDialog2.AreaAddressTextAdapter(getActivity(), arrAreas, 0, maxsize, minsize);
                mWvArea.setVisibleItems(5);
                mWvArea.setCurrentItem(0);
                String currentAreaText = (String) areaAdapter.getItemText(mWvArea.getCurrentItem());
                setAreaTextviewSize(currentAreaText, areaAdapter);
                mWvArea.setViewAdapter(areaAdapter);
            }
        });

        mWvProvince.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) provinceAdapter.getItemText(wheel.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        mWvCity.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                strCity = currentText;
                setCityTextviewSize(currentText, cityAdapter);
                initAreas(currentText);
                areaAdapter = new ProvinceDialog2.AreaAddressTextAdapter(getActivity(), arrAreas, 0, maxsize, minsize);
                mWvArea.setVisibleItems(5);
                mWvArea.setCurrentItem(0);
                String currentCityText = (String) areaAdapter.getItemText(mWvArea.getCurrentItem());
                setAreaTextviewSize(currentCityText, areaAdapter);
                mWvArea.setViewAdapter(areaAdapter);
            }
        });

        mWvCity.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) cityAdapter.getItemText(wheel.getCurrentItem());
                setCityTextviewSize(currentText, cityAdapter);
            }
        });

        mWvArea.addChangingListener(new OnWheelChangedListener() {

            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                strArea = currentText;
                setAreaTextviewSize(currentText, areaAdapter);
            }
        });

        mWvArea.addScrollingListener(new OnWheelScrollListener() {

            @Override
            public void onScrollingStarted(WheelView wheel) {
            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                String currentText = (String) areaAdapter.getItemText(wheel.getCurrentItem());
                setAreaTextviewSize(currentText, areaAdapter);
            }
        });
    }

    private void initData() {
        try {
            String json = readString(getActivity().getAssets().open("region2.json"));
            Type type = new TypeToken<List<Province>>() {
            }.getType();
            provinceCityLists = new Gson().fromJson(json, type);
            if (provinceCityLists != null && !provinceCityLists.isEmpty()) {
                for (Province provinceCity : provinceCityLists) {
                    arrProvinces.add(provinceCity.getProvince());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class AddressTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected AddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_view, NO_RESOURCE, currentItem, maxsize, minsize, selColor, unSelColor);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    private class CityAddressTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        protected CityAddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_view, NO_RESOURCE, currentItem, maxsize, minsize, selColor, unSelColor);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            return list.get(index) + "";
        }
    }

    private class AreaAddressTextAdapter extends AbstractWheelTextAdapter {
        List<String> list;

        public AreaAddressTextAdapter(Context context, List<String> list, int currentItem, int maxsize, int minsize) {
            super(context, R.layout.item_view, NO_RESOURCE, currentItem, maxsize, minsize, selColor, unSelColor);
            this.list = list;
            setItemTextResource(R.id.tempValue);
        }

        @Override
        protected CharSequence getItemText(int i) {
            return list.size() == 0 ? "" : list.get(i) + "";
        }

        @Override
        public View getItem(int index, View convertView, ViewGroup parent) {
            View view = super.getItem(index, convertView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return list.size();
        }
    }

    /**
     * 设置字体大小
     *
     * @param curriteItemText
     * @param adapter
     */
    public void setTextviewSize(String curriteItemText, ProvinceDialog2.AddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(20);
                textvew.setTextColor(selColor);
            } else {
                textvew.setTextSize(14);
                textvew.setTextColor(unSelColor);
            }
        }
    }

    public void setCityTextviewSize(String curriteItemText, ProvinceDialog2.CityAddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(20);
                textvew.setTextColor(selColor);
            } else {
                textvew.setTextSize(14);
                textvew.setTextColor(unSelColor);
            }
        }
    }

    public void setAreaTextviewSize(String curriteItemText, ProvinceDialog2.AreaAddressTextAdapter adapter) {
        ArrayList<View> arrayList = adapter.getTestViews();
        int size = arrayList.size();
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textvew = (TextView) arrayList.get(i);
            currentText = textvew.getText().toString();
            if (curriteItemText.equals(currentText)) {
                textvew.setTextSize(20);
                textvew.setTextColor(selColor);
            } else {
                textvew.setTextSize(14);
                textvew.setTextColor(unSelColor);
            }
        }
    }

    /**
     * 根据省找到市
     *
     * @param
     */
    private List<String> initCitys(String province) {
        arrCitys.clear();
        for (Province provinceCity : provinceCityLists) {
            if (provinceCity.getProvince().equals(province)) {
                cityLists = provinceCity.getCitys();
                if (cityLists != null && !cityLists.isEmpty()) {
                    for (Province.CityListBean string : cityLists) {
                        arrCitys.add(string.getName());
                    }
                }
            }
        }
        return arrCitys;
    }

    /**
     * 根据市找到区
     *
     * @param
     */
    private List<String> initAreas(String strCity) {
        arrAreas.clear();
        for (Province.CityListBean cityListBean : cityLists) {
            if (cityListBean.getName().equals(strCity)) {
                areaLists = cityListBean.getAreaList();
                if (areaLists != null && !areaLists.isEmpty()) {
                    for (Province.AreaListBean string : areaLists) {
                        arrAreas.add(string.getName());
                    }
                }
            }
        }
        return arrAreas;
    }

    public void setCityLevel(int type) {
        this.type = type;
    }

    /**
     * 初始化地点
     *
     * @param province
     * @param city
     */
    public void setAddress(String province, String city, String strArea) {
        if (!TextUtils.isEmpty(province)) {
            this.strProvince = province;
        }
        if (!TextUtils.isEmpty(city)) {
            this.strCity = city;
        }
        if (!TextUtils.isEmpty(strArea)) {
            this.strArea = strArea;
        }
    }

    /**
     * 返回省会索引
     *
     * @param province
     * @return
     */
    public int getProvinceItem(String province) {
        int size = arrProvinces.size();
        int provinceIndex = 0;
        for (int i = 0; i < size; i++) {
            if (province.equals(arrProvinces.get(i))) {
                return provinceIndex;
            } else {
                provinceIndex++;
            }
        }
        return provinceIndex;
    }

    /**
     * 得到城市索引
     *
     * @param city
     * @return
     */
    public int getCityItem(String city) {
        int size = arrCitys.size();
        int cityIndex = 0;
        for (int i = 0; i < size; i++) {
            if (city.equals(arrCitys.get(i))) {
                return cityIndex;
            } else {
                cityIndex++;
            }
        }
        return cityIndex;
    }

    /**
     * 得到区的索引
     *
     * @param strArea
     * @return
     */
    public int getAreaItem(String strArea) {
        int size = arrAreas.size();
        int areaIndex = 0;
        for (int i = 0; i < size; i++) {
            if (strArea.equals(arrAreas.get(i))) {
                return areaIndex;
            } else {
                areaIndex++;
            }
        }
        return areaIndex;
    }

    @OnClick({R.id.iv_wheel_ok, R.id.iv_wheel_cancel})
    public void okClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wheel_ok:
                int mProvinceItem = mWvProvince.getCurrentItem();
                Province mProvince = provinceCityLists.get(mProvinceItem);
                strProvince = mProvince.getProvince();
                int mCityItem = mWvCity.getCurrentItem();
                Province.CityListBean mCityListBean = cityLists.get(mCityItem);
                strCity = mCityListBean.name;
                int mAreaItem = mWvArea.getCurrentItem();
                Province.AreaListBean mAreaListBean = areaLists.get(mAreaItem);
                mListener.onWhellFinish(mProvince, mCityListBean, mAreaListBean);
                dismiss();
                break;
            case R.id.iv_wheel_cancel:
                dismiss();
                break;
        }

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

    public void show(FragmentManager fragmentManager) {
        dialogWeakReference = new WeakReference<ProvinceDialog2>(this);
        show(fragmentManager, "dialog");
    }
}
