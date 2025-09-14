package com.ecommerce.project.controller;

import com.ecommerce.project.request.WishlistMoveRequest;
import com.ecommerce.project.request.WishlistRequest;
import com.ecommerce.project.response.WishlistMoveResponse;
import com.ecommerce.project.response.WishlistResponse;
import com.ecommerce.project.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/wishlist")
@RequiredArgsConstructor
public class WishlistController {

    private final WishlistService wishlistService;

    @PostMapping("/add-to-wishlist")
    public ResponseEntity<WishlistResponse> addToWishlist(@RequestBody WishlistRequest request) {
        return ResponseEntity.ok(wishlistService.addToWishlist(request));
    }

    @GetMapping("/get-wishlist-details/{customerId}")
    public ResponseEntity<List<WishlistResponse>> getWishlist(@PathVariable Long customerId) {
        return ResponseEntity.ok(wishlistService.getWishlist(customerId));
    }

    @DeleteMapping("/remove-from-wishlist")
    public ResponseEntity<String> removeFromWishlist(@RequestParam Long customerId, @RequestParam Long productId) {
        wishlistService.removeFromWishlist(customerId, productId);
        return ResponseEntity.ok("Product removed from wishlist");
    }

    @PostMapping("/move-to-order/wishlist")
    public ResponseEntity<WishlistMoveResponse> moveToOrder(@RequestBody WishlistMoveRequest request) {
        return ResponseEntity.ok(wishlistService.moveFromWishlistToOrder(request));
    }
}
