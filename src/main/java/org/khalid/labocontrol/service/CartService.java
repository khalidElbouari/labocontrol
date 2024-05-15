package org.khalid.labocontrol.service;

import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.entities.CartItem;
import org.khalid.labocontrol.entities.Product;
import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.repository.CartItemRepository;
import org.khalid.labocontrol.repository.CartRepository;
import org.khalid.labocontrol.service.security.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;

    @Autowired
    public CartService(CartRepository cartRepository,CartItemRepository cartItemRepository,UserService userService) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userService = userService;
    }

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }


    public Cart addToCart(Utilisateur user, Product product) {
        Cart cart = getActiveCart(user); // Get the active cart for the user
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setStatus("active"); // Set status as active for new cart
        }

        // Check if the product is already in the cart
        CartItem existingItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + 1); // Increase quantity if product is already in cart
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(product);
            newItem.setQuantity(1);
            cart.getCartItems().add(newItem); // Add new cart item to cart
        }

        // Save the updated cart to the database
        cartRepository.save(cart);
        return cart;
    }

    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }

    public Cart getActiveCart(Utilisateur utilisateur) {
        List<Cart> carts = utilisateur.getCarts();
        // If the user has no carts, create a new active cart
        if (carts.isEmpty()) {
            Cart newCart = new Cart();
            newCart.setUser(utilisateur);
            newCart.setStatus("active");
            return cartRepository.save(newCart);
        }

        // Find the first cart with the active status
        return carts.stream()
                .filter(cart -> cart.getStatus().equals("active"))
                .findFirst()
                .orElse(null);
    }

    public List<CartItem> getCartItems(Long userId) {
        // Retrieve the user's active cart
        Cart cart = cartRepository.findActiveCartByUserId(userId);

        // If user has an active cart, fetch its cart items
        if (cart != null) {
            return cartItemRepository.findByCart(cart);
        } else {
            // If no active cart found, return an empty list
            return Collections.emptyList();
        }
    }
}