package com.baibei.shiyi.order.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_ord_pay_channel")
public class PayChannel {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 渠道编码
     */
    @Column(name = "channel_code")
    private String channelCode;

    /**
     * 渠道名称
     */
    @Column(name = "channel_name")
    private String channelName;

    /**
     * 上下架状态，up=上架，down=下架
     */
    private String status;

    /**
     * 渠道图片
     */
    @Column(name = "img_url")
    private String imgUrl;

    /**
     * 是否默认(1:是，0:否)
     */
    @Column(name = "as_default")
    private Byte asDefault;

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
     * 状态(1:正常，0:删除)
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
     * 获取渠道编码
     *
     * @return channel_code - 渠道编码
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * 设置渠道编码
     *
     * @param channelCode 渠道编码
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    /**
     * 获取渠道名称
     *
     * @return channel_name - 渠道名称
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * 设置渠道名称
     *
     * @param channelName 渠道名称
     */
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    /**
     * 获取上下架状态，up=上架，down=下架
     *
     * @return status - 上下架状态，up=上架，down=下架
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置上下架状态，up=上架，down=下架
     *
     * @param status 上下架状态，up=上架，down=下架
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取渠道图片
     *
     * @return img_url - 渠道图片
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * 设置渠道图片
     *
     * @param imgUrl 渠道图片
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    /**
     * 获取是否默认(1:是，0:否)
     *
     * @return as_default - 是否默认(1:是，0:否)
     */
    public Byte getAsDefault() {
        return asDefault;
    }

    /**
     * 设置是否默认(1:是，0:否)
     *
     * @param asDefault 是否默认(1:是，0:否)
     */
    public void setAsDefault(Byte asDefault) {
        this.asDefault = asDefault;
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
     * 获取状态(1:正常，0:删除)
     *
     * @return flag - 状态(1:正常，0:删除)
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * 设置状态(1:正常，0:删除)
     *
     * @param flag 状态(1:正常，0:删除)
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}