package com.ecommerce.project.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String title;
    private String description;
    private Double price;
    private Integer quantity;
}
