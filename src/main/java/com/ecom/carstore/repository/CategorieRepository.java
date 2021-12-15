package com.ecom.carstore.repository;

import com.ecom.carstore.domain.Categorie;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Categorie entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    @Query("select c.categorie from Categorie c")
    List<String> listCategorie();
}
