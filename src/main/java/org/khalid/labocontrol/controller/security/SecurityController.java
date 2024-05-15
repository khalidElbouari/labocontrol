package org.khalid.labocontrol.controller.security;

import org.khalid.labocontrol.entities.security.Utilisateur;
import org.khalid.labocontrol.service.security.PictureService;
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
    private PictureService pictureService;


   /* @GetMapping("/profile-picture")
    public ResponseEntity<Map<String, String>> getProfilePicture() {
        Map<String, String> response = new HashMap<>();
        response.put("profilePictureUrl", profilePicture);
        return ResponseEntity.ok(response);
    }*/
   @PostMapping("/login")
   public ResponseEntity<Map<String, Object>> login(String username, String password) {
       // Authenticate the user
       Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(username, password)
       );

       // Get the authenticated user details
       Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
       // Build the JWT token without including the image data
       String jwt = buildJwtToken(authentication, utilisateur);
       // Fetch the image data separately
       byte[] imageData = utilisateur.getImageData();
       // Create a response containing the JWT token and image data
       Map<String, Object> responseData = new HashMap<>();
       responseData.put("access-token", jwt);
       responseData.put("imageData", imageData); // Convert image data to Base64 string

       return ResponseEntity.ok(responseData);
   }

    private String buildJwtToken(Authentication authentication, Utilisateur utilisateur) {
        String fullName = utilisateur.getNom() + ' ' + utilisateur.getPrenom();
        Long userId = utilisateur.getId();
        Instant instant = Instant.now();
        String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .issuedAt(instant)
                .expiresAt(instant.plus(20, ChronoUnit.MINUTES))
                .subject(utilisateur.getUsername())
                .claim("scope", scope)
                .claim("fullName", fullName)
                .claim("userId", userId)
                .build();

        JwtEncoderParameters jwtEncoderParameters =
                JwtEncoderParameters.from(
                        JwsHeader.with(MacAlgorithm.HS512).build(),
                        jwtClaimsSet
                );

        return jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
    }

        /*@PostMapping("/login")
        public Map<String, String> login(String username, String password) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
            // Get the authenticated user details
            Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
            String fullName=utilisateur.getNom()+' '+utilisateur.getPrenom();
            // Fetch the profile picture URL for the authenticated user
            String profilePictureUrl = "http://localhost:8055/" + utilisateur.getPhotoName();
            byte[] imageData=utilisateur.getImageData();
            Instant instant = Instant.now();
            String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(" "));
            JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                    .issuedAt(instant)
                    .expiresAt(instant.plus(20, ChronoUnit.MINUTES))
                    .subject(username)
                    .claim("scope", scope)
                    .claim("profilePictureUrl", profilePictureUrl)
                    .claim("fullName", fullName)
                    .claim("imageData", imageData)
                    .build();
            JwtEncoderParameters jwtEncoderParameters =
                    JwtEncoderParameters.from(
                            JwsHeader.with(MacAlgorithm.HS512).build(),
                            jwtClaimsSet
                    );
            String jwt = jwtEncoder.encode(jwtEncoderParameters).getTokenValue();
            return Map.of("access-token", jwt);
        }
*/
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
                profilePicturePath = pictureService.saveProfilePicture(profilePicture);
            }
            byte[] imageData = profilePicture.getBytes();

            // Create a Utilisateur object with the other user data
            Utilisateur utilisateur = new Utilisateur(nom, prenom, null, username, password, true,imageData);
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
