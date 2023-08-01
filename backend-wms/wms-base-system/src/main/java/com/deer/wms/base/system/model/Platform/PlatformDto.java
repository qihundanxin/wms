package com.deer.wms.base.system.model.Platform;

import lombok.ToString;

@ToString
public class PlatformDto extends Platform {
    private String wareName;

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }
}
