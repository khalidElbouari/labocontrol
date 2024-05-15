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


    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestParam Long userId, @RequestBody Product product) {
        // Retrieve the user from the database (assuming you have a UserService)
        Utilisateur user = userService.getUserById(userId);

        // Call the addToCart method in CartService
        Cart cart = cartService.addToCart(user, product);

        // Optionally, return the updated cart or a success message
        return ResponseEntity.ok(cart); // Return the updated cart object
    }

    // Define the endpoint to fetch cart items for a user
    @GetMapping("/items")
    public ResponseEntity<List<CartItem>> getCartItems(@RequestParam Long userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }
}