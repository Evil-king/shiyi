package com.baibei.component.rocketmq.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_rocketmq_msg")
public class RocketmqMsg {
    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * topic
     */
    private String topic;

    /**
     * tags
     */
    private String tags;

    /**
     * 消息key
     */
    @Column(name = "message_key")
    private String messageKey;

    /**
     * 发送状态，success=发送成功，fail=发送失败
     */
    @Column(name = "send_status")
    private String sendStatus;

    /**
     * 处理状态，wait=待处理，processed=已处理
     */
    @Column(name = "process_status")
    private String processStatus;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 消息内容
     */
    @Column(name = "message_content")
    private String messageContent;

    /**
     * 发送失败的异常信息
     */
    private String reason;

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
     * 获取topic
     *
     * @return topic - topic
     */
    public String getTopic() {
        return topic;
    }

    /**
     * 设置topic
     *
     * @param topic topic
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * 获取tags
     *
     * @return tags - tags
     */
    public String getTags() {
        return tags;
    }

    /**
     * 设置tags
     *
     * @param tags tags
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * 获取消息key
     *
     * @return message_key - 消息key
     */
    public String getMessageKey() {
        return messageKey;
    }

    /**
     * 设置消息key
     *
     * @param messageKey 消息key
     */
    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    /**
     * 获取发送状态，success=发送成功，fail=发送失败
     *
     * @return send_status - 发送状态，success=发送成功，fail=发送失败
     */
    public String getSendStatus() {
        return sendStatus;
    }

    /**
     * 设置发送状态，success=发送成功，fail=发送失败
     *
     * @param sendStatus 发送状态，success=发送成功，fail=发送失败
     */
    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    /**
     * 获取处理状态，wait=待处理，processed=已处理
     *
     * @return process_status - 处理状态，wait=待处理，processed=已处理
     */
    public String getProcessStatus() {
        return processStatus;
    }

    /**
     * 设置处理状态，wait=待处理，processed=已处理
     *
     * @param processStatus 处理状态，wait=待处理，processed=已处理
     */
    public void setProcessStatus(String processStatus) {
        this.processStatus = processStatus;
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
     * 获取消息内容
     *
     * @return message_content - 消息内容
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * 设置消息内容
     *
     * @param messageContent 消息内容
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * 获取发送失败的异常信息
     *
     * @return reason - 发送失败的异常信息
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置发送失败的异常信息
     *
     * @param reason 发送失败的异常信息
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}