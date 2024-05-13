package org.khalid.labocontrol.service;

import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public Cart createCart(Cart cart) {
        return cartRepository.save(cart);
    }


    public Cart getUserCartByUsername(String username) {
        // Implement logic to fetch user's cart based on username
        // You might retrieve it from the database or any other storage mechanism

        // For demonstration purposes, let's assume you have a method to retrieve the cart by username
        return cartRepository.findByUsername(username);
    }



}