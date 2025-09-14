package com.ecommerce.project.controller;

import com.ecommerce.project.request.OrderCreateRequest;
import com.ecommerce.project.request.OrderShippingUpdateRequest;
import com.ecommerce.project.response.OrderResponse;
import com.ecommerce.project.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/order")
public class OrderController {

    private final OrderService service;

    @PostMapping("/create")
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderCreateRequest req) {
        return ResponseEntity.ok(service.create(req));
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<OrderResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @PostMapping("/update-shipping/{id}")
    public ResponseEntity<OrderResponse> updateShipping(@PathVariable("id") Long orderId,
            @RequestBody OrderShippingUpdateRequest req) {

        return ResponseEntity.ok(service.updateShipping(orderId, req));
    }

    @PostMapping("/cancel-with-id/{id}")
    public ResponseEntity<OrderResponse> cancel(@PathVariable("id") Long id) {
        return ResponseEntity.ok(service.cancel(id));
    }

}
