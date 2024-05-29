package org.khalid.labocontrol.service;

import jakarta.persistence.EntityNotFoundException;
import org.khalid.labocontrol.entities.CartItem;
import org.khalid.labocontrol.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    @Autowired
    public CartItemService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public CartItem addCartItem(CartItem cartItem) {
        return cartItemRepository.save(cartItem);
    }

    public void removeCartItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    public CartItem updateCartItemQuantity(Long id, int quantity) {
        Optional<CartItem> optionalCartItem = cartItemRepository.findById(id);
        if (optionalCartItem.isPresent()) {
            CartItem cartItem = optionalCartItem.get();
            cartItem.setQuantity(quantity);
            return cartItemRepository.save(cartItem);
        } else {
            throw new EntityNotFoundException("Panier avec le id " + id + " n'est pas trouver");
        }
    }

    public boolean isProductInCarts(Long productId) {
        return cartItemRepository.existsByProductId(productId);
    }
}