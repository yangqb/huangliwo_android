package com.feitianzhu.huangliwo.shop.ui.dialog;

import com.feitianzhu.huangliwo.model.Province;

public interface ProvinceCallBack {

    void onWhellFinish(Province province, Province.CityListBean city,
                       Province.AreaListBean mAreaListBean);

}
