package com.baibei.shiyi.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * (0标识顶层)
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 分类标题（类型名称）
     */
    private String title;

    /**
     * 排序
     */
    private Integer seq;

    /**
     * 是否是末端类目（1：是；0：否）
     */
    private Byte end;

    /**
     * 图片
     */
    private String img;

    /**
     * 是否隐藏（show：显示；hidden：隐藏）
     */
    @Column(name = "show_status")
    private String showStatus;

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
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取(0标识顶层)
     *
     * @return parent_id - (0标识顶层)
     */
    public Long getParentId() {
        return parentId;
    }

    /**
     * 设置(0标识顶层)
     *
     * @param parentId (0标识顶层)
     */
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取分类标题（类型名称）
     *
     * @return title - 分类标题（类型名称）
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置分类标题（类型名称）
     *
     * @param title 分类标题（类型名称）
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取排序
     *
     * @return seq - 排序
     */
    public Integer getSeq() {
        return seq;
    }

    /**
     * 设置排序
     *
     * @param seq 排序
     */
    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    /**
     * 获取是否是末端类目（1：是；0：否）
     *
     * @return end - 是否是末端类目（1：是；0：否）
     */
    public Byte getEnd() {
        return end;
    }

    /**
     * 设置是否是末端类目（1：是；0：否）
     *
     * @param end 是否是末端类目（1：是；0：否）
     */
    public void setEnd(Byte end) {
        this.end = end;
    }

    /**
     * 获取是否隐藏（show：显示；hidden：隐藏）
     *
     * @return show_status - 是否隐藏（show：显示；hidden：隐藏）
     */
    public String getShowStatus() {
        return showStatus;
    }

    /**
     * 设置是否隐藏（show：显示；hidden：隐藏）
     *
     * @param showStatus 是否隐藏（show：显示；hidden：隐藏）
     */
    public void setShowStatus(String showStatus) {
        this.showStatus = showStatus;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}