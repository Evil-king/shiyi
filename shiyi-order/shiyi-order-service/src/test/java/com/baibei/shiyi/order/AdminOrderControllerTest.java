package com.baibei.shiyi.order;

import com.alibaba.fastjson.JSONObject;
import com.baibei.shiyi.common.tool.constants.Constants;
import com.baibei.shiyi.order.feign.base.dto.AdminOrderDto;
import com.baibei.shiyi.order.model.Order;
import com.baibei.shiyi.order.model.OrderItem;
import com.baibei.shiyi.order.model.OrderRefund;
import com.baibei.shiyi.order.service.IOrderItemService;
import com.baibei.shiyi.order.service.IOrderRefundService;
import com.baibei.shiyi.order.service.IOrderService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderServiceApplication.class)
@Transactional
public class AdminOrderControllerTest {


    protected MockMvc mockMvc;

    @Autowired
    protected WebApplicationContext wac;

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IOrderItemService orderItemService;

    @Autowired
    private IOrderRefundService orderRefundService;

    private OrderItem orderItem;


    @Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
        List<OrderItem> orderItemList = orderItemService.findAll();
        orderItemList = orderItemList.stream().filter
                (result -> (result.getRefundStatus() == null && result.getStatus().equals(Constants.MallOrderStatus.UNDELIVERY)))
                .collect(Collectors.toList());
        orderItem = orderItemList.get((int) (orderItemList.size() * Math.random()));
    }


    @Test
    public void testPageList() throws Exception {
        AdminOrderDto orderDto = new AdminOrderDto();
        orderDto.setCurrentPage(1);
        orderDto.setPageSize(20);
        mockMvc.perform(post("/admin/order/pageList")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(orderDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testGetById() throws Exception {
        List<Order> orderList = orderService.findAll();
        Assert.assertEquals(orderList.isEmpty(), Boolean.FALSE);
        AdminOrderDto orderDto = new AdminOrderDto();
        orderDto.setOrderId(orderList.get((int) (orderList.size() * Math.random())).getId());
        mockMvc.perform(post("/admin/order/getById")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(orderDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAddLogistics() throws Exception {
        List<OrderItem> orderItems = orderItemService.findAll();
        Assert.assertEquals(orderItems.isEmpty(), Boolean.FALSE);
        AdminOrderDto orderDto = new AdminOrderDto();
        orderDto.setDeliveryCompany("安能快递");
        orderDto.setDeliveryNo("123456");
        OrderItem orderItem = orderItems.get((int) (orderItems.size() * Math.random()));
        orderDto.setOrderItemNo(orderItem.getOrderItemNo());
        mockMvc.perform(post("/admin/order/addLogistics")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(orderDto))).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
        OrderItem result = orderItemService.findById(orderItem.getId());
        Assert.assertEquals(result.getDeliveryNo(), orderDto.getDeliveryNo());
    }

    @Test
    public void testOrderCount() throws Exception {
        mockMvc.perform(post("/admin/order/orderCount")
                .contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * 关闭子订单
     */
    @Test
    public void testCloseItemOrder() throws Exception {

        AdminOrderDto orderDto = new AdminOrderDto();
        orderDto.setOrderItemNo(orderItem.getOrderItemNo());
        orderDto.setReason("订单有问题");
        mockMvc.perform(post("/admin/order/cloneItemOrder")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(orderDto))).andExpect(status().isOk());
        OrderItem result = orderItemService.findByOrderItemNo(orderItem.getOrderItemNo());
        Assert.assertEquals(result.getRefundStatus(), Constants.RefundStatus.APPLY_REFUND);
    }


    /**
     * 测试订单退款接受
     */
    @Test
    public void testItemRefundAgree() throws Exception {

        // stop 1进行关闭子订单
        testCloseItemOrder();
        OrderRefund refund = orderRefundService.findBy("orderItemId", orderItem.getId());
        // stop 2根据orderItemId去查询退款订单
        AdminOrderDto orderDto = new AdminOrderDto();
        orderDto.setRefundId(refund.getId());
        mockMvc.perform(post("/admin/order/itemRefundAgree")
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(orderDto))).andExpect(status().isOk());
    }

    /**
     * 测试订单拒绝
     */
    @Test
    public void testItemRefundReject() throws Exception {
        // stop 1 进行关闭子订单
        testCloseItemOrder();
        OrderRefund refund = orderRefundService.findBy("orderItemId", orderItem.getId());
        // stop 2根据orderItemId去查询退款订单
        AdminOrderDto orderDto = new AdminOrderDto();
        orderDto.setRefundId(refund.getId());
        // stop 2 根据orderItmerId 去查询子订单
        mockMvc.perform(post("/admin/order/itemRefundReject")
                .contentType(MediaType.APPLICATION_JSON_UTF8).contentType(JSONObject.toJSONString(orderDto))).andExpect(status().isOk());
    }



}
