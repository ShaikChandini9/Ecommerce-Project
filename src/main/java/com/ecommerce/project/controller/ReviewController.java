package com.ecommerce.project.controller;

import com.ecommerce.project.request.ReviewRequest;
import com.ecommerce.project.response.ReviewResponse;
import com.ecommerce.project.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/review")
public class ReviewController {

    private final ReviewService service;

    @PostMapping("/create")
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest req) {
        return ResponseEntity.ok(service.create(req));
    }
}
