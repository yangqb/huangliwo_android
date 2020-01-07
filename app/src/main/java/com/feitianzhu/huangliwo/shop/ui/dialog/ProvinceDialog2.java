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
    @BindView(R.id.wv_province)
    WheelView mWvProvince;
    @BindView(R.id.wv_city)
    WheelView mWvCity;

    private int maxsize = 24;
    private int minsize = 14;
    /**
     * 省份列表
     */
    private ArrayList<String> arrProvinces = new ArrayList<String>();
    /**
     * 城市列表
     */
    private ArrayList<Province.CityListBean> arrCitys = new ArrayList<>();
    private ProvinceDialog2.AddressTextAdapter provinceAdapter;
    private ProvinceDialog2.CityAddressTextAdapter cityAdapter;
    private String strProvince = "北京市";
    private String strCity = "北京市";
    private int selColor;
    private int unSelColor;
    private ProvinceCallBack mListener;
    private static WeakReference<ProvinceDialog2> dialogWeakReference;
    /**
     * 省份列表
     */
    private List<Province> provinceCityLists;

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
        mWvProvince.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) provinceAdapter.getItemText(mWvProvince.getCurrentItem());
                setTextviewSize(currentText, provinceAdapter);
            }
        });

        initCitys(strProvince);
        cityAdapter = new ProvinceDialog2.CityAddressTextAdapter(getActivity(), arrCitys, getCityItem(strCity), maxsize, minsize);
        mWvCity.setVisibleItems(5);
        mWvCity.setViewAdapter(cityAdapter);
        mWvCity.setCurrentItem(getCityItem(strCity));
        mWvCity.setShadowColor(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);
        mWvCity.setWheelBackground(R.drawable.province_wheel_bg);
        mWvCity.setWheelForeground(R.drawable.province_wheel_val);
        mWvCity.post(new Runnable() {

            @Override
            public void run() {
                String currentText = (String) cityAdapter.getItemText(mWvCity.getCurrentItem());
                setCityTextviewSize(currentText, cityAdapter);
            }
        });
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
        List<Province.CityListBean> list;

        protected CityAddressTextAdapter(Context context, List<Province.CityListBean> list, int currentItem, int maxsize, int minsize) {
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
            return list.get(index).name + "";
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

    /**
     * 根据省找到市
     *
     * @param
     */
    private void initCitys(String province) {
        arrCitys.clear();
        for (Province provinceCity : provinceCityLists) {
            if (provinceCity.getProvince().equals(province)) {
                List<Province.CityListBean> citys = provinceCity.getCitys();
                if (citys != null && !citys.isEmpty()) {
                    for (Province.CityListBean string : citys) {
                        arrCitys.add(string);
                    }
                }
            }
        }
    }

    /**
     * 初始化地点
     *
     * @param province
     * @param city
     */
    public void setAddress(String province, String city) {
        if (!TextUtils.isEmpty(province)) {
            this.strProvince = province;
        }
        if (!TextUtils.isEmpty(city)) {
            this.strCity = city;
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

    @OnClick({R.id.iv_wheel_ok, R.id.iv_wheel_cancel})
    public void okClick(View v) {
        switch (v.getId()) {
            case R.id.iv_wheel_ok:
                int mItem = mWvProvince.getCurrentItem();
                Province mProvince = provinceCityLists.get(mItem);
                strProvince = mProvince.getProvince();
                int mCurrentItem = mWvCity.getCurrentItem();
                Province.CityListBean mCityListBean = arrCitys.get(mCurrentItem);
                strCity = mCityListBean.name;
                mListener.onWhellFinish(mProvince, mCityListBean, null);
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
