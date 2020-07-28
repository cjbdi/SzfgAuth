package com.github.cjbdi.szfg.data;

/**
 * @author Boning Liang
 */
public enum PartyType {


    PEOPLE("people"),
    ORG("org");

    private String value;

    PartyType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
