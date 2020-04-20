package com.baibei.shiyi.admin.modules.system.bean.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: hyc
 * @date: 2019/10/10 10:20
 * @description:
 */
@Data
public class OrganizationBusinessDto {
    /**
     * 组织机构编号
     */

    private Long id;


    private String orgCode;

    private String orgName;
    /**
     * 组织机构类型，business：事业部；organization：机构，branchOffice:分公司
     */
//    @NotNull(message = "组织机构类型不能为空")
    private String orgType;

    /**
     * 商业类型(personal:个人,enterprise:企业)
     */
    @NotNull(message = "实体类型不能为空")
    private String businessType;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 返佣账号
     */
    private String returnAccount;

    /**
     * 返佣冻结比例
     */
    private Float rebateFreezeRatio;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 工商营业证
     */
    private String businessLicense;

    /**
     * 组织机构代码证
     */
    private String organizationCodeCertificate;

    /**
     * 税务登记证
     */
    private String taxRegistrationCertificate;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     * 代理区域
     */
    private String agentArea;


    /**
     * 平级推荐人
     */
    private Long referrer;

    /**
     * 注册手机号
     */
    private String registerMobile;

    /**
     * 代理区域
     */
    private List<String> agentAreaList;


    /**
     * 分公司关联的代理
     */
    private List<List<Long>> branchAssociateId;


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
     * 客户编号
     */
    private String customerNO;

}
