package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.Vendeur;
import com.ecom.carstore.repository.VendeurRepository;
import com.ecom.carstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.ecom.carstore.domain.Vendeur}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VendeurResource {

    private final Logger log = LoggerFactory.getLogger(VendeurResource.class);

    private static final String ENTITY_NAME = "vendeur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VendeurRepository vendeurRepository;

    public VendeurResource(VendeurRepository vendeurRepository) {
        this.vendeurRepository = vendeurRepository;
    }

    /**
     * {@code POST  /vendeurs} : Create a new vendeur.
     *
     * @param vendeur the vendeur to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new vendeur, or with status {@code 400 (Bad Request)} if the vendeur has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/vendeurs")
    public ResponseEntity<Vendeur> createVendeur(@Valid @RequestBody Vendeur vendeur) throws URISyntaxException {
        log.debug("REST request to save Vendeur : {}", vendeur);
        if (vendeur.getId() != null) {
            throw new BadRequestAlertException("A new vendeur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Vendeur result = vendeurRepository.save(vendeur);
        return ResponseEntity
            .created(new URI("/api/vendeurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /vendeurs/:id} : Updates an existing vendeur.
     *
     * @param id the id of the vendeur to save.
     * @param vendeur the vendeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendeur,
     * or with status {@code 400 (Bad Request)} if the vendeur is not valid,
     * or with status {@code 500 (Internal Server Error)} if the vendeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/vendeurs/{id}")
    public ResponseEntity<Vendeur> updateVendeur(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Vendeur vendeur
    ) throws URISyntaxException {
        log.debug("REST request to update Vendeur : {}, {}", id, vendeur);
        if (vendeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Vendeur result = vendeurRepository.save(vendeur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendeur.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /vendeurs/:id} : Partial updates given fields of an existing vendeur, field will ignore if it is null
     *
     * @param id the id of the vendeur to save.
     * @param vendeur the vendeur to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated vendeur,
     * or with status {@code 400 (Bad Request)} if the vendeur is not valid,
     * or with status {@code 404 (Not Found)} if the vendeur is not found,
     * or with status {@code 500 (Internal Server Error)} if the vendeur couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/vendeurs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Vendeur> partialUpdateVendeur(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Vendeur vendeur
    ) throws URISyntaxException {
        log.debug("REST request to partial update Vendeur partially : {}, {}", id, vendeur);
        if (vendeur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, vendeur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!vendeurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Vendeur> result = vendeurRepository
            .findById(vendeur.getId())
            .map(existingVendeur -> {
                if (vendeur.getLogin() != null) {
                    existingVendeur.setLogin(vendeur.getLogin());
                }
                if (vendeur.getCoordonnee() != null) {
                    existingVendeur.setCoordonnee(vendeur.getCoordonnee());
                }

                return existingVendeur;
            })
            .map(vendeurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, vendeur.getId().toString())
        );
    }

    /**
     * {@code GET  /vendeurs} : get all the vendeurs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of vendeurs in body.
     */
    @GetMapping("/vendeurs")
    public List<Vendeur> getAllVendeurs() {
        log.debug("REST request to get all Vendeurs");
        return vendeurRepository.findAll();
    }

    /**
     * {@code GET  /vendeurs/:id} : get the "id" vendeur.
     *
     * @param id the id of the vendeur to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the vendeur, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/vendeurs/{id}")
    public ResponseEntity<Vendeur> getVendeur(@PathVariable Long id) {
        log.debug("REST request to get Vendeur : {}", id);
        Optional<Vendeur> vendeur = vendeurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(vendeur);
    }

    /**
     * {@code DELETE  /vendeurs/:id} : delete the "id" vendeur.
     *
     * @param id the id of the vendeur to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/vendeurs/{id}")
    public ResponseEntity<Void> deleteVendeur(@PathVariable Long id) {
        log.debug("REST request to delete Vendeur : {}", id);
        vendeurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
