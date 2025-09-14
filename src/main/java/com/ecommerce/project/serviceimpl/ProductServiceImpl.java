package com.ecommerce.project.serviceimpl;

import com.ecommerce.project.entity.Product;
import com.ecommerce.project.repository.ProductRepository;
import com.ecommerce.project.request.ProductRequest;
import com.ecommerce.project.response.ProductResponse;
import com.ecommerce.project.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.List;

@Service @RequiredArgsConstructor @Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

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

    private ProductResponse mapResponse(Product p) {
        return ProductResponse.builder()
                .id(p.getId()).title(p.getTitle()).description(p.getDescription())
                .price(p.getPrice()).quantity(p.getQuantity()).build();
    }

}
