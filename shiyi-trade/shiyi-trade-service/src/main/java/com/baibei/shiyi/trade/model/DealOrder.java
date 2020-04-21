package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_deal_order")
public class DealOrder {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 成交单单号
     */
    @Column(name = "deal_no")
    private String dealNo;

    /**
     * 关联委托单单号
     */
    @Column(name = "entrust_no")
    private String entrustNo;

    /**
     * 冗余商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 摘牌方客户编号
     */
    private String delister;

    /**
     * 被摘牌方客户编码
     */
    @Column(name = "be_delister")
    private String beDelister;

    /**
     * 摘牌方手续费
     */
    @Column(name = "delister_fee")
    private BigDecimal delisterFee;

    /**
     * 被摘牌方手续费
     */
    @Column(name = "be_delister_fee")
    private BigDecimal beDelisterFee;

    /**
     * 成交类型，buy=摘牌买入，sell=摘牌卖出
     */
    private String direction;

    /**
     * 成交价格
     */
    private BigDecimal price;

    /**
     * 成交数量
     */
    private Integer count;

    /**
     * 成交单状态，init=初始化，success=摘牌成功，fail=摘牌失败
     */
    private String status;

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
     * 获取成交单单号
     *
     * @return deal_no - 成交单单号
     */
    public String getDealNo() {
        return dealNo;
    }

    /**
     * 设置成交单单号
     *
     * @param dealNo 成交单单号
     */
    public void setDealNo(String dealNo) {
        this.dealNo = dealNo;
    }

    /**
     * 获取关联委托单单号
     *
     * @return entrust_no - 关联委托单单号
     */
    public String getEntrustNo() {
        return entrustNo;
    }

    /**
     * 设置关联委托单单号
     *
     * @param entrustNo 关联委托单单号
     */
    public void setEntrustNo(String entrustNo) {
        this.entrustNo = entrustNo;
    }

    /**
     * 获取冗余商品交易编码
     *
     * @return product_trade_no - 冗余商品交易编码
     */
    public String getProductTradeNo() {
        return productTradeNo;
    }

    /**
     * 设置冗余商品交易编码
     *
     * @param productTradeNo 冗余商品交易编码
     */
    public void setProductTradeNo(String productTradeNo) {
        this.productTradeNo = productTradeNo;
    }

    /**
     * 获取摘牌方客户编号
     *
     * @return delister - 摘牌方客户编号
     */
    public String getDelister() {
        return delister;
    }

    /**
     * 设置摘牌方客户编号
     *
     * @param delister 摘牌方客户编号
     */
    public void setDelister(String delister) {
        this.delister = delister;
    }

    /**
     * 获取被摘牌方客户编码
     *
     * @return be_delister - 被摘牌方客户编码
     */
    public String getBeDelister() {
        return beDelister;
    }

    /**
     * 设置被摘牌方客户编码
     *
     * @param beDelister 被摘牌方客户编码
     */
    public void setBeDelister(String beDelister) {
        this.beDelister = beDelister;
    }

    /**
     * 获取摘牌方手续费
     *
     * @return delister_fee - 摘牌方手续费
     */
    public BigDecimal getDelisterFee() {
        return delisterFee;
    }

    /**
     * 设置摘牌方手续费
     *
     * @param delisterFee 摘牌方手续费
     */
    public void setDelisterFee(BigDecimal delisterFee) {
        this.delisterFee = delisterFee;
    }

    /**
     * 获取被摘牌方手续费
     *
     * @return be_delister_fee - 被摘牌方手续费
     */
    public BigDecimal getBeDelisterFee() {
        return beDelisterFee;
    }

    /**
     * 设置被摘牌方手续费
     *
     * @param beDelisterFee 被摘牌方手续费
     */
    public void setBeDelisterFee(BigDecimal beDelisterFee) {
        this.beDelisterFee = beDelisterFee;
    }

    /**
     * 获取成交类型，buy=摘牌买入，sell=摘牌卖出
     *
     * @return direction - 成交类型，buy=摘牌买入，sell=摘牌卖出
     */
    public String getDirection() {
        return direction;
    }

    /**
     * 设置成交类型，buy=摘牌买入，sell=摘牌卖出
     *
     * @param direction 成交类型，buy=摘牌买入，sell=摘牌卖出
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * 获取成交价格
     *
     * @return price - 成交价格
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置成交价格
     *
     * @param price 成交价格
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取成交数量
     *
     * @return count - 成交数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置成交数量
     *
     * @param count 成交数量
     */
    public void setCount(Integer count) {
        this.count = count;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}