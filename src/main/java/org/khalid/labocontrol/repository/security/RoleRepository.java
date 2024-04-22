package org.khalid.labocontrol.repository.security;

//RoleRepository.java

import org.khalid.labocontrol.entities.security.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Role findByName(String name);
}
