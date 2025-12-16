package com.example.order.admin.vo.req;

import com.example.order.common.vo.PageReq;

/**
 * 订单查询请求VO
 */
public class OrderQueryReq extends PageReq {

    private String orderNo;
    private Long userId;
    private String status;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderQueryReq{" +
                "orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", status='" + status + '\'' +
                ", pageNum=" + getPageNum() +
                ", pageSize=" + getPageSize() +
                '}';
    }
}
