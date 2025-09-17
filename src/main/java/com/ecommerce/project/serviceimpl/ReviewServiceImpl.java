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
    public ReviewResponse create(ReviewRequest request) {
        Customer customer = customerRepo.findById(request.getCustomerId()).orElseThrow(()
                -> new NoSuchElementException("Customer not found"));
        Product product = productRepo.findById(request.getProductId()).orElseThrow(()
                -> new NoSuchElementException("Product not found"));

        Review review = Review.builder()
                .customer(customer).product(product).rating(request.getRating()).comment(request.getComment())
                .build();

        review = reviewRepo.save(review);
        return ReviewResponse.builder()
                .id(review.getId()).customerId(customer.getId()).productId(product.getId())
                .rating(review.getRating()).comment(review.getComment())
                .build();
    }
}
