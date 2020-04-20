package com.baibei.shiyi.account.service;
import com.baibei.shiyi.account.common.dto.EmpowermentDto;
import com.baibei.shiyi.account.feign.bean.dto.ChangeCustomerBeanDto;
import com.baibei.shiyi.account.feign.bean.dto.CustomerNoDto;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.feign.bean.vo.CustomerBeanVo;
import com.baibei.shiyi.account.model.CustomerBean;
import com.baibei.shiyi.common.core.mybatis.Service;
import com.baibei.shiyi.common.tool.api.ApiResult;

import java.math.BigDecimal;


/**
* @author: hyc
* @date: 2019/10/29 16:56:57
* @description: CustomerBean服务接口
*/
public interface ICustomerBeanService extends Service<CustomerBean> {
    /**
     * 获取豆账户信息
     * @param customerNoDto
     * @return
     */
    ApiResult<CustomerBeanVo> getBalance(CustomerNoDto customerNoDto);
    /**
     * 根据用户编号以及豆类型获取豆账户余额
     */
    ApiResult<CustomerBean> getBalanceByType(String customerNo,String beanType);

    ApiResult changeAmount(ChangeCustomerBeanDto changeAmountDto);

    /**
     * 根据积分类型判断该用户余额是否足够扣
     * @param fundType
     * @param customerNo
     * @param changeAmount
     * @return
     */
    ApiResult checkBalanceByType(String fundType, String customerNo, BigDecimal changeAmount);

    /**
     * 赋能通证操作
     * @param empowermentDto
     * @return
     */
    ApiResult empowerment(EmpowermentDto empowermentDto);

    /**
     * 每日释放待赋能余额
     * @return
     */
    ApiResult dayRelease();
}
