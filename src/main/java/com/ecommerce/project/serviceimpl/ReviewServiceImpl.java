package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.Customer;
import com.ecommerce.project.entity.Product;
import com.ecommerce.project.entity.Review;
import com.ecommerce.project.repository.CustomerRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.repository.ReviewRepository;
import com.ecommerce.project.request.ReviewRequest;
import com.ecommerce.project.response.ReviewResponse;
import com.ecommerce.project.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;

    @Override
    public ReviewResponse create(ReviewRequest r) {
        Customer c = customerRepo.findById(r.getCustomerId()).orElseThrow(()
                -> new NoSuchElementException("Customer not found"));
        Product p = productRepo.findById(r.getProductId()).orElseThrow(()
                -> new NoSuchElementException("Product not found"));

        Review review = Review.builder()
                .customer(c).product(p).rating(r.getRating()).comment(r.getComment())
                .build();

        review = reviewRepo.save(review);
        return ReviewResponse.builder()
                .id(review.getId()).customerId(c.getId()).productId(p.getId())
                .rating(review.getRating()).comment(review.getComment())
                .build();
    }
}
