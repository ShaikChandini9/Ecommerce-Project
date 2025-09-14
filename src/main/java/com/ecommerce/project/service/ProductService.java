package com.ecommerce.project.service;

import com.ecommerce.project.request.ProductRequest;
import com.ecommerce.project.response.ProductResponse;

import java.util.List;

public interface ProductService {

    ProductResponse create(ProductRequest request);
    ProductResponse addStock(Long productId, int addQty);
    ProductResponse get(Long id);
    List<ProductResponse> list();
}
