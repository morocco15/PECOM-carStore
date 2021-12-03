package com.ecom.carstore.repository;

import com.ecom.carstore.domain.Voiture;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Voiture entity.
 */
@Repository
public interface VoitureRepository extends JpaRepository<Voiture, Long> {
    @Query(
        value = "select distinct voiture from Voiture voiture left join fetch voiture.categories",
        countQuery = "select count(distinct voiture) from Voiture voiture"
    )
    Page<Voiture> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct voiture from Voiture voiture left join fetch voiture.categories")
    List<Voiture> findAllWithEagerRelationships();

    @Query("select voiture from Voiture voiture left join fetch voiture.categories where voiture.id =:id")
    Optional<Voiture> findOneWithEagerRelationships(@Param("id") Long id);

    @Query("select voiture from Voiture voiture ORDER BY voiture.miseEnVente DESC")
    List<Voiture> derniereVoitureAjouter();

    @Query("select voiture.version from Voiture voiture where voiture.id=:id")
    Integer getVoitureVersion(@Param("id") Long id);
}
