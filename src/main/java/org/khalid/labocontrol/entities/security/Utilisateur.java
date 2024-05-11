package org.khalid.labocontrol.entities.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "utilisateurs")
public class Utilisateur implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    private String prenom;
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    private Date datenaiss;
    private String photoName;
    @Transient
    @JsonIgnore
    private MultipartFile profilePicture;
    private String username;
    private String password;
    private boolean enabled=true;//hna par defaut khasha tkon true
    @Lob
    @Column(length = 1048576) // Specify the desired length for the column (in bytes)
    private byte[] imageData;
    

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "utilisateur_roles",
            joinColumns = @JoinColumn(name = "utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @JsonIgnoreProperties("utilisateurs") 
    private Set<Role> roles = new HashSet<>();


    // Constructor with arguments
    public Utilisateur(String nom, String prenom, Date datenaiss, String username, String password, boolean enabled, byte[] imageData) {
        this.nom = nom;
        this.prenom = prenom;
        this.datenaiss = datenaiss;
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.imageData = imageData;

    }

    // No-args constructor (needed for JPA)
    public Utilisateur() {}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

 // Implement a method to add the "USER" role to the user
    public void addRole(Role role) {
        if (roles == null) {
            roles = new HashSet<>();
        }

        roles.add(role);
    }
    
    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; 
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; 
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public Date getDatenaiss() {
		return datenaiss;
	}

	public void setDatenaiss(Date datenaiss) {
		this.datenaiss = datenaiss;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photo) {
		this.photoName = photo;
	}

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }

    @JsonIgnoreProperties("utilisateurs")
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}


    
}
