package com.ecommerce.project.request;

import com.ecommerce.project.enumpackage.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {

    @NotNull
    private Long orderId;

    @NotNull
    private PaymentMode mode;

    @NotNull
    @DecimalMin("0.0")
    private Double amount;

}
