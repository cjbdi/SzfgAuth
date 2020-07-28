package com.github.cjbdi.szfg.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.NonNull;

/**
 * @author Boning Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicInfo {

    public BasicInfo(@NonNull String caseId, @NonNull String caseCause, String caseType, String ajbs) {
        this.caseId = caseId;
        this.caseCause = caseCause;
        this.ajbs = ajbs;
        this.caseType = caseType;
    }

    /**
     * 案号
     */
    private String caseId;

    /**
     * 案件标识
     */
    private String ajbs;

    /**
     * 案件案由
     */
    private String caseCause;

    /**
     * 案件类型
     */
    private String caseType;

    /**
     * 时间戳
     */
    private String timestamp;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getAjbs() {
        return ajbs;
    }

    public void setAjbs(String ajbs) {
        this.ajbs = ajbs;
    }

    public String getCaseCause() {
        return caseCause;
    }

    public void setCaseCause(String caseCause) {
        this.caseCause = caseCause;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
