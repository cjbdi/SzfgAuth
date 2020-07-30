package com.github.cjbdi.szfg.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author Boning Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SearchVO {

    /**
     * 用户
     */
    private UserVO user;

    /**
     * 审理的案件
     */
    private LegalCaseVO legalCase;

    /**
     * 生成的检索报告的一些设置
     */
    private ReportSettingVO reportSetting;

    /**
     * 初始Tab页
     */
    private String initTab;

    /**
     * Access Key
     */
    private String accessKey;

    /**
     * 初始数据范围是否为全国
     */
    private Boolean courtNationwide;

    /**
     * 签名
     */
    private String sign;

    /**
     * 时间戳
     */
    private String timestamp;

    /**
     * 其它参数（预留，暂时不用填）
     */
    private Map<String, String> params;


    public UserVO getUser() {
        return user;
    }

    public void setUser(UserVO user) {
        this.user = user;
    }

    public LegalCaseVO getLegalCase() {
        return legalCase;
    }

    public void setLegalCase(LegalCaseVO legalCase) {
        this.legalCase = legalCase;
    }

    public ReportSettingVO getReportSetting() {
        return reportSetting;
    }

    public void setReportSetting(ReportSettingVO reportSetting) {
        this.reportSetting = reportSetting;
    }

    public String getInitTab() {
        return initTab;
    }

    public void setInitTab(String initTab) {
        this.initTab = initTab;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSign() {
        return sign;
    }

    public Boolean getCourtNationwide() {
        return courtNationwide;
    }

    public void setCourtNationwide(Boolean courtNationwide) {
        this.courtNationwide = courtNationwide;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
