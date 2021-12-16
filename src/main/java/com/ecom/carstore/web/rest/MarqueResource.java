package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.Marque;
import com.ecom.carstore.service.MarqueService;
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
 * REST controller for managing {@link com.ecom.carstore.domain.Marque}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MarqueResource {

    private final Logger log = LoggerFactory.getLogger(MarqueResource.class);

    private static final String ENTITY_NAME = "marque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarqueService marqueService;

    public MarqueResource(MarqueService marqueService) {
        this.marqueService = marqueService;
    }

    /**
     * {@code POST  /marques} : Create a new marque.
     *
     * @param marque the marque to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new marque, or with status {@code 400 (Bad Request)} if the marque has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/marques")
    public ResponseEntity<Marque> createMarque(@RequestBody Marque marque) throws URISyntaxException {
        return marqueService.createMarque(marque);
    }

    /**
     * {@code PUT  /marques/:id} : Updates an existing marque.
     *
     * @param id the id of the marque to save.
     * @param marque the marque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marque,
     * or with status {@code 400 (Bad Request)} if the marque is not valid,
     * or with status {@code 500 (Internal Server Error)} if the marque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/marques/{id}")
    public ResponseEntity<Marque> updateMarque(@PathVariable(value = "id", required = false) final Long id, @RequestBody Marque marque)
        throws URISyntaxException {
        return marqueService.updateMarque(id, marque);
    }

    /**
     * {@code PATCH  /marques/:id} : Partial updates given fields of an existing marque, field will ignore if it is null
     *
     * @param id the id of the marque to save.
     * @param marque the marque to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated marque,
     * or with status {@code 400 (Bad Request)} if the marque is not valid,
     * or with status {@code 404 (Not Found)} if the marque is not found,
     * or with status {@code 500 (Internal Server Error)} if the marque couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/marques/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Marque> partialUpdateMarque(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Marque marque
    ) throws URISyntaxException {
        return marqueService.partialUpdateMarque(id, marque);
    }

    /**
     * {@code GET  /marques} : get all the marques.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of marques in body.
     */
    @GetMapping("/marques")
    public List<Marque> getAllMarques() {
        return marqueService.getAllMarques();
    }

    /**
     * {@code GET  /marques/:id} : get the "id" marque.
     *
     * @param id the id of the marque to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the marque, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/marques/{id}")
    public ResponseEntity<Marque> getMarque(@PathVariable Long id) {
        return marqueService.getMarque(id);
    }

    /**
     * {@code DELETE  /marques/:id} : delete the "id" marque.
     *
     * @param id the id of the marque to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/marques/{id}")
    public ResponseEntity<Void> deleteMarque(@PathVariable Long id) {
        return marqueService.deleteMarque(id);
    }
}
