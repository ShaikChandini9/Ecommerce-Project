package com.ecommerce.project.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {

    private Long id;
    private Long orderId;
    private String mode;
    private String status;
    private String confirmationCode;
    private Double amount;
    private LocalDateTime createdAt;

}
