package com.deer.wms.project.root.generator.configurer;

/**
 * Created by guo on 2018/11/15.
 */
public class Table {
    private String tableName;

    private String modelName;

    private String column;

    private String type;

    public Table() {

    }

    public Table(String tableName, String modelName, String column, String type) {
        this.tableName = tableName;
        this.modelName = modelName;
        this.column = column;
        this.type = type;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
