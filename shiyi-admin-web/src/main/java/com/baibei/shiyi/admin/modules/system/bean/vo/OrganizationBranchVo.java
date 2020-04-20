package com.baibei.shiyi.admin.modules.system.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Data
public class OrganizationBranchVo extends BaseRowModel {

    /**
     * 机构Id
     */
    private Long id;

    //机构编号
    @ExcelProperty(index = 1, value = "分公司编号")
    private String orgCode;
    //机构名称
    @ExcelProperty(index = 2, value = "分公司名称")
    private String orgName;
    //商业类型  personal,个人,enterprise,企业
    private String businessType;

    @ExcelProperty(index = 3, value = "所属机构")
    private String organizationName;

    @ExcelProperty(index = 4, value = "实体")
    private String businessTypeText;

    @ExcelProperty(index = 5, value = "姓名")
    private String name;

    @ExcelProperty(index = 6, value = "身份证号码")
    private String idCard;

    @ExcelProperty(index = 7, value = "公司名称")
    private String companyName;


    @ExcelProperty(index = 8, value = "工商营业执照")
    private String businessLicense;

    @ExcelProperty(index = 9, value = "组织分公司代码证")
    private String organizationCodeCertificate;

    @ExcelProperty(index = 10, value = "税务登记证")
    private String taxRegistrationCertificate;

    /**
     * 机构类型
     */
    private String orgType;

    /**
     * 上一级父类Id
     */
    private Long pid;

    @ExcelProperty(index = 11, value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 分公司关联代理Id
     */
    List<List<Long>> branchAssociateId;
}
