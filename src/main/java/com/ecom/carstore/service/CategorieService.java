package com.ecom.carstore.service;

import com.ecom.carstore.domain.Categorie;
import com.ecom.carstore.repository.CategorieRepository;
import com.ecom.carstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@Service
@Transactional
public class CategorieService {

    private final Logger log = LoggerFactory.getLogger(CategorieService.class);

    private final CategorieRepository categorieRepository;

    private static final String ENTITY_NAME = "categorie";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CategorieService(CategorieRepository categorieRepository) {
        this.categorieRepository = categorieRepository;
    }

    public List<String> listCategorie() {
        return categorieRepository.listCategorie();
    }

    public ResponseEntity<Categorie> createCategorie(Categorie categorie) throws URISyntaxException {
        log.debug("REST request to save Categorie : {}", categorie);
        if (categorie.getId() != null) {
            throw new BadRequestAlertException("A new categorie cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Categorie result = categorieRepository.save(categorie);
        return ResponseEntity
            .created(new URI("/api/categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Categorie> updateCategorie(Long id, Categorie categorie) throws URISyntaxException {
        log.debug("REST request to update Categorie : {}, {}", id, categorie);
        if (categorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Categorie result = categorieRepository.save(categorie);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categorie.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Categorie> partialUpdateCategorie(Long id, Categorie categorie) throws URISyntaxException {
        log.debug("REST request to partial update Categorie partially : {}, {}", id, categorie);
        if (categorie.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categorie.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categorieRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Categorie> result = categorieRepository
            .findById(categorie.getId())
            .map(existingCategorie -> {
                if (categorie.getCategorie() != null) {
                    existingCategorie.setCategorie(categorie.getCategorie());
                }

                return existingCategorie;
            })
            .map(categorieRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categorie.getId().toString())
        );
    }

    public List<Categorie> getAllCategories() {
        log.debug("REST request to get all Categories");
        return categorieRepository.findAll();
    }

    public ResponseEntity<Categorie> getCategorie(Long id) {
        log.debug("REST request to get Categorie : {}", id);
        Optional<Categorie> categorie = categorieRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categorie);
    }

    public ResponseEntity<Void> deleteCategorie(Long id) {
        log.debug("REST request to delete Categorie : {}", id);
        categorieRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
