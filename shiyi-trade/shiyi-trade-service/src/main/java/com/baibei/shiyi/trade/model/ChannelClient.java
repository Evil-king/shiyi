package com.baibei.shiyi.trade.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "tbl_channel_client")
public class ChannelClient {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 客户端类型，IOS=IOS端，Android=Android端，H5=H5端，Smart=小程序端
     */
    @Column(name = "client_type")
    private String clientType;

    /**
     * 版本
     */
    private String version;

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
     * 是否上架，1=上架，0=下架
     */
    private Integer updown;

    /**
     * 成交时间
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
     * 获取客户端类型，IOS=IOS端，Android=Android端，H5=H5端，Smart=小程序端
     *
     * @return client_type - 客户端类型，IOS=IOS端，Android=Android端，H5=H5端，Smart=小程序端
     */
    public String getClientType() {
        return clientType;
    }

    /**
     * 设置客户端类型，IOS=IOS端，Android=Android端，H5=H5端，Smart=小程序端
     *
     * @param clientType 客户端类型，IOS=IOS端，Android=Android端，H5=H5端，Smart=小程序端
     */
    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    /**
     * 获取版本
     *
     * @return version - 版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置版本
     *
     * @param version 版本
     */
    public void setVersion(String version) {
        this.version = version;
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
     * 获取是否上架，1=上架，0=下架
     *
     * @return updown - 是否上架，1=上架，0=下架
     */
    public Integer getUpdown() {
        return updown;
    }

    /**
     * 设置是否上架，1=上架，0=下架
     *
     * @param updown 是否上架，1=上架，0=下架
     */
    public void setUpdown(Integer updown) {
        this.updown = updown;
    }

    /**
     * 获取成交时间
     *
     * @return create_time - 成交时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置成交时间
     *
     * @param createTime 成交时间
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