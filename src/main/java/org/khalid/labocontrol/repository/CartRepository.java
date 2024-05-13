package org.khalid.labocontrol.repository;

import org.khalid.labocontrol.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
  //  Cart getUserActiveCartByUsername(String username);
    Cart findByUsername(String username);
}