package com.feitianzhu.huangliwo.strategy.bean;

public class TitileBean {

    /**
     * columnId : 2
     * columnName : 正品保障
     */

    private int columnId;
    private String columnName;

    public TitileBean(String columnName) {
        this.columnName = columnName;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}
