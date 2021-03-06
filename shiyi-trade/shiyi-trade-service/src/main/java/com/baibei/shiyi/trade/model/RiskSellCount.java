package com.baibei.shiyi.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_risk_sellcount")
public class RiskSellCount {
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
     * 冗余交易商名称
     */
    @Column(name = "customer_name")
    private String customerName;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * 冗余商品名称
     */
    @Column(name = "product_name")
    private String productName;

    /**
     * 可卖数量
     */
    @Column(name = "sell_count")
    private Integer sellCount;

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
     * 获取冗余交易商名称
     *
     * @return customer_name - 冗余交易商名称
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置冗余交易商名称
     *
     * @param customerName 冗余交易商名称
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
     * 获取冗余商品名称
     *
     * @return product_name - 冗余商品名称
     */
    public String getProductName() {
        return productName;
    }

    /**
     * 设置冗余商品名称
     *
     * @param productName 冗余商品名称
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * 获取可卖数量
     *
     * @return sell_count - 可卖数量
     */
    public Integer getSellCount() {
        return sellCount;
    }

    /**
     * 设置可卖数量
     *
     * @param sellCount 可卖数量
     */
    public void setSellCount(Integer sellCount) {
        this.sellCount = sellCount;
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
}