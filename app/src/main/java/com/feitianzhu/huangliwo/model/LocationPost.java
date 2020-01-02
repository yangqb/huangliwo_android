package com.feitianzhu.huangliwo.model;

public  class LocationPost {
        public boolean isLocationed;

        public LocationPost(boolean mIsLocationed) {
            isLocationed = mIsLocationed;
        }

    @Override
    public String toString() {
        return "LocationPost{" +
                "isLocationed=" + isLocationed +
                '}';
    }
}