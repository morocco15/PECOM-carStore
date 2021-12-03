package com.ecom.carstore.repository;

import com.ecom.carstore.domain.Panier;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Panier entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PanierRepository extends JpaRepository<Panier, Long> {



}
