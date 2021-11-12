package com.ecom.carstore.repository;

import com.ecom.carstore.domain.Souhait;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Souhait entity.
 */
@Repository
public interface SouhaitRepository extends JpaRepository<Souhait, Long> {
    @Query(
        value = "select distinct souhait from Souhait souhait left join fetch souhait.voitures",
        countQuery = "select count(distinct souhait) from Souhait souhait"
    )
    Page<Souhait> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct souhait from Souhait souhait left join fetch souhait.voitures")
    List<Souhait> findAllWithEagerRelationships();

    @Query("select souhait from Souhait souhait left join fetch souhait.voitures where souhait.id =:id")
    Optional<Souhait> findOneWithEagerRelationships(@Param("id") Long id);
}
