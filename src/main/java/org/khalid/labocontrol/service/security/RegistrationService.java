package org.khalid.labocontrol.service.security;

import org.khalid.labocontrol.entities.security.Role;
import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.repository.security.RoleRepository;
import org.khalid.labocontrol.repository.security.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class RegistrationService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    public RegistrationService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
    }

  
    public void registerNewUser(String username, String password) {
        Utilisateur newUser = new Utilisateur();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setEnabled(true); // Set to true by default
        // Retrieve the "USER" role from the database
        Role userRole = roleRepository.findByName("USER");
        // Assign retrieved role "USER" to the user
        newUser.addRole(userRole);
        utilisateurRepository.save(newUser);
    }
}
