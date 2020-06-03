package com.feitianzhu.huangliwo.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * package name: com.feitianzhu.fu700.model
 * user: yangqinbo
 * date: 2019/12/17
 * time: 15:41
 * email: 694125155@qq.com
 */
public class ShoppingCartModel implements Serializable {

    public double allMoney;
    public List<CartGoodsModel> SCcar;
    public List<CartGoodsModel> allSCcar;


    public static class CartGoodsModel implements Parcelable {
        public int carId;
        public int goodsId;
        public int userId;
        public int goodsCount;
        public String speci;
        public String title;
        public double price;
        public double rebatePv;
        public String goodsImg;
        public double totalMoney;
        public double totalRebatePv;
        public double postage;
        public int checks;
        public String speciName;
        public String remark;
        public int sellOut;


        protected CartGoodsModel(Parcel in) {
            carId = in.readInt();
            goodsId = in.readInt();
            userId = in.readInt();
            goodsCount = in.readInt();
            speci = in.readString();
            title = in.readString();
            price = in.readDouble();
            rebatePv = in.readDouble();
            goodsImg = in.readString();
            totalMoney = in.readDouble();
            totalRebatePv = in.readDouble();
            postage = in.readDouble();
            checks = in.readInt();
            speciName = in.readString();
            remark = in.readString();
            sellOut = in.readInt();
        }

        public static final Creator<CartGoodsModel> CREATOR = new Creator<CartGoodsModel>() {
            @Override
            public CartGoodsModel createFromParcel(Parcel in) {
                return new CartGoodsModel(in);
            }

            @Override
            public CartGoodsModel[] newArray(int size) {
                return new CartGoodsModel[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(carId);
            dest.writeInt(goodsId);
            dest.writeInt(userId);
            dest.writeInt(goodsCount);
            dest.writeString(speci);
            dest.writeString(title);
            dest.writeDouble(price);
            dest.writeDouble(rebatePv);
            dest.writeString(goodsImg);
            dest.writeDouble(totalMoney);
            dest.writeDouble(totalRebatePv);
            dest.writeDouble(postage);
            dest.writeInt(checks);
            dest.writeString(speciName);
            dest.writeString(remark);
            dest.writeInt(sellOut);
        }
    }

}
