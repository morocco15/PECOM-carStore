package com.ecom.carstore.repository;

import com.ecom.carstore.domain.Categorie;
import com.ecom.carstore.domain.Panier;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Etat;
import com.ecom.carstore.domain.enumeration.Statut;
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

    @Query("select voiture from Voiture voiture where voiture.statut='LIBRE' ORDER BY voiture.miseEnVente DESC")
    Page<Voiture> derniereVoitureAjouter(Pageable pageable);

    @Query("select voiture.version from Voiture voiture where voiture.id=:id")
    Integer getVoitureVersion(@Param("id") Long id);

    @Query("select v from Voiture as v where v.prix <= :max")
    List<Voiture> maxPrix(@Param("max") Long max);

    @Query("select v from Voiture as v where v.prix >= :min")
    List<Voiture> minPrix(@Param("min") Long min);

    @Query("select v from Voiture as v where v.prix >= :min and v.prix <= :max")
    List<Voiture> limitePrix(@Param("min") Long min, @Param("max") Long max);

    @Query("select v from Voiture as v where v.etat = :etat")
    List<Voiture> limiteEtat(@Param("etat") Etat etat);

    //select v.* from Voiture as v join rel_voiture__categories r on v.id=r.voiture_id join Categorie as c on r.categories_id=c.id where c.categorie = 'Account mindshare'
    @Query(
        value = "select v.* from Voiture as v join rel_voiture__categories r on v.id=r.voiture_id join Categorie as c on r.categories_id=c.id where c.categorie = :categorie",
        nativeQuery = true
    )
    List<Voiture> limiteCategorie(@Param("categorie") String categorie);
}
