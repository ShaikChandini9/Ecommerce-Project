package com.ecommerce.project.service;

import com.ecommerce.project.request.CustomerRequest;
import com.ecommerce.project.response.CustomerResponse;

import java.util.List;

public interface CustomerService {
    CustomerResponse create(CustomerRequest request);
    CustomerResponse update(Long id, CustomerRequest request);
    CustomerResponse get(Long id);
    List<CustomerResponse> list();
    void delete(Long id);
}
