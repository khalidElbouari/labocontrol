package org.khalid.labocontrol.controller;

import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // Add endpoints for CRUD operations

    @PostMapping("/create")
    public ResponseEntity<Cart> createCart(@RequestBody Cart cart) {
        Cart newCart = cartService.createCart(cart);
        return new ResponseEntity<>(newCart, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<Cart> getUserCart(Authentication authentication) {
        // Check if user is authenticated
        if (authentication != null && authentication.isAuthenticated()) {
            // Get user's username from authentication
            String username = authentication.getName();

            // Implement logic to fetch user's cart based on username
            Cart cart = cartService.getUserCartByUsername(username);

            if (cart != null) {
                return new ResponseEntity<>(cart, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            // User is not authenticated, return unauthorized status
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}