package org.khalid.labocontrol.controller.security;

import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.service.security.CustomUserDetailsService;
import org.khalid.labocontrol.service.security.ProfilePictureService;
import org.khalid.labocontrol.service.security.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class SecurityController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private ProfilePictureService profilePictureService;


   /* @GetMapping("/profile-picture")
    public ResponseEntity<Map<String, String>> getProfilePicture() {
        Map<String, String> response = new HashMap<>();
        response.put("profilePictureUrl", profilePicture);
        return ResponseEntity.ok(response);
    }*/

        @PostMapping("/login")
        public Map<String, String> login(String username, String password) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            // Get the authenticated user details
            Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
            // Fetch the profile picture URL for the authenticated user
            String profilePictureUrl = "http://localhost:8055/" + utilisateur.getPhotoName();

            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(10, ChronoUnit.MINUTES))
                    .subject(username)
                    .claim("scope", scope)
                    .claim("profilePictureUrl", profilePictureUrl)
                    .build();
            JwtEncoderParameters jwtEncoderParameters =
                    JwtEncoderParameters.from(
                            JwsHeader.with(MacAlgorithm.HS512).build(),
                            jwtClaimsSet
                    );
            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerNewUser(@RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                                               @RequestParam("nom") String nom,
                                                               @RequestParam("prenom") String prenom,
                                                               @RequestParam("username") String username,
                                                               @RequestParam("password") String password) {
        try {
            // Check if profilePicture is provided before attempting to save it
            String profilePicturePath = null;
            if (profilePicture != null) {
                profilePicturePath = profilePictureService.saveProfilePicture(profilePicture);
            }
            // Create a Utilisateur object with the other user data
            Utilisateur utilisateur = new Utilisateur(nom, prenom, null, username, password, true);
            utilisateur.setPhotoName(profilePicturePath); // Set the photo path in the utilisateur object
            utilisateur.setProfilePicture(profilePicture);
            // Register the new user
            registrationService.registerNewUser(utilisateur);

            Map<String, String> response = new HashMap<>();
            response.put("message", "User registered successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to register user: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

}
