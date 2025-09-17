package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.OrderItem;
import com.ecommerce.project.entity.Product;
import com.ecommerce.project.entity.Wishlist;
import com.ecommerce.project.enumpackage.OrderStatus;
import com.ecommerce.project.repository.OrderItemRepository;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.repository.WishlistRepository;
import com.ecommerce.project.request.ProductRequest;
import com.ecommerce.project.response.ProductResponse;
import com.ecommerce.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final OrderItemRepository orderItemRepo;
    private final WishlistRepository wishlistRepo;

    @Override
    public ProductResponse create(ProductRequest request) {
        Product product = Product.builder()
                .title(request.getTitle()).description(request.getDescription())
                .price(request.getPrice()).quantity(request.getQuantity())
                .build();
        return mapResponse(repo.save(product));
    }

    @Override
    public ProductResponse addStock(Long productId, int addQty) {
        Product product = repo.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));
        product.setQuantity(product.getQuantity() + addQty);
        return mapResponse(repo.save(product));
    }

    @Override
    public ProductResponse get(Long id) {
        return mapResponse(repo.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found")));
    }

    @Override
    public List<ProductResponse> list() {
        return repo.findAll().stream().map(this::mapResponse).toList();
    }

    @Override
    public void deleteProduct(Long productId) {
        Product product = repo.findById(productId)
                .orElseThrow(() -> new NoSuchElementException("Product not found with ID: " + productId));

        List<OrderItem> activeOrderItems = orderItemRepo.findByProduct(product);
        boolean isInActiveOrder = activeOrderItems.stream()
                .anyMatch(orderItem -> orderItem.getOrder().getStatus() != OrderStatus.CANCELLED);

        if (isInActiveOrder) {
            throw new IllegalStateException("Cannot delete product. It is part of an active order.");
        }

        List<Wishlist> wishlists = wishlistRepo.findByProduct(product);
        if (!wishlists.isEmpty()) {
            throw new IllegalStateException("Cannot delete product. It exists in one or more wishlists.");
        }

        repo.delete(product);
    }

    private ProductResponse mapResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId()).title(product.getTitle()).description(product.getDescription())
                .price(product.getPrice()).quantity(product.getQuantity()).build();
    }

}
