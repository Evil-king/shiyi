package com.baibei.shiyi.trade.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_risk_product")
public class RiskProduct {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品交易编码
     */
    @Column(name = "product_trade_no")
    private String productTradeNo;

    /**
     * open=开启交易，close=暂停交易
     */
    @Column(name = "open_status")
    private String openStatus;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人
     */
    private String modifier;

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
     * 获取open=开启交易，close=暂停交易
     *
     * @return open_status - open=开启交易，close=暂停交易
     */
    public String getOpenStatus() {
        return openStatus;
    }

    /**
     * 设置open=开启交易，close=暂停交易
     *
     * @param openStatus open=开启交易，close=暂停交易
     */
    public void setOpenStatus(String openStatus) {
        this.openStatus = openStatus;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
     * 获取修改人
     *
     * @return modifier - 修改人
     */
    public String getModifier() {
        return modifier;
    }

    /**
     * 设置修改人
     *
     * @param modifier 修改人
     */
    public void setModifier(String modifier) {
        this.modifier = modifier;
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