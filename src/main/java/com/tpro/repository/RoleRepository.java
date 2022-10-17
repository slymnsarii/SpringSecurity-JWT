package com.tpro.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tpro.domain.Role;
import com.tpro.domain.enums.UserRole;

@Repository //JpaRepository'den extends ettigim icin yazmasam da olur
public interface RoleRepository extends JpaRepository<Role, Integer>{

	//Rollerde findById ile gitmek zor oldugu icin findByRollerle ilgili bir method'la gitmemiz daha iyi
	Optional<Role>findByName(UserRole name); //servis katmaninda orElseThrow ile handle etmek icin optional donduruyoruz
}
