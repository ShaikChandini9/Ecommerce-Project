package com.ecommerce.project.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WishlistResponse {

    private Long wishlistId;
    private Long customerId;
    private Long productId;
    private String productTitle;
    private Double productPrice;
    private String message;
}
