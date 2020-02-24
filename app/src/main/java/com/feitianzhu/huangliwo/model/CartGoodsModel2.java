package com.feitianzhu.huangliwo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CartGoodsModel2 implements Parcelable {
    protected CartGoodsModel2(Parcel in) {
    }

    public static final Creator<CartGoodsModel2> CREATOR = new Creator<CartGoodsModel2>() {
        @Override
        public CartGoodsModel2 createFromParcel(Parcel in) {
            return new CartGoodsModel2(in);
        }

        @Override
        public CartGoodsModel2[] newArray(int size) {
            return new CartGoodsModel2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
