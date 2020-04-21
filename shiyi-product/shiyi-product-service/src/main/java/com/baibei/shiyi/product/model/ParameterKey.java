package com.baibei.shiyi.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_parameter_key")
public class ParameterKey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联后台类目id
     */
    @Column(name = "type_id")
    private Long typeId;

    /**
     * 参数名称
     */
    private String title;

    /**
     * 参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     */
    private String type;

    /**
     * 排序
     */
    private Integer seq;
    /**
     * 是否可编辑状态（allow=允许；unallow=不允许）
     */
    @Column(name = "edit_flag")
    private String editFlag;

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
     * 获取关联后台类目id
     *
     * @return type_id - 关联后台类目id
     */
    public Long getTypeId() {
        return typeId;
    }

    /**
     * 设置关联后台类目id
     *
     * @param typeId 关联后台类目id
     */
    public void setTypeId(Long typeId) {
        this.typeId = typeId;
    }

    /**
     * 获取参数名称
     *
     * @return title - 参数名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置参数名称
     *
     * @param title 参数名称
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     *
     * @return type - 参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     */
    public String getType() {
        return type;
    }

    /**
     * 设置参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     *
     * @param type 参数类型（date:日期类型，text:文本类型，select:下拉框类型，single:单选类型，multy：多选类型）
     */
    public void setType(String type) {
        this.type = type;
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

    public String getEditFlag() {
        return editFlag;
    }

    public void setEditFlag(String editFlag) {
        this.editFlag = editFlag;
    }
}