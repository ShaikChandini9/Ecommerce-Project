package com.ecommerce.project.controller;

import com.ecommerce.project.request.PaymentRequest;
import com.ecommerce.project.response.PaymentResponse;
import com.ecommerce.project.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/payment")
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/create")
    public ResponseEntity<PaymentResponse> pay(@Valid @RequestBody PaymentRequest request) {
        return ResponseEntity.ok(service.pay(request));
    }
}
