package com.baibei.shiyi.trade.model;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_tra_trade_config")
public class TradeConfig {
    /**
     * 主键id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * T+N天(交易解锁时间)
     */
    @Column(name = "trade_frozen_day")
    private Integer tradeFrozenDay;

    /**
     * T+N天(兑换解锁时间)
     */
    @Column(name = "exchange_frozen_day")
    private Integer exchangeFrozenDay;

    /**
     * 收盘价计算
     */
    @Column(name = "closing_price_calculation")
    private Integer closingPriceCalculation;

    /**
     * 买入手续费
     */
    @Column(name = "buy_fee")
    private BigDecimal buyFee;

    /**
     * 卖出手续费
     */
    @Column(name = "sell_fee")
    private BigDecimal sellFee;

    /**
     * 最优价成交，1=是，0=否
     */
    @Column(name = "best_price_deal")
    private Byte bestPriceDeal;

    /**
     * 行情区是否显示，1=是，0=否
     */
    @Column(name = "market_area_show")
    private Byte marketAreaShow;

    /**
     * 报价区是否显示，1=是，0=否
     */
    @Column(name = "quotation_area_show")
    private Byte quotationAreaShow;

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
     * 获取主键id
     *
     * @return id - 主键id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置主键id
     *
     * @param id 主键id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取T+N天(交易解锁时间)
     *
     * @return trade_frozen_day - T+N天(交易解锁时间)
     */
    public Integer getTradeFrozenDay() {
        return tradeFrozenDay;
    }

    /**
     * 设置T+N天(交易解锁时间)
     *
     * @param tradeFrozenDay T+N天(交易解锁时间)
     */
    public void setTradeFrozenDay(Integer tradeFrozenDay) {
        this.tradeFrozenDay = tradeFrozenDay;
    }

    /**
     * 获取T+N天(兑换解锁时间)
     *
     * @return exchange_frozen_day - T+N天(兑换解锁时间)
     */
    public Integer getExchangeFrozenDay() {
        return exchangeFrozenDay;
    }

    /**
     * 设置T+N天(兑换解锁时间)
     *
     * @param exchangeFrozenDay T+N天(兑换解锁时间)
     */
    public void setExchangeFrozenDay(Integer exchangeFrozenDay) {
        this.exchangeFrozenDay = exchangeFrozenDay;
    }

    /**
     * 获取收盘价计算
     *
     * @return closing_price_calculation - 收盘价计算
     */
    public Integer getClosingPriceCalculation() {
        return closingPriceCalculation;
    }

    /**
     * 设置收盘价计算
     *
     * @param closingPriceCalculation 收盘价计算
     */
    public void setClosingPriceCalculation(Integer closingPriceCalculation) {
        this.closingPriceCalculation = closingPriceCalculation;
    }

    /**
     * 获取买入手续费
     *
     * @return buy_fee - 买入手续费
     */
    public BigDecimal getBuyFee() {
        return buyFee;
    }

    /**
     * 设置买入手续费
     *
     * @param buyFee 买入手续费
     */
    public void setBuyFee(BigDecimal buyFee) {
        this.buyFee = buyFee;
    }

    /**
     * 获取卖出手续费
     *
     * @return sell_fee - 卖出手续费
     */
    public BigDecimal getSellFee() {
        return sellFee;
    }

    /**
     * 设置卖出手续费
     *
     * @param sellFee 卖出手续费
     */
    public void setSellFee(BigDecimal sellFee) {
        this.sellFee = sellFee;
    }

    /**
     * 获取最优价成交，1=是，0=否
     *
     * @return best_price_deal - 最优价成交，1=是，0=否
     */
    public Byte getBestPriceDeal() {
        return bestPriceDeal;
    }

    /**
     * 设置最优价成交，1=是，0=否
     *
     * @param bestPriceDeal 最优价成交，1=是，0=否
     */
    public void setBestPriceDeal(Byte bestPriceDeal) {
        this.bestPriceDeal = bestPriceDeal;
    }

    /**
     * 获取行情区是否显示，1=是，0=否
     *
     * @return market_area_show - 行情区是否显示，1=是，0=否
     */
    public Byte getMarketAreaShow() {
        return marketAreaShow;
    }

    /**
     * 设置行情区是否显示，1=是，0=否
     *
     * @param marketAreaShow 行情区是否显示，1=是，0=否
     */
    public void setMarketAreaShow(Byte marketAreaShow) {
        this.marketAreaShow = marketAreaShow;
    }

    /**
     * 获取报价区是否显示，1=是，0=否
     *
     * @return quotation_area_show - 报价区是否显示，1=是，0=否
     */
    public Byte getQuotationAreaShow() {
        return quotationAreaShow;
    }

    /**
     * 设置报价区是否显示，1=是，0=否
     *
     * @param quotationAreaShow 报价区是否显示，1=是，0=否
     */
    public void setQuotationAreaShow(Byte quotationAreaShow) {
        this.quotationAreaShow = quotationAreaShow;
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