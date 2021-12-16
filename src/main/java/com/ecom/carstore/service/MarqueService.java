package com.ecom.carstore.service;

import com.ecom.carstore.domain.Marque;
import com.ecom.carstore.repository.MarqueRepository;
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
public class MarqueService {

    private final Logger log = LoggerFactory.getLogger(MarqueService.class);

    private static final String ENTITY_NAME = "marque";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MarqueRepository marqueRepository;

    public MarqueService(MarqueRepository marqueRepository) {
        this.marqueRepository = marqueRepository;
    }

    public ResponseEntity<Marque> createMarque(Marque marque) throws URISyntaxException {
        log.debug("REST request to save Marque : {}", marque);
        if (marque.getId() != null) {
            throw new BadRequestAlertException("A new marque cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Marque result = marqueRepository.save(marque);
        return ResponseEntity
            .created(new URI("/api/marques/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Marque> updateMarque(Long id, Marque marque) throws URISyntaxException {
        log.debug("REST request to update Marque : {}, {}", id, marque);
        if (marque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marque.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Marque result = marqueRepository.save(marque);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marque.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Marque> partialUpdateMarque(Long id, Marque marque) throws URISyntaxException {
        log.debug("REST request to partial update Marque partially : {}, {}", id, marque);
        if (marque.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, marque.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!marqueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Marque> result = marqueRepository
            .findById(marque.getId())
            .map(existingMarque -> {
                if (marque.getMarque() != null) {
                    existingMarque.setMarque(marque.getMarque());
                }

                return existingMarque;
            })
            .map(marqueRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, marque.getId().toString())
        );
    }

    public List<Marque> getAllMarques() {
        log.debug("REST request to get all Marques");
        return marqueRepository.findAll();
    }

    public ResponseEntity<Marque> getMarque(Long id) {
        log.debug("REST request to get Marque : {}", id);
        Optional<Marque> marque = marqueRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(marque);
    }

    public ResponseEntity<Void> deleteMarque(Long id) {
        log.debug("REST request to delete Marque : {}", id);
        marqueRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
