package com.deer.wms.base.system.model.Area;

import lombok.ToString;

@ToString
public class AreaInfoDto extends AreaInfo {
    private String wareName;

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }
}
