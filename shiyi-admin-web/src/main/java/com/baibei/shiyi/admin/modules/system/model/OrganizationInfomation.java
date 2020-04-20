package com.baibei.shiyi.admin.modules.system.model;

import javax.persistence.*;

@Table(name = "tbl_admin_organization_infomation")
public class OrganizationInfomation {
    /**
     * 自增长主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商业类型(personal,个人,enterprise,企业)
     */
    @Column(name = "business_type")
    private String businessType;

    /**
     * 工商营业证
     */
    @Column(name = "business_license")
    private String businessLicense;

    /**
     * 组织机构代码证
     */
    @Column(name = "organization_code_certificate")
    private String organizationCodeCertificate;

    /**
     * 公司名称
     */
    @Column(name = "company_name")
    private String companyName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    @Column(name = "id_card")
    private String idCard;

    /**
     * 返还账号
     */
    @Column(name = "return_account")
    private String returnAccount;

    /**
     * 返佣冻结比例
     */
    @Column(name = "rebate_freeze_ratio")
    private Float rebateFreezeRatio;

    /**
     * 代理区域
     */
    @Column(name = "agent_area")
    private String agentArea;

    /**
     * 平级推荐人
     */
    private Long referrer;

    /**
     * 组织机构id
     */
    @Column(name = "organization_id")
    private Long organizationId;

    /**
     * 税务登记证
     */
    @Column(name = "tax_registration_certificate")
    private String taxRegistrationCertificate;

    /**
     * 代理城市
     */
    @Column(name = "agent_area_city")
    private String agentAreaCity;

    /**
     * 代理省
     */
    @Column(name = "agent_area_province")
    private String agentAreaProvince;

    /**
     * 代理区
     */
    @Column(name = "agent_area_region")
    private String agentAreaRegion;

    /**
     * 获取自增长主键
     *
     * @return id - 自增长主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置自增长主键
     *
     * @param id 自增长主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取商业类型(personal,个人,enterprise,企业)
     *
     * @return buinsess_type - 商业类型(personal,个人,enterprise,企业)
     */
    public String getBusinessType() {
        return businessType;
    }

    /**
     * 设置商业类型(personal,个人,enterprise,企业)
     *
     * @param businessType 商业类型(personal,个人,enterprise,企业)
     */
    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取工商营业证
     *
     * @return business_license - 工商营业证
     */
    public String getBusinessLicense() {
        return businessLicense;
    }

    /**
     * 设置工商营业证
     *
     * @param businessLicense 工商营业证
     */
    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

    /**
     * 获取组织机构代码证
     *
     * @return organization_code_certificate - 组织机构代码证
     */
    public String getOrganizationCodeCertificate() {
        return organizationCodeCertificate;
    }

    /**
     * 设置组织机构代码证
     *
     * @param organizationCodeCertificate 组织机构代码证
     */
    public void setOrganizationCodeCertificate(String organizationCodeCertificate) {
        this.organizationCodeCertificate = organizationCodeCertificate;
    }

    /**
     * 获取公司名称
     *
     * @return company_name - 公司名称
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * 设置公司名称
     *
     * @param companyName 公司名称
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * 获取姓名
     *
     * @return name - 姓名
     */
    public String getName() {
        return name;
    }

    /**
     * 设置姓名
     *
     * @param name 姓名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取身份证
     *
     * @return id_card - 身份证
     */
    public String getIdCard() {
        return idCard;
    }

    /**
     * 设置身份证
     *
     * @param idCard 身份证
     */
    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    /**
     * 获取返还账号
     *
     * @return return_account - 返还账号
     */
    public String getReturnAccount() {
        return returnAccount;
    }

    /**
     * 设置返还账号
     *
     * @param returnAccount 返还账号
     */
    public void setReturnAccount(String returnAccount) {
        this.returnAccount = returnAccount;
    }

    /**
     * 获取返佣冻结比例
     *
     * @return rebate_freeze_ratio - 返佣冻结比例
     */
    public Float getRebateFreezeRatio() {
        return rebateFreezeRatio;
    }

    /**
     * 设置返佣冻结比例
     *
     * @param rebateFreezeRatio 返佣冻结比例
     */
    public void setRebateFreezeRatio(Float rebateFreezeRatio) {
        this.rebateFreezeRatio = rebateFreezeRatio;
    }

    /**
     * 获取代理区域
     *
     * @return agent_area - 代理区域
     */
    public String getAgentArea() {
        return agentArea;
    }

    /**
     * 设置代理区域
     *
     * @param agentArea 代理区域
     */
    public void setAgentArea(String agentArea) {
        this.agentArea = agentArea;
    }

    /**
     * 获取平级推荐人
     *
     * @return referrer - 平级推荐人
     */
    public Long getReferrer() {
        return referrer;
    }

    /**
     * 设置平级推荐人
     *
     * @param referrer 平级推荐人
     */
    public void setReferrer(Long referrer) {
        this.referrer = referrer;
    }

    /**
     * 获取组织机构id
     *
     * @return organization_id - 组织机构id
     */
    public Long getOrganizationId() {
        return organizationId;
    }

    /**
     * 设置组织机构id
     *
     * @param organizationId 组织机构id
     */
    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    /**
     * 获取税务登记证
     *
     * @return tax_registration_certificate - 税务登记证
     */
    public String getTaxRegistrationCertificate() {
        return taxRegistrationCertificate;
    }

    /**
     * 设置税务登记证
     *
     * @param taxRegistrationCertificate 税务登记证
     */
    public void setTaxRegistrationCertificate(String taxRegistrationCertificate) {
        this.taxRegistrationCertificate = taxRegistrationCertificate;
    }

    public String getAgentAreaCity() {
        return agentAreaCity;
    }

    public void setAgentAreaCity(String agentAreaCity) {
        this.agentAreaCity = agentAreaCity;
    }

    public String getAgentAreaProvince() {
        return agentAreaProvince;
    }

    public void setAgentAreaProvince(String agentAreaProvince) {
        this.agentAreaProvince = agentAreaProvince;
    }

    public String getAgentAreaRegion() {
        return agentAreaRegion;
    }

    public void setAgentAreaRegion(String agentAreaRegion) {
        this.agentAreaRegion = agentAreaRegion;
    }
}