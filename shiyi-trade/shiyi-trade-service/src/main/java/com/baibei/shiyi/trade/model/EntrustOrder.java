package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_entrust_order")
public class EntrustOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 交易商编码
     */
    @Column(name = "customer_no")
    private String customerNo;

    /**
     * 交易商姓名，冗余
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 委托单单号
     */
    @Column(name = "entrust_no")
    private String entrustNo;

    /**
     * 委托时间
     */
    @Column(name = "entrust_time")
    private Date entrustTime;

    /**
     * 委托方向，buy=挂牌买入，sell=挂牌卖出
     */
    private String direction;

    /**
     * 委托价格
     */
    private BigDecimal price;

    /**
     * 委托数量
     */
    @Column(name = "entrust_count")
    private Integer entrustCount;

    /**
     * 未成交数量
     */
    @Column(name = "wait_count")
    private Integer waitCount;

    /**
     * 成交数量
     */
    @Column(name = "deal_count")
    private Integer dealCount;

    /**
     * 撤销数量
     */
    @Column(name = "revoke_count")
    private Integer revokeCount;

    /**
     * 委托结果，wait_deal=待成交，all_deal=全部成交，system_revoke=系统撤单, customer_revoke=客户撤单
     */
    private String result;

    /**
     * 是否匿名(1:匿名，0:不匿名)
     */
    @Column(name = "anonymous_flag")
    private Byte anonymousFlag;

    /**
     * 变动的资金/持仓
     */
    @Column(name = "change_amount")
    private BigDecimal changeAmount;

    /**
     * 挂牌买入单个持仓的手续费
     */
    @Column(name = "buy_fee")
    private BigDecimal buyFee;

    /**
     * 成交单状态，init=初始化，success=委托成功，fail=委托失败
     */
    private String status;

    /**
     * 委托失败原因
     */
    private String reason;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "modify_time")
    private Date modifyTime;

    /**
     * 是否删除(1:正常，0:删除)
     */
    private Byte flag;

    /**
     * 获取主键
     *
     * @return id - 主键
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键
     *
     * @param id 主键
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取交易商编码
     *
     * @return customer_no - 交易商编码
     */
    public String getCustomerNo() {
        return customerNo;
    }

    /**
     * 设置交易商编码
     *
     * @param customerNo 交易商编码
     */
    public void setCustomerNo(String customerNo) {
        this.customerNo = customerNo;
    }

    /**
     * 获取交易商姓名，冗余
     *
     * @return customer_name - 交易商姓名，冗余
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置交易商姓名，冗余
     *
     * @param customerName 交易商姓名，冗余
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取商品交易编码
     *
     * @return product_trade_no - 商品交易编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置商品交易编码
     *
     * @param productTradeNo 商品交易编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取委托单单号
     *
     * @return entrust_no - 委托单单号
     */
    public String getEntrustNo() {
        return entrustNo;
    }

    /**
     * 设置委托单单号
     *
     * @param entrustNo 委托单单号
     */
    public void setEntrustNo(String entrustNo) {
        this.entrustNo = entrustNo;
    }

    /**
     * 获取委托时间
     *
     * @return entrust_time - 委托时间
     */
    public Date getEntrustTime() {
        return entrustTime;
    }

    /**
     * 设置委托时间
     *
     * @param entrustTime 委托时间
     */
    public void setEntrustTime(Date entrustTime) {
        this.entrustTime = entrustTime;
    }

    /**
     * 获取委托方向，buy=挂牌买入，sell=挂牌卖出
     *
     * @return direction - 委托方向，buy=挂牌买入，sell=挂牌卖出
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 设置委托方向，buy=挂牌买入，sell=挂牌卖出
     *
     * @param direction 委托方向，buy=挂牌买入，sell=挂牌卖出
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * 获取委托价格
     *
     * @return price - 委托价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置委托价格
     *
     * @param price 委托价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取委托数量
     *
     * @return entrust_count - 委托数量
     */
    public Integer getEntrustCount() {
        return entrustCount;
    }

    /**
     * 设置委托数量
     *
     * @param entrustCount 委托数量
     */
    public void setEntrustCount(Integer entrustCount) {
        this.entrustCount = entrustCount;
    }

    /**
     * 获取未成交数量
     *
     * @return wait_count - 未成交数量
     */
    public Integer getWaitCount() {
        return waitCount;
    }

    /**
     * 设置未成交数量
     *
     * @param waitCount 未成交数量
     */
    public void setWaitCount(Integer waitCount) {
        this.waitCount = waitCount;
    }

    /**
     * 获取成交数量
     *
     * @return deal_count - 成交数量
     */
    public Integer getDealCount() {
        return dealCount;
    }

    /**
     * 设置成交数量
     *
     * @param dealCount 成交数量
     */
    public void setDealCount(Integer dealCount) {
        this.dealCount = dealCount;
    }

    /**
     * 获取撤销数量
     *
     * @return revoke_count - 撤销数量
     */
    public Integer getRevokeCount() {
        return revokeCount;
    }

    /**
     * 设置撤销数量
     *
     * @param revokeCount 撤销数量
     */
    public void setRevokeCount(Integer revokeCount) {
        this.revokeCount = revokeCount;
    }

    /**
     * 获取委托结果，wait_deal=待成交，all_deal=全部成交，system_revoke=系统撤单, customer_revoke=客户撤单
     *
     * @return result - 委托结果，wait_deal=待成交，all_deal=全部成交，system_revoke=系统撤单, customer_revoke=客户撤单
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置委托结果，wait_deal=待成交，all_deal=全部成交，system_revoke=系统撤单, customer_revoke=客户撤单
     *
     * @param result 委托结果，wait_deal=待成交，all_deal=全部成交，system_revoke=系统撤单, customer_revoke=客户撤单
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 获取是否匿名(1:匿名，0:不匿名)
     *
     * @return anonymous_flag - 是否匿名(1:匿名，0:不匿名)
     */
    public Byte getAnonymousFlag() {
        return anonymousFlag;
    }

    /**
     * 设置是否匿名(1:匿名，0:不匿名)
     *
     * @param anonymousFlag 是否匿名(1:匿名，0:不匿名)
     */
    public void setAnonymousFlag(Byte anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    /**
     * 获取成交单状态，init=初始化，success=委托成功，fail=委托失败
     *
     * @return status - 成交单状态，init=初始化，success=委托成功，fail=委托失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置成交单状态，init=初始化，success=委托成功，fail=委托失败
     *
     * @param status 成交单状态，init=初始化，success=委托成功，fail=委托失败
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取委托失败原因
     *
     * @return reason - 委托失败原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置委托失败原因
     *
     * @param reason 委托失败原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取是否删除(1:正常，0:删除)
     *
     * @return flag - 是否删除(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置是否删除(1:正常，0:删除)
     *
     * @param flag 是否删除(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }

    public BigDecimal getChangeAmount() {
        return changeAmount;
    }

    public void setChangeAmount(BigDecimal changeAmount) {
        this.changeAmount = changeAmount;
    }

    public BigDecimal getBuyFee() {
        return buyFee;
    }

    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }
}