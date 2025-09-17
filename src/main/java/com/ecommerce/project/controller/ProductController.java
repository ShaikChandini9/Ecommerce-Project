package com.ecommerce.project.controller;

import com.ecommerce.project.request.ProductRequest;
import com.ecommerce.project.request.StockUpdateRequest;
import com.ecommerce.project.response.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService service;

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @PutMapping("/add-stock/{id}")
    public ResponseEntity<ProductResponse> addStock(@PathVariable Long id, @Valid @RequestBody StockUpdateRequest stockrequest) {
        return ResponseEntity.ok(service.addStock(id, stockrequest.getAddQuantity()));
    }

    @GetMapping("/get-by-id/{id}")
    public ResponseEntity<ProductResponse> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<ProductResponse>> list() {
        return ResponseEntity.ok(service.list());
    }

    @DeleteMapping("/delete-by-id/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        service.deleteProduct(productId);
        return ResponseEntity.ok("Product deleted successfully with ID: " + productId);
    }
}
