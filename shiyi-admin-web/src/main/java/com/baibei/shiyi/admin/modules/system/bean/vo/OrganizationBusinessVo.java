package com.baibei.shiyi.admin.modules.system.bean.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * @author: hyc
 * @date: 2019/10/10 10:26
 * @description:
 */
@Data
public class OrganizationBusinessVo extends BaseRowModel {
    // 机构id
    private Long id;
    //机构编号
    @ExcelProperty(index = 1, value = "事业部编号")
    private String orgCode;
    //机构名称
    @ExcelProperty(index = 2, value = "事业部名称")
    private String orgName;
    //商业类型  personal,个人,enterprise,企业

    @ExcelProperty(index = 3, value = "实体")
    private String businessTypeText;

    private String businessType;
    //姓名
    @ExcelProperty(index = 4, value = "姓名")
    private String name;
    //身份证号
    @ExcelProperty(index = 5, value = "身份证")
    private String idCard;
    //公司名称
    @ExcelProperty(index = 6, value = "公司名称")
    private String companyName;
    //工商营业证
    @ExcelProperty(index = 7, value = "工商营业证")
    private String businessLicense;
    //组织机构代码证
    @ExcelProperty(index = 8, value = "组织机构代码证")
    private String organizationCodeCertificate;
    //税务登记证
    @ExcelProperty(index = 9, value = "税务登记证")
    private String taxRegistrationCertificate;
    //返还账号
    @ExcelProperty(index = 10, value = "返回账号")
    private String returnAccount;
    //返佣冻结比例
    @ExcelProperty(index = 11, value = "返佣冻结比列")
    private Float rebateFreezeRatio;
    //创建时间
    @ExcelProperty(index = 12, value = "创建时间", format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //上级机构名称
    private String parentName;

    /**
     * 机构类型
     */
    private String orgType;

    private Long pid;
}
