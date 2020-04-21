package com.baibei.shiyi.product.service;

import com.baibei.shiyi.common.tool.page.MyPageInfo;
import com.baibei.shiyi.product.feign.bean.dto.GroupCurdDto;
import com.baibei.shiyi.product.feign.bean.dto.GroupDto;
import com.baibei.shiyi.product.feign.bean.dto.ProductGroupDto;
import com.baibei.shiyi.product.feign.bean.vo.GroupVo;
import com.baibei.shiyi.product.model.Group;
import com.baibei.shiyi.common.core.mybatis.Service;

import javax.servlet.ServletException;
import java.util.List;


/**
 * @author: Longer
 * @date: 2019/07/30 10:12:45
 * @description: Group服务接口
 */
public interface IGroupService extends Service<Group> {

    MyPageInfo<GroupVo> pageList(GroupDto groupDto);

    void save(GroupCurdDto groupDto);

    void update(GroupCurdDto groupDto);

    /**
     * 删除分组的商品
     *
     * @param productGroupDto
     */
    void deleteProductGroupRef(ProductGroupDto productGroupDto);

    Group findByTitle(String title);

    GroupVo getById(GroupCurdDto groupCurdDto);

    /**
     * 删除分组
     *
     * @param groupId
     */
    void deleteGroupById(Long groupId);

    /**
     * 查询List
     *
     * @param groupDto
     * @return
     */
    List<GroupVo> findByList(GroupDto groupDto);

    /**
     * 检查名字是否重复
     *
     * @param groupCurdDto
     * @return
     */
    boolean checkTitleRepeat(GroupCurdDto groupCurdDto);

    /**
     * 统计某组下面有多少个商品
     * @param groupId
     * @return
     */
    int sumGroupProduct(Long groupId);
}
