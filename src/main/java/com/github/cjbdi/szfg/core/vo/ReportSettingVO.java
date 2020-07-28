package com.github.cjbdi.szfg.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.cjbdi.szfg.data.GenerateMethod;

import java.util.Map;

/**
 * @author Boning Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ReportSettingVO {

    /**
     * 检索报告生成方式，默认为下载
     * 下载到本地   - download
     * TBD 保存到ftp   - ftp
     * TBD 保存到oss   - oss
     * TBD 回传生成检索报告需要的数据  - api
     * 注：TBD - To be development （待开发）
     */
    private String reportGenerateMethod;

    /**
     * 生成的检索报告版本
     */
    private String reportVersion;

    /**
     * (TBD)
     * Ftp （加密传输）
     */
    private String ftp;

    /**
     * (TBD)
     * OSS（加密传输）
     */
    private String oss;

    /**
     * 回传地址Api version
     */
    private String apiVersion;

    /**
     * (TBD)
     * 回传地址Api
     */
    private String api;

    /**
     * 其它参数（预留，暂时不用填）
     */
    private Map<String, String> params;


    public String getReportGenerateMethod() {
        return reportGenerateMethod;
    }

    public void setReportGenerateMethod(GenerateMethod generateMethod) {
        this.reportGenerateMethod = generateMethod.getValue();
    }

    public String getReportVersion() {
        return reportVersion;
    }

    public void setReportVersion(String reportVersion) {
        this.reportVersion = reportVersion;
    }

    public String getFtp() {
        return ftp;
    }

    public void setFtp(String ftp) {
        this.ftp = ftp;
    }

    public String getOss() {
        return oss;
    }

    public void setOss(String oss) {
        this.oss = oss;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
