package com.ecom.carstore.repository;

import com.ecom.carstore.domain.Vendeur;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Vendeur entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VendeurRepository extends JpaRepository<Vendeur, Long> {}
