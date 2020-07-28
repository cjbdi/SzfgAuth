package com.github.cjbdi.szfg.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

/**
 * @author Boning Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserVO {

    /**
     * 用户Id，，知识服务平台的usr_id
     */
    private String userId;

    /**
     * 用户的姓名，知识服务平台的usr_name
     */
    private String userName;

    /**
     * 法院代码，2015技术规范
     */
    private String fydm;

    /**
     * 法院代码，uim 统一 uuid
     */
    private String courtUimUuid;

    /**
     * 法院名称
     */
    private String court;

    /**
     * 法官姓名
     */
    private String judge;

    /**
     * 苏州及下辖法院固定传 ROLE_SUZHOU
     * 知识服务平台传 ROLE_ZSFW
     */
    private String leianRole;

    /**
     * 其它参数（预留，暂时不用填）
     */
    private Map<String, String> params;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFydm() {
        return fydm;
    }

    public void setFydm(String fydm) {
        this.fydm = fydm;
    }

    public String getCourtUimUuid() {
        return courtUimUuid;
    }

    public void setCourtUimUuid(String courtUimUuid) {
        this.courtUimUuid = courtUimUuid;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getLeianRole() {
        return leianRole;
    }

    public void setLeianRole(String leianRole) {
        this.leianRole = leianRole;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}
