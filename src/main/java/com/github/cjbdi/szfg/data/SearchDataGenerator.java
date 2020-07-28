package com.github.cjbdi.szfg.data;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cjbdi.szfg.auth.AuthUtil;
import com.github.cjbdi.szfg.auth.SzfgAuth;
import com.github.cjbdi.szfg.core.vo.ApiVO;
import com.github.cjbdi.szfg.core.vo.FtpVO;
import com.github.cjbdi.szfg.core.vo.InfoVO;
import com.github.cjbdi.szfg.core.vo.OssVO;
import com.github.cjbdi.szfg.core.exception.NoPublicKeyException;
import com.github.cjbdi.szfg.core.exception.NoRoleException;
import com.github.cjbdi.szfg.core.util.StringUtils;
import com.github.cjbdi.szfg.core.vo.*;
import com.google.common.cache.Cache;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Boning Liang
 */
public class SearchDataGenerator {

    private SearchVO searchVO;
    private String searchId;
    private String publicKey;
    private BasicInfo basicInfo;

    /**
     *
     * @param publicKey 公钥
     * @param role 角色
     */
    public SearchDataGenerator(String publicKey, String role, BasicInfo basicInfo) throws Exception {
        this.basicInfo = basicInfo;
        searchVO = new SearchVO();
        initUser();
        initDownloadMethod();
        initLegalCase();
        mapBasicInfo();

        if (StrUtil.isBlank(publicKey)) {
            throw new NoPublicKeyException();
        } else {
            this.publicKey = publicKey;
        }
        initSign();
        searchId = StringUtils.getUUID();

        if (StrUtil.isBlank(role)) {
            throw new NoRoleException();
        } else {
            setLeianRole(role);
        }


    }

    public void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public void addDocument(@NonNull DocumentType type, @NonNull String content) {
        addDocument(type, content, null);
    }

    public void addDocument(@NonNull DocumentType type, @NonNull String content, String fileName) {
        if (StrUtil.isNotBlank(content)) {
            if (searchVO.getLegalCase() == null) {
                LegalCaseVO legalCaseVO = new LegalCaseVO();
                searchVO.setLegalCase(legalCaseVO);
            }

            if (searchVO.getLegalCase().getDocuments() == null) {
                List<DocumentVO> documentVOList = new ArrayList<>();
                searchVO.getLegalCase().setDocuments(documentVOList);
            }

            DocumentVO documentVO = new DocumentVO();
            documentVO.setDocumentContent(content);
            documentVO.setDocumentType(type.getValue());
            if (StrUtil.isNotBlank(fileName)) {
                documentVO.setFileName(fileName);
            }
            searchVO.getLegalCase().getDocuments().add(documentVO);
        }
    }

    public void addParty(@NonNull PartyType type, @NonNull String partyName, String partyId,  @NonNull String partyIdentity) {
        if (searchVO.getLegalCase() == null) {
            LegalCaseVO legalCaseVO = new LegalCaseVO();
            searchVO.setLegalCase(legalCaseVO);
        }
        if (searchVO.getLegalCase().getParties() == null) {
            List<PartyVO> partyVOList = new ArrayList<>();
            searchVO.getLegalCase().setParties(partyVOList);
        }

        PartyVO partyVO = new PartyVO();
        partyVO.setPartyName(partyName);
        partyVO.setPartyType(type.getValue());
        partyVO.setPartyIdentity(partyIdentity);
        if (type == PartyType.PEOPLE) {
            partyVO.setPartyId(partyId);
        }
        searchVO.getLegalCase().getParties().add(partyVO);
    }

    public String getSearchId() throws Exception {
        if (this.searchVO != null) {
            Cache<String, SearchVO> cache = SearchHolder.getInstance();
            SearchVO searchVO = cache.getIfPresent(searchId);

            if (searchVO == null) {
                cache.put(searchId, this.searchVO);
            }
            return searchId;
        }
        return null;
    }

    private void mapBasicInfo() throws Exception {
        checkBasicInfo();

        this.searchVO.getLegalCase().setCaseCause(basicInfo.getCaseCause());
        this.searchVO.getLegalCase().setCaseId(basicInfo.getCaseId());
        this.searchVO.getLegalCase().setAjbs(basicInfo.getAjbs());
        this.searchVO.getLegalCase().setCaseType(basicInfo.getCaseType());
    }

    private void checkBasicInfo() throws Exception {
        if (basicInfo == null) {
            throw new Exception("Basic Info is null");
        }
        if (StrUtil.isBlank(basicInfo.getCaseCause())) {
            throw new Exception("案由为空");
        }
        if (StrUtil.isBlank(basicInfo.getCaseId())) {
            throw new Exception("案号为空");
        }
    }

    public SearchVO getSearch() {
        return searchVO;
    }

    /**
     *
     * @param courtName 法院名称
     * @param fydm 法院代码
     */
    public void setCourt(String courtName, String fydm) {
        searchVO.getUser().setCourt(courtName);
        searchVO.getUser().setFydm(fydm);
    }

    /**
     *
     * @param userId 用户名
     * @param usersName 用户的姓名
     * @param judge 法官姓名(可不传)
     */
    public void setUser(@NonNull String userId, @NonNull String usersName, String judge) {
        searchVO.getUser().setUserId(userId);
        searchVO.getUser().setUserName(usersName);
        if (StrUtil.isBlank(judge)) {
            judge = usersName;
        }
        searchVO.getUser().setJudge(judge);
    }

    public void setUser(@NonNull String userId, @NonNull String userName) {
        setUser(userId, userName, null);
    }

    /**
     *
     * @param leianRole 角色代码
     */
    public void setLeianRole(String leianRole) {
        if (searchVO.getUser() == null) {
            UserVO userVO = new UserVO();
            searchVO.setUser(userVO);
        }
        searchVO.getUser().setLeianRole(leianRole);
    }


    /**
     * 设置将报告保存至用户本地计算机，（默认）
     */
    public void reportDownload() {
        ReportSettingVO reportSettingVO = new ReportSettingVO();
        reportSettingVO.setReportGenerateMethod(GenerateMethod.DOWNLOAD);
        reportSettingVO.setReportVersion("1");
        searchVO.setReportSetting(reportSettingVO);
    }

    /**
     * 设置将报告保存至ftp服务器
     * @param ftpVO ftp
     * @throws Exception 异常
     */
    public void saveReport2Ftp(FtpVO ftpVO) throws Exception {
        setDownloadMethod(GenerateMethod.FTP, ftpVO);
    }

    /**
     * 设置将报告保存至oss服务器
     * @param ossVO oss
     * @throws Exception 异常
     */
    public void saveReport2Oss(OssVO ossVO) throws Exception {
        setDownloadMethod(GenerateMethod.OSS, ossVO);
    }

    /**
     * 设置将检索记录回传至接口
     * @param apiVO api接口
     * @throws Exception 异常
     */
    public void callBackApi(ApiVO apiVO) throws Exception {
        setDownloadMethod(GenerateMethod.API, apiVO);
    }


    /**
     *
     * @param method 生成方法
     * @param infoVO null 或 ftpVO 或 ossVO 或 apiVO
     * @throws Exception 异常
     */
    private void setDownloadMethod(GenerateMethod method, InfoVO infoVO) throws Exception {
        if (searchVO.getReportSetting() == null) {
            ReportSettingVO reportSettingVO = new ReportSettingVO();
            searchVO.setReportSetting(reportSettingVO);
        }

        searchVO.getReportSetting().setReportVersion("1");
        if (method == null) {
            searchVO.getReportSetting().setReportGenerateMethod(GenerateMethod.DOWNLOAD);
        } else {
            searchVO.getReportSetting().setReportGenerateMethod(method);
            switch (method) {
                case DOWNLOAD:
                    break;
                case FTP:
                    if (infoVO instanceof FtpVO) {
                        searchVO.getReportSetting().setFtp(SzfgAuth.getFtp((FtpVO) infoVO, publicKey));
                    } else {
                        searchVO.getReportSetting().setReportGenerateMethod(GenerateMethod.DOWNLOAD);
                    }
                    break;
                case OSS:
                    if (infoVO instanceof OssVO) {
                        searchVO.getReportSetting().setOss(SzfgAuth.getOss((OssVO) infoVO, publicKey));
                    } else {
                        searchVO.getReportSetting().setReportGenerateMethod(GenerateMethod.DOWNLOAD);
                    }
                    break;
                case API:
                    if (infoVO instanceof ApiVO) {
                        searchVO.getReportSetting().setApi(SzfgAuth.getApi((ApiVO) infoVO, publicKey));
                    } else {
                        searchVO.getReportSetting().setReportGenerateMethod(GenerateMethod.DOWNLOAD);
                    }
                    break;
            }
        }

    }


    private void initDownloadMethod() {
        if (searchVO.getReportSetting() == null) {
            ReportSettingVO reportSettingVO = new ReportSettingVO();
            reportSettingVO.setReportGenerateMethod(GenerateMethod.DOWNLOAD);
            reportSettingVO.setReportVersion("1");
            searchVO.setReportSetting(reportSettingVO);
        }
    }

    private void initUser() {
        if (searchVO.getUser() == null) {
            UserVO userVO = new UserVO();
            searchVO.setUser(userVO);
        }
    }

    private void initSign() throws Exception {
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("+08:00"));
        String timestamp = String.valueOf(localDateTime.toEpochSecond(ZoneOffset.of("+08:00")));
        basicInfo.setTimestamp(timestamp);
        ObjectMapper objectMapper = new ObjectMapper();
        String basicInfoString = objectMapper.writeValueAsString(basicInfo);
        searchVO.setSign(AuthUtil.encryptRSA(basicInfoString, this.publicKey));
    }

    private void initLegalCase() {
        if (searchVO.getLegalCase() == null) {
            LegalCaseVO legalCaseVO = new LegalCaseVO();
            searchVO.setLegalCase(legalCaseVO);
        }
    }

}
