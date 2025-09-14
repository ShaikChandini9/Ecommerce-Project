package com.ecommerce.project.service;

import com.ecommerce.project.request.WishlistMoveRequest;
import com.ecommerce.project.request.WishlistRequest;
import com.ecommerce.project.response.WishlistMoveResponse;
import com.ecommerce.project.response.WishlistResponse;

import java.util.List;

public interface WishlistService {

    WishlistResponse addToWishlist(WishlistRequest request);
    List<WishlistResponse> getWishlist(Long customerId);
    void removeFromWishlist(Long customerId, Long productId);
    WishlistMoveResponse moveFromWishlistToOrder(WishlistMoveRequest request);
}
