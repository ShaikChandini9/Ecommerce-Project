package com.ecommerce.project.request;

import lombok.Data;

@Data
public class WishlistMoveRequest {

    private Long customerId;
    private Long productId;
    private Integer quantity;
}
