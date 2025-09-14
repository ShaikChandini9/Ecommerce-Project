package com.ecommerce.project.service;


import com.ecommerce.project.request.OrderCreateRequest;
import com.ecommerce.project.request.OrderShippingUpdateRequest;
import com.ecommerce.project.response.OrderResponse;

public interface OrderService {

    OrderResponse create(OrderCreateRequest request);
    OrderResponse cancel(Long orderId);
    OrderResponse updateShipping(Long orderId, OrderShippingUpdateRequest request);
    OrderResponse get(Long id);
}
