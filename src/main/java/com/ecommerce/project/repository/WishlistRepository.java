package com.ecommerce.project.repository;

import com.ecommerce.project.entity.Wishlist;
import com.ecommerce.project.entity.Customer;
import com.ecommerce.project.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByCustomer(Customer customer);
    Optional<Wishlist> findByCustomerAndProduct(Customer customer, Product product);
}
