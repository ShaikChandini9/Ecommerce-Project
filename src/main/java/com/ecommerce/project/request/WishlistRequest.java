package com.ecommerce.project.request;

import lombok.Data;

@Data
public class WishlistRequest {

    private Long customerId;
    private Long productId;
}
