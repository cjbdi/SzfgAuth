package com.github.cjbdi.szfg.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * @author Boning Liang
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PartyVO {

    /**
     * 当事人的身份，如被告、原告等
     */
    private String partyIdentity;

    /**
     * 当事人的类型，如自然人、组织机构等
     * 组织机构传 org
     * 自然人传 people
     */
    private String partyType;

    /**
     * 当事人的名称，如自然人的姓名、公司的名称等
     */
    private String partyName;

    /**
     * 身份证号，当事人为自然人则为身份证号，其它为空
     */
    private String partyId;

    public String getPartyIdentity() {
        return partyIdentity;
    }

    public void setPartyIdentity(String partyIdentity) {
        this.partyIdentity = partyIdentity;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }
}
