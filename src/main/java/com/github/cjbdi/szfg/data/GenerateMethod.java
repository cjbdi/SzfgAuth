package com.github.cjbdi.szfg.data;

/**
 * @author Boning Liang
 */
public enum GenerateMethod {

    DOWNLOAD("download"),
    OSS("oss"),
    FTP("ftp"),
    API("api");

    private String value;

    GenerateMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
