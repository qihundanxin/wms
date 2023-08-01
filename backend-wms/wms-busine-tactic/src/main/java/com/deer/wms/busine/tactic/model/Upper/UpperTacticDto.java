package com.deer.wms.busine.tactic.model.Upper;

import lombok.ToString;

@ToString
public class UpperTacticDto extends UpperTactic {
    private String wareName;
    private String organizationName;

    public String getWareName() {
        return wareName;
    }

    public void setWareName(String wareName) {
        this.wareName = wareName;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
