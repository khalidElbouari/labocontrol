package org.khalid.labocontrol.controller;

import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.entities.CartItem;
import org.khalid.labocontrol.entities.Product;
import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.service.CartService;
import org.khalid.labocontrol.service.security.CustomUserDetailsService;
import org.khalid.labocontrol.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService,UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getCartItems(@RequestParam Long userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }
    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam Long userId, @RequestBody Product product) {
        // Retrieve the user from the database
        Utilisateur user = userService.getUserById(userId);
        // Call the addOrUpdateCart method in CartService
        Cart cart = cartService.addOrUpdateCart(user, product, 1); // adding one product at a time
        // Optionally, return the updated cart or a success message
        return ResponseEntity.ok(cart);
    }
    @PostMapping("/process")
    public ResponseEntity<?> processCartItems(
            @RequestParam Long userId,
            @RequestBody List<CartItem> cartItems) {
        try {
            // Retrieve the user from the database (assuming you have a UserService)
            Utilisateur user = userService.getUserById(userId);
            // Process each cart item and add it to the user's cart
            for (CartItem cartItem : cartItems) {
                cartService.addOrUpdateCart(user, cartItem.getProduct(), cartItem.getQuantity());
            }
            return ResponseEntity.ok("Cart items processed successfully.");
        } catch (Exception e) {
            // Handle any exceptions and return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing cart items: " + e.getMessage());
        }
    }


}