package com.feitianzhu.huangliwo.model;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class MultiCollectionModel implements MultiItemEntity {
    public static final int GOODS = 2;
    public static final int MERCHANT = 1;
    private int type;
    private CollectionInfo.CollectionModel collectionModel;

    public MultiCollectionModel(int type) {
        this.type = type;
    }

    @Override
    public int getItemType() {
        return type;
    }

    public CollectionInfo.CollectionModel getCollectionModel() {
        return collectionModel;
    }

    public void setCollectionModel(CollectionInfo.CollectionModel collectionModel) {
        this.collectionModel = collectionModel;
    }
}
