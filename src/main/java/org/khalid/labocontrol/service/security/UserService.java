package org.khalid.labocontrol.service.security;

import jakarta.persistence.EntityNotFoundException;
import org.khalid.labocontrol.entities.Cart;
import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.repository.security.UtilisateurRepository;
import org.khalid.labocontrol.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UtilisateurRepository utilisateurRepository;

    @Autowired
    public UserService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public Utilisateur getUserById(Long userId) {
        return utilisateurRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
    }

}