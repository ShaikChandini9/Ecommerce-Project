package com.ecommerce.project.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Long id;
    private Long customerId;
    private Long productId;
    private Integer rating;
    private String comment;
}
