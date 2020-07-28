package com.github.cjbdi.szfg.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

/**
 * @author Boning Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class LegalCaseVO {


    /**
     * 案由
     */
    private String caseCause;

    /**
     * 案号
     */
    private String caseId;

    /**
     * 案件类型，如刑事
     */
    private String caseType;

    /**
     * 审理程序
     */
    private String caseProcedure;

    /**
     * 法官在办案件标识
     */
    private String ajbs;

    /**
     * 文书
     */
    private List<DocumentVO> documents;

    /**
     * 当事人
     */
    private List<PartyVO> parties;

    /**
     * 其它参数（预留，暂时不用填）
     */
    private Map<String, String> params;

    public String getCaseCause() {
        return caseCause;
    }

    public void setCaseCause(String caseCause) {
        this.caseCause = caseCause;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getCaseProcedure() {
        return caseProcedure;
    }

    public void setCaseProcedure(String caseProcedure) {
        this.caseProcedure = caseProcedure;
    }

    public String getAjbs() {
        return ajbs;
    }

    public void setAjbs(String ajbs) {
        this.ajbs = ajbs;
    }

    public List<DocumentVO> getDocuments() {
        return documents;
    }

    public void setDocuments(List<DocumentVO> documents) {
        this.documents = documents;
    }

    public List<PartyVO> getParties() {
        return parties;
    }

    public void setParties(List<PartyVO> parties) {
        this.parties = parties;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
