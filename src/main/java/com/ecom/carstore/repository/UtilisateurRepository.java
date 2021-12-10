package com.ecom.carstore.repository;

import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Utilisateur;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Utilisateur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByidcompte(User id);

    Utilisateur getByidcompte(User id);
}
