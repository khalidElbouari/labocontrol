package org.khalid.labocontrol.repository;

import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.entities.CartItem;
import org.khalid.labocontrol.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndProduct(Cart cart, Product product);
    List<CartItem> findByCart(Cart cart);
    boolean existsByProductId(Long productId);


}