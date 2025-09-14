package com.ecommerce.project.service;


import com.ecommerce.project.request.PaymentRequest;
import com.ecommerce.project.response.PaymentResponse;

public interface PaymentService {

    PaymentResponse pay(PaymentRequest request);
    Double getTotalPaidByCustomer(Long customerId);
}
