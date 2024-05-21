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
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Cart addOrUpdateCart(Utilisateur user, Product product, int quantity) {
        // Retrieve the active cart for the user, or create a new one if none exists
        Cart activeCart = getActiveCart(user);
        // Check if the product is already in the active cart
        CartItem existingItem = cartItemRepository.findByCartAndProduct(activeCart, product);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity); // Increase quantity
        } else {
            // If the product is not in the active cart, create a new cart item
            CartItem newItem = new CartItem();
            newItem.setCart(activeCart);
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            // Save the new cart item to the database
            cartItemRepository.save(newItem);
            // Add the new cart item to the active cart
            activeCart.getCartItems().add(newItem);
        }

        // Save the active cart to the database again to persist any changes
        cartRepository.save(activeCart);
        return activeCart;
    }

    @Transactional
    public Cart getActiveCart(Utilisateur utilisateur) {
        // Retrieve the active cart directly from the database with a lock to prevent concurrent modifications
        Cart activeCart = cartRepository.findActiveCartByUserId(utilisateur.getId());

        if (activeCart == null) {
            // If no active cart exists, create a new one and set it as active
            activeCart = new Cart();
            activeCart.setUser(utilisateur);
            activeCart.setStatus("active");
            activeCart = cartRepository.save(activeCart);

            // Add the new cart to the user's carts
            utilisateur.getCarts().add(activeCart);
        }

        return activeCart;
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