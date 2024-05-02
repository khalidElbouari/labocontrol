package org.khalid.labocontrol.service.security;


import org.khalid.labocontrol.entities.security.Role;
import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.repository.security.RoleRepository;
import org.khalid.labocontrol.repository.security.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class RegistrationService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired

    private  ProfilePictureService profilePictureService;
    @Autowired
    public RegistrationService(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;
    }
    public void registerNewUser(Utilisateur user) throws Exception {
        if (user.getNom() == null || user.getNom().isEmpty()) {
            throw new Exception("Nom is required");
        }
        if (user.getPrenom() == null || user.getPrenom().isEmpty()) {
            throw new Exception("Prenom is required");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            throw new Exception("Username is required");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new Exception("Password is required");
        }
        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Set default enabled status
        user.setEnabled(true);

        // Retrieve the "USER" role from the database
        Role userRole = roleRepository.findByName("USER");

        // Assign retrieved role "USER" to the user
        user.addRole(userRole);

        // Save the user
        utilisateurRepository.save(user);
    }




}
