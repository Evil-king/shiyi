package com.baibei.shiyi.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_group")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 商品组编码
     */
    @Column(name = "group_no")
    private String groupNo;

    /**
     * 组名
     */
    private String title;

    /**
     * 商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     */
    @Column(name = "group_type")
    private String groupType;

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
     * 获取商品组编码
     *
     * @return group_no - 商品组编码
     */
    public String getGroupNo() {
        return groupNo;
    }

    /**
     * 设置商品组编码
     *
     * @param groupNo 商品组编码
     */
    public void setGroupNo(String groupNo) {
        this.groupNo = groupNo;
    }

    /**
     * 获取组名
     *
     * @return title - 组名
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置组名
     *
     * @param title 组名
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     *
     * @return group_type - 商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     */
    public String getGroupType() {
        return groupType;
    }

    /**
     * 设置商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     *
     * @param groupType 商品组类型（common=普通类型，hot=热门推荐，new=新品上市）
     */
    public void setGroupType(String groupType) {
        this.groupType = groupType;
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