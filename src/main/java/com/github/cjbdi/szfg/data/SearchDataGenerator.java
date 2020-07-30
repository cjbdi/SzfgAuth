package com.github.cjbdi.szfg.data;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
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
     * @param basicInfo 基本信息
     * @throws Exception 异常
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

    /**
     *
     * @param basicInfo 文书基本信息
     *                  包括：案号、案件标识、案由、案件类型
     */
    private void setBasicInfo(BasicInfo basicInfo) {
        this.basicInfo = basicInfo;
    }

    public void addDocument(@NonNull DocumentType type, @NonNull String content) {
        addDocument(type.getValue(), content);
    }

    public void addDocument(@NonNull DocumentType type, @NonNull String content, String fileName) {
        addDocument(type.getValue(), content, fileName);
    }

    /**
     *
     * @param type
     * @param content
     */
    private void addDocument(@NonNull String type, @NonNull String content) {
        addDocument(type, content, null);
    }

    private void addDocument(@NonNull String type, @NonNull String content, String fileName) {
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
            documentVO.setDocumentType(type);
            if (StrUtil.isNotBlank(fileName)) {
                documentVO.setFileName(fileName);
            }
            searchVO.getLegalCase().getDocuments().add(documentVO);
        }
    }

    public void addParty(@NonNull String type,
                         @NonNull String partyName,
                         String partyId,
                         @NonNull String partyIdentity) {
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
        partyVO.setPartyType(type);
        partyVO.setPartyIdentity(partyIdentity);
        if (PartyType.PEOPLE.getValue().equals(type)) {
            partyVO.setPartyId(partyId);
        }
        searchVO.getLegalCase().getParties().add(partyVO);
    }

    public void addParty(@NonNull PartyType type,
                         @NonNull String partyName,
                         String partyId,
                         @NonNull String partyIdentity) {
        addParty(type.getValue(), partyName, partyId, partyIdentity);
    }

    public String getSearchId() throws Exception {
        if (this.searchVO != null) {

//            SearchVO searchVO = SearchHolder.getSearch(searchId);
            SearchVO searchVO = SearchHolder.getSearchByGoogleCache(searchId);
            if (searchVO == null) {
//                SearchHolder.addSearch(searchId, this.searchVO);
                SearchHolder.addSearchIntoGoogleCache(searchId, this.searchVO);
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
        reportDownload("1");
    }

    public void reportDownload(String reportVersion) {
        ReportSettingVO reportSettingVO = new ReportSettingVO();
        reportSettingVO.setReportGenerateMethod(GenerateMethod.DOWNLOAD);
        if (StrUtil.isBlank(reportVersion)) {
            reportSettingVO.setReportVersion("1");
        } else {
            reportSettingVO.setReportVersion(reportVersion);
        }
        searchVO.setReportSetting(reportSettingVO);
    }

    /**
     * 设置将报告保存至ftp服务器
     * @param ftpVO ftp
     * @throws Exception 异常
     */
    public void saveReport2Ftp(FtpVO ftpVO, String reportVersion) throws Exception {
        setDownloadMethod(GenerateMethod.FTP, ftpVO, reportVersion);
    }
    public void saveReport2Ftp(FtpVO ftpVO) throws Exception {
        setDownloadMethod(GenerateMethod.FTP, ftpVO);
    }

    /**
     * 设置将报告保存至oss服务器
     * @param ossVO oss
     * @throws Exception 异常
     */
    public void saveReport2Oss(OssVO ossVO, String reportVersion) throws Exception {
        setDownloadMethod(GenerateMethod.OSS, ossVO, reportVersion);
    }

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
        setDownloadMethod(method, infoVO, null);
    }
    private void setDownloadMethod(GenerateMethod method, InfoVO infoVO, String reportVersion) throws Exception {
        if (searchVO.getReportSetting() == null) {
            ReportSettingVO reportSettingVO = new ReportSettingVO();
            searchVO.setReportSetting(reportSettingVO);
        }

        if (StrUtil.isBlank(reportVersion)) {
            searchVO.getReportSetting().setReportVersion("1");
        } else {
            searchVO.getReportSetting().setReportVersion(reportVersion);
        }

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

    public static SearchDataGenerator.SearchDataBuilder builder() {
        return new SearchDataGenerator.SearchDataBuilder();
    }

    @Override
    public String toString() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(searchVO);
        } catch (JsonProcessingException e) {
            return "";
        }
    }


    public static class SearchDataBuilder {

        private String publicKey;

        // basic info
        private String caseId = null;
        private String caseCause = null;
        private String caseType = null;
        private String ajbs = null;

        private String leianRole;
        private List<DocumentVO> documentVOList = new ArrayList<>();
        private String courtName;
        private String fydm;
        private String userId;
        private String userName;

        private FtpVO ftpVO;
        private OssVO ossVO;
        private ApiVO apiVO;
        private GenerateMethod generateMethod;
        private String reportVersion;

        private List<PartyVO> partyVOList = new ArrayList<>();

        SearchDataBuilder() {

        }

        public SearchDataGenerator build() throws Exception {
            BasicInfo basicInfo = new BasicInfo(caseId, caseCause, caseType, ajbs);
            SearchDataGenerator searchDataGenerator = new SearchDataGenerator(publicKey, leianRole, basicInfo);
            documentVOList.forEach(documentVO -> {
                searchDataGenerator.addDocument(
                        documentVO.getDocumentType(),
                        documentVO.getDocumentContent(),
                        documentVO.getFileName());
            });
            if (StrUtil.isBlank(courtName)) {
                throw new Exception("Court name is missing.");
            }
            searchDataGenerator.setCourt(courtName, fydm);

            searchDataGenerator.setUser(userId, userName);

            if (generateMethod != null) {
                switch (generateMethod) {
                    case DOWNLOAD:
                        searchDataGenerator.reportDownload(reportVersion);
                        break;
                    case FTP:
                        if (this.ftpVO != null) {
                            searchDataGenerator.saveReport2Ftp(this.ftpVO, reportVersion);
                        } else {
                            searchDataGenerator.reportDownload(reportVersion);
                        }
                        break;
                    case OSS:
                        if (this.ossVO != null) {
                            searchDataGenerator.saveReport2Oss(this.ossVO, reportVersion);
                        } else {
                            searchDataGenerator.reportDownload();
                        }
                        break;
                    case API:
                        if (this.apiVO != null) {
                            searchDataGenerator.callBackApi(this.apiVO);
                        } else {
                            searchDataGenerator.reportDownload();
                        }
                        break;
                    default:
                        searchDataGenerator.reportDownload(reportVersion);
                        break;
                }
            } else {
                searchDataGenerator.reportDownload(reportVersion);
            }

            // 当事人
            partyVOList.forEach(partyVO -> {
                searchDataGenerator.addParty(
                        partyVO.getPartyType(),
                        partyVO.getPartyName(),
                        partyVO.getPartyId(),
                        partyVO.getPartyIdentity());
            });

            return searchDataGenerator;
        }



        public SearchDataBuilder caseId(String caseId) {
            this.caseId = caseId;
            return this;
        }

        public SearchDataBuilder caseCause(String caseCause) {
            this.caseCause = caseCause;
            return this;
        }

        public SearchDataBuilder caseType(String caseType) {
            this.caseType = caseType;
            return this;
        }

        public SearchDataBuilder ajbs(String ajbs) {
            this.ajbs = ajbs;
            return this;
        }

        public SearchDataBuilder role(String role) {
            this.leianRole = role;
            return this;
        }

        public SearchDataBuilder publicKey(String publicKey) {
            this.publicKey = publicKey;
            return this;
        }

        public SearchDataBuilder ftp(String host,
                                     Integer port,
                                     String username,
                                     String password,
                                     String workDir,
                                     String encoding) {
            return this.ftp(host, port, username, password, workDir, encoding, null);
        }

        public SearchDataBuilder ftp(String host,
                                     Integer port,
                                     String username,
                                     String password,
                                     String workDir,
                                     String encoding,
                                     ApiVO callBackApi) {
            FtpVO ftpVO = new FtpVO();
            ftpVO.setHost(host);
            ftpVO.setPort(port);
            ftpVO.setUsername(username);
            ftpVO.setPassword(password);
            ftpVO.setWorkDir(workDir);
            ftpVO.setEncoding(encoding);
            ftpVO.setApi(callBackApi);
            return this.ftp(ftpVO);
        }

        public SearchDataBuilder ftp(FtpVO ftpVO) {
            this.generateMethod = GenerateMethod.FTP;
            this.ftpVO = ftpVO;
            return this;
        }

        public SearchDataBuilder oss(String endpoint,
                                     String accessKeyId,
                                     String accessKeySecret) {
            return this.oss(endpoint, accessKeyId, accessKeySecret, null);
        }

        public SearchDataBuilder oss(String endpoint,
                                     String accessKeyId,
                                     String accessKeySecret,
                                     ApiVO callBackApi) {
            OssVO ossVO = new OssVO();
            ossVO.setEndpoint(endpoint);
            ossVO.setAccessKeyId(accessKeyId);
            ossVO.setAccessKeySecret(accessKeySecret);
            ossVO.setApi(callBackApi);
            return this.oss(ossVO);
        }

        public SearchDataBuilder oss(OssVO ossVO) {
            this.generateMethod = GenerateMethod.OSS;
            this.ossVO = ossVO;
            return this;
        }

        public SearchDataBuilder api(String apiName, String apiUrl) {
            ApiVO apiVO = new ApiVO();
            apiVO.setName(apiName);
            apiVO.setUrl(apiUrl);
            return this.api(apiVO);
        }

        public SearchDataBuilder api(ApiVO apiVO) {
            this.generateMethod = GenerateMethod.API;
            this.apiVO = apiVO;
            return this;
        }

        public SearchDataBuilder downloadReport() {
            this.generateMethod = GenerateMethod.DOWNLOAD;
            return this;
        }

        public SearchDataBuilder reportVersion(String version) {
            this.reportVersion = version;
            return this;
        }

        public SearchDataBuilder user(String userId, String userName) {
            this.userId = userId;
            this.userName = userName;
            return this;
        }

        public SearchDataGenerator.SearchDataBuilder court(String courtName, String fydm) {
            this.courtName = courtName;
            this.fydm = fydm;
            return this;
        }

        public SearchDataBuilder addParty(@NonNull PartyType type,
                                          @NonNull String partyName,
                                          String partyId,
                                          @NonNull String partyIdentity) {
            PartyVO partyVO = new PartyVO();
            partyVO.setPartyName(partyName);
            partyVO.setPartyType(type.getValue());
            partyVO.setPartyIdentity(partyIdentity);
            if (type == PartyType.PEOPLE) {
                partyVO.setPartyId(partyId);
            }
            partyVOList.add(partyVO);
            return this;
        }

        public SearchDataBuilder addDocument(@NonNull DocumentType type,
                                             @NonNull String content) {
            return this.addDocument(type, content, null);
        }

        public SearchDataBuilder addDocument(@NonNull DocumentType type,
                                             @NonNull String content,
                                             String fileName) {
            DocumentVO documentVO = new DocumentVO();
            documentVO.setDocumentType(type.getValue());
            documentVO.setDocumentContent(content);
            documentVO.setFileName(fileName);
            this.documentVOList.add(documentVO);
            return this;
        }
    }

}
