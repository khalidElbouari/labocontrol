package org.khalid.labocontrol.repository;

import org.khalid.labocontrol.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    // You can add custom methods here if needed
}