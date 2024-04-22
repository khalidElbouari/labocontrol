package org.khalid.labocontrol.entities.security;


import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;


@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



	@Override
	public String getAuthority() {
		return name;
	}

    
}
