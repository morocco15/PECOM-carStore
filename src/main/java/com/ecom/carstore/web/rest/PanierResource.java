package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.Panier;
import com.ecom.carstore.repository.PanierRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ecom.carstore.domain.Panier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PanierResource {

    private final Logger log = LoggerFactory.getLogger(PanierResource.class);

    private static final String ENTITY_NAME = "panier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PanierRepository panierRepository;

    public PanierResource(PanierRepository panierRepository) {
        this.panierRepository = panierRepository;
    }

    /**
     * {@code POST  /paniers} : Create a new panier.
     *
     * @param panier the panier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new panier, or with status {@code 400 (Bad Request)} if the panier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paniers")
    public ResponseEntity<Panier> createPanier(@RequestBody Panier panier) throws URISyntaxException {
        log.debug("REST request to save Panier : {}", panier);
        if (panier.getId() != null) {
            throw new BadRequestAlertException("A new panier cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Panier result = panierRepository.save(panier);
        return ResponseEntity
            .created(new URI("/api/paniers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /paniers/:id} : Updates an existing panier.
     *
     * @param id the id of the panier to save.
     * @param panier the panier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panier,
     * or with status {@code 400 (Bad Request)} if the panier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the panier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paniers/{id}")
    public ResponseEntity<Panier> updatePanier(@PathVariable(value = "id", required = false) final Long id, @RequestBody Panier panier)
        throws URISyntaxException {
        log.debug("REST request to update Panier : {}, {}", id, panier);
        if (panier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!panierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Panier result = panierRepository.save(panier);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, panier.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /paniers/:id} : Partial updates given fields of an existing panier, field will ignore if it is null
     *
     * @param id the id of the panier to save.
     * @param panier the panier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panier,
     * or with status {@code 400 (Bad Request)} if the panier is not valid,
     * or with status {@code 404 (Not Found)} if the panier is not found,
     * or with status {@code 500 (Internal Server Error)} if the panier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paniers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Panier> partialUpdatePanier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Panier panier
    ) throws URISyntaxException {
        log.debug("REST request to partial update Panier partially : {}, {}", id, panier);
        if (panier.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, panier.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!panierRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Panier> result = panierRepository
            .findById(panier.getId())
            .map(existingPanier -> {
                return existingPanier;
            })
            .map(panierRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, panier.getId().toString())
        );
    }

    /**
     * {@code GET  /paniers} : get all the paniers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paniers in body.
     */
    @GetMapping("/paniers")
    public List<Panier> getAllPaniers() {
        log.debug("REST request to get all Paniers");
        return panierRepository.findAll();
    }

    /**
     * {@code GET  /paniers/:id} : get the "id" panier.
     *
     * @param id the id of the panier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the panier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paniers/{id}")
    public ResponseEntity<Panier> getPanier(@PathVariable Long id) {
        log.debug("REST request to get Panier : {}", id);
        Optional<Panier> panier = panierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(panier);
    }

    /**
     * {@code DELETE  /paniers/:id} : delete the "id" panier.
     *
     * @param id the id of the panier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paniers/{id}")
    public ResponseEntity<Void> deletePanier(@PathVariable Long id) {
        log.debug("REST request to delete Panier : {}", id);
        panierRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
