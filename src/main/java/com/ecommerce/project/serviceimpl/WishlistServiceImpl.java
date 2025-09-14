package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.*;
import com.ecommerce.project.enumpackage.OrderStatus;
import com.ecommerce.project.repository.*;
import com.ecommerce.project.request.*;
import com.ecommerce.project.response.*;
import com.ecommerce.project.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepo;
    private final CustomerRepository customerRepo;
    private final ProductRepository productRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository orderItemRepo;

    @Override
    public WishlistResponse addToWishlist(WishlistRequest request) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        boolean exists = wishlistRepo.findByCustomerAndProduct(customer, product).isPresent();
        if (exists) {
            return WishlistResponse.builder()
                    .customerId(customer.getId())
                    .productId(product.getId())
                    .message("Product is already in the wishlist")
                    .build();
        }

        Wishlist wishlist = Wishlist.builder()
                .customer(customer)
                .product(product)
                .build();

        Wishlist saved = wishlistRepo.save(wishlist);

        return WishlistResponse.builder()
                .wishlistId(saved.getId())
                .customerId(customer.getId())
                .productId(product.getId())
                .productTitle(product.getTitle())
                .productPrice(product.getPrice())
                .message("Product added to wishlist")
                .build();
    }

    @Override
    public List<WishlistResponse> getWishlist(Long customerId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        return wishlistRepo.findByCustomer(customer).stream()
                .map(wishlist -> WishlistResponse.builder()
                        .wishlistId(wishlist.getId())
                        .customerId(customerId)
                        .productId(wishlist.getProduct().getId())
                        .productTitle(wishlist.getProduct().getTitle())
                        .productPrice(wishlist.getProduct().getPrice())
                        .message("Product fetched from wishlist")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void removeFromWishlist(Long customerId, Long productId) {
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        Wishlist wishlist = wishlistRepo.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new NoSuchElementException("Product not found in wishlist"));

        wishlistRepo.delete(wishlist);
    }

    @Override
    public WishlistMoveResponse moveFromWishlistToOrder(WishlistMoveRequest request) {
        Customer customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Product product = productRepo.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException("Product not found"));

        Wishlist wishlistItem = wishlistRepo.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new IllegalStateException("Product is not in wishlist"));

        if (product.getQuantity() < request.getQuantity()) {
            throw new IllegalStateException("Not enough stock available for this product");
        }

        Order order = Order.builder()
                .customer(customer)
                .status(OrderStatus.PENDING_PAYMENT)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepo.save(order);

        OrderItem orderItem = OrderItem.builder()
                .order(savedOrder)
                .product(product)
                .quantity(request.getQuantity())
                .unitPrice(product.getPrice())
                .lineTotal(product.getPrice() * request.getQuantity())
                .build();

        orderItemRepo.save(orderItem);

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepo.save(product);

        wishlistRepo.delete(wishlistItem);

        return WishlistMoveResponse.builder()
                .orderId(savedOrder.getId())
                .customerId(customer.getId())
                .productId(product.getId())
                .productTitle(product.getTitle())
                .quantity(request.getQuantity())
                .totalPrice(orderItem.getLineTotal())
                .message("Product moved from wishlist to order successfully")
                .orderCreatedAt(savedOrder.getCreatedAt())
                .build();
    }
}
