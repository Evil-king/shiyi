package com.baibei.shiyi.admin.modules.system.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrganizationProxyVo extends BaseRowModel {

    // 机构id
    private Long id;

    @ExcelProperty(index = 1, value = "机构编号")
    private String orgCode;

    //机构名称
    @ExcelProperty(index = 2, value = "机构名称")
    private String orgName;

    @ExcelProperty(index = 3, value = "事业部名称")
    private String businessName;

    @ExcelProperty(index = 4, value = "组织机构名称")
    private String organizationName;

    //商业类型  personal,个人,enterprise,企业
    private String businessType;

    @ExcelProperty(index = 5, value = "实体")
    private String businessTypeText;

    /**
     * 机构类型(business：事业部；organization：机构；cityAgent：市代理；areaAgent：区代理；ordinaryAgent：普通代理；branchOffice:分公司)
     */
    private String orgType;


    @ExcelProperty(index = 6, value = "姓名")
    private String name;

    @ExcelProperty(index = 7, value = "身份证")
    private String idCard;

    @ExcelProperty(index = 8, value = "公司名称")
    private String companyName;

    @ExcelProperty(index = 9, value = "工商营业执照")
    private String businessLicense;

    @ExcelProperty(index = 10, value = "组织机构代码证")
    private String organizationCodeCertificate;

    @ExcelProperty(index = 11, value = "税务登记证")
    private String taxRegistrationCertificate;
    /**
     * 机构类型文字表达
     */
    @ExcelProperty(index = 12, value = "代理类型")
    private String orgTypeText;

    @ExcelProperty(index = 13, value = "代理区域")
    private String agentArea;

    @ExcelProperty(index = 14, value = "市代理")
    private String cityAgent;

    @ExcelProperty(index = 15, value = "区代理")
    private String areaAgent;

    @ExcelProperty(index = 16, value = "平级推荐人")
    private Long referrer;

    //返还账号
    @ExcelProperty(index = 17, value = "返还账号")
    private String returnAccount;


    @ExcelProperty(index = 18, value = "返佣冻结比例")
    private Float rebateFreezeRatio;


    @ExcelProperty(index = 19, value = "注册手机号")
    private String registerMobile;

    //创建时间
    @ExcelProperty(index = 20, value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    /**
     * 父id
     */
    private String pid;


    /**
     * 普通代理
     */
    private String ordinaryAgent;


    /**
     * 机构Id
     */
    private Long organizationId;

    /**
     * 市代理Id
     */
    private Long cityAgentId;

    /**
     * 区代理Id
     */
    private Long areaAgentId;


    /**
     * 代理省
     */
    private String agentAreaProvince;

    /**
     * 代理市
     */
    private String agentAreaCity;

    /**
     * 代理区
     */
    private String agentAreaRegion;

    /**
     * 代理区域
     */
    private List<String> agentAreaList;

}
