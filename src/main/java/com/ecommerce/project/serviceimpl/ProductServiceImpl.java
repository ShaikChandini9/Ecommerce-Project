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
    public ProductResponse create(ProductRequest r) {
        Product p = Product.builder()
                .title(r.getTitle()).description(r.getDescription())
                .price(r.getPrice()).quantity(r.getQuantity())
                .build();
        return mapResponse(repo.save(p));
    }

    @Override
    public ProductResponse addStock(Long productId, int addQty) {
        Product p = repo.findById(productId).orElseThrow(() -> new NoSuchElementException("Product not found"));
        p.setQuantity(p.getQuantity() + addQty);
        return mapResponse(repo.save(p));
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

    private ProductResponse mapResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId()).title(p.getTitle()).description(p.getDescription())
                .price(p.getPrice()).quantity(p.getQuantity()).build();
    }

}
