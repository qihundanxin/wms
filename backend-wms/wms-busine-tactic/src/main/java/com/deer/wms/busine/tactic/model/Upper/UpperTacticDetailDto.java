package com.deer.wms.busine.tactic.model.Upper;

import lombok.ToString;

@ToString
public class UpperTacticDetailDto extends UpperTacticDetail {

    private String upperTacticName;
    private String shelfName;
    private String shelfCode;
    private String shelfDescribe;
    private String wareName;

    public String getUpperTacticName() {
        return upperTacticName;
    }

    public void setUpperTacticName(String upperTacticName) {
        this.upperTacticName = upperTacticName;
    }

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    public String getShelfCode() {
        return shelfCode;
    }

    public void setShelfCode(String shelfCode) {
        this.shelfCode = shelfCode;
    }

    public String getShelfDescribe() {
        return shelfDescribe;
    }

    public void setShelfDescribe(String shelfDescribe) {
        this.shelfDescribe = shelfDescribe;
    }

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }
}
