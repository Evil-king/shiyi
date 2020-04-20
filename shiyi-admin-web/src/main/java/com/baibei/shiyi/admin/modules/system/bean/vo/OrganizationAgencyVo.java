package com.baibei.shiyi.admin.modules.system.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

@Data
public class OrganizationAgencyVo extends BaseRowModel {
    // 机构id
    private Long id;
    //机构编号
    @ExcelProperty(index = 1, value = "机构编号")
    private String orgCode;
    //机构名称
    @ExcelProperty(index = 2, value = "机构名称")
    private String orgName;
    //商业类型  personal,个人,enterprise,企业
    private String businessType;


    @ExcelProperty(index = 3,value = "所属事业部编号")
    private String businessCode;

    /**
     * 所属事业部名称
     */
    @ExcelProperty(index = 4,value = "所属事业部")
    private String businessName;

    @ExcelProperty(index = 5, value = "实体")
    private String businessTypeText;
    //姓名
    @ExcelProperty(index = 6, value = "姓名")
    private String name;
    //身份证号
    @ExcelProperty(index = 7, value = "身份证")
    private String idCard;
    //公司名称
    @ExcelProperty(index = 8, value = "公司名称")
    private String companyName;
    //工商营业证
    @ExcelProperty(index = 9, value = "工商营业证")
    private String businessLicense;
    //组织机构代码证
    @ExcelProperty(index = 10, value = "组织机构代码证")
    private String organizationCodeCertificate;
    //税务登记证
    @ExcelProperty(index = 11, value = "税务登记证")
    private String taxRegistrationCertificate;
    //返还账号
    @ExcelProperty(index = 12,value = "返还账号")
    private String returnAccount;
    //返佣冻结比例
    @ExcelProperty(index = 13,value = "返佣冻结比例")
    private Float rebateFreezeRatio;

    //创建时间
    @ExcelProperty(index = 14,value = "创建时间",format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 父id
     */
    private String pid;

}
