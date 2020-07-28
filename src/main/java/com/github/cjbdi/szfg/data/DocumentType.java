package com.github.cjbdi.szfg.data;

/**
 * @author Boning Liang
 */
public enum DocumentType {

    CAI_PAN_WEN_SHU("裁判文书"),
    PAN_JUE_SHU("判决书"),
    CAI_DING_SHU("裁定书"),
    TONG_ZHI_SHU("通知书"),
    JUE_DING_SHU("决定书"),
    TIAO_JIE_SHU("调解书"),
    ZHI_XING_SHU("执行书"),
    TING_SHEN_BI_LU("庭审笔录"),
    BI_LU("笔录"),
    QI_SU_SHU("起诉书"),
    OTHER("其它");

    private String value;

    DocumentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
