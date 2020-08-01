package com.shr.marketplace.models;

public enum ProjectType {
    SOFTWARE("SOFTWARE"), REAL_ESTATE("REAL_ESTATE"), AVIATION("AVIATION");

    String type;
    private ProjectType(final String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}
