package com.feitianzhu.huangliwo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PassengerModel implements Parcelable {
    public boolean isSelect;
    public String id;
    public String name;
    public int ageType;
    public String cardType;
    public String cardNo;
    public int userId;
    public int sex;
    public String birthday;

    protected PassengerModel(Parcel in) {
        isSelect = in.readByte() != 0;
        id = in.readString();
        name = in.readString();
        ageType = in.readInt();
        cardType = in.readString();
        cardNo = in.readString();
        userId = in.readInt();
        sex = in.readInt();
        birthday = in.readString();
    }

    public static final Creator<PassengerModel> CREATOR = new Creator<PassengerModel>() {
        @Override
        public PassengerModel createFromParcel(Parcel in) {
            return new PassengerModel(in);
        }

        @Override
        public PassengerModel[] newArray(int size) {
            return new PassengerModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeString(id);
        dest.writeString(name);
        dest.writeInt(ageType);
        dest.writeString(cardType);
        dest.writeString(cardNo);
        dest.writeInt(userId);
        dest.writeInt(sex);
        dest.writeString(birthday);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PassengerModel user = (PassengerModel) obj;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
