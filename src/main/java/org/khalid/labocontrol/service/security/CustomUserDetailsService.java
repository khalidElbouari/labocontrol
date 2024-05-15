package org.khalid.labocontrol.service.security;


import org.khalid.labocontrol.repository.security.RoleRepository;
import org.khalid.labocontrol.repository.security.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
// mab9itch khdam bhad l class (:
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomUserDetailsService(UtilisateurRepository utilisateurRepository,
                                    RoleRepository roleRepository
                                    ) {
        this.utilisateurRepository = utilisateurRepository;
        this.roleRepository = roleRepository;

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }

}
