package com.ecommerce.project.service;


import com.ecommerce.project.request.ReviewRequest;
import com.ecommerce.project.response.ReviewResponse;

public interface ReviewService {

    ReviewResponse create(ReviewRequest request);
}
