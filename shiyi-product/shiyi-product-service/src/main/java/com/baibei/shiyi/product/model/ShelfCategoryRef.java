package com.baibei.shiyi.product.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tbl_pro_shelf_categroy_ref")
public class ShelfCategoryRef {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 上架id
     */
    @Column(name = "shelf_id")
    private Long shelfId;

    /**
     * 前端类目id
     */
    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "modify_time")
    private Date modifyTime;

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
     * 获取上架id
     *
     * @return shelf_id - 上架id
     */
    public Long getShelfId() {
        return shelfId;
    }

    /**
     * 设置上架id
     *
     * @param shelfId 上架id
     */
    public void setShelfId(Long shelfId) {
        this.shelfId = shelfId;
    }

    /**
     * 获取前端类目id
     *
     * @return category_id - 前端类目id
     */
    public Long getCategoryId() {
        return categoryId;
    }

    /**
     * 设置前端类目id
     *
     * @param categoryId 前端类目id
     */
    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return flag
     */
    public Byte getFlag() {
        return flag;
    }

    /**
     * @param flag
     */
    public void setFlag(Byte flag) {
        this.flag = flag;
    }
}