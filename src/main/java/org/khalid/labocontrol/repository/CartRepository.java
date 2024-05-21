package org.khalid.labocontrol.repository;

import jakarta.persistence.LockModeType;
import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.entities.security.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findActiveCartByUserId(Long userId);
   /* @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cart c WHERE c.user = :user AND c.status = :status")
    Cart findByUserAndStatusWithLock(@Param("user") Utilisateur user, @Param("status") String status);
*/
}