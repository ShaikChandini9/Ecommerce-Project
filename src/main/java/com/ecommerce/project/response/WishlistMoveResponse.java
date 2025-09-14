package com.ecommerce.project.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WishlistMoveResponse {

    private Long orderId;
    private Long customerId;
    private Long productId;
    private String productTitle;
    private Integer quantity;
    private Double totalPrice;
    private String message;
    private LocalDateTime orderCreatedAt;
}
