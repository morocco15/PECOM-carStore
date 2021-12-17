package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.*;
import com.ecom.carstore.repository.SouhaitRepository;
import com.ecom.carstore.repository.UserRepository;
import com.ecom.carstore.repository.UtilisateurRepository;
import com.ecom.carstore.service.SouhaitService;
import com.ecom.carstore.service.VoitureService;
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
 * REST controller for managing {@link com.ecom.carstore.domain.Souhait}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SouhaitResource {

    private final Logger log = LoggerFactory.getLogger(SouhaitResource.class);

    private static final String ENTITY_NAME = "souhait";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SouhaitService souhaitService;
    private final VoitureService voitureService;

    public SouhaitResource(SouhaitService souhaitService, VoitureService voitureService) {
        this.souhaitService = souhaitService;
        this.voitureService = voitureService;
    }

    /**
     * {@code POST  /souhaits} : Create a new souhait.
     *
     * @param souhait the souhait to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new souhait, or with status {@code 400 (Bad Request)} if the souhait has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/souhaits")
    public ResponseEntity<Souhait> createSouhait(@RequestBody Souhait souhait) throws URISyntaxException {
        return souhaitService.createSouhait(souhait);
    }

    /**
     * {@code PUT  /souhaits/:id} : Updates an existing souhait.
     *
     * @param id the id of the souhait to save.
     * @param souhait the souhait to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated souhait,
     * or with status {@code 400 (Bad Request)} if the souhait is not valid,
     * or with status {@code 500 (Internal Server Error)} if the souhait couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/souhaits/{id}")
    public ResponseEntity<Souhait> updateSouhait(@PathVariable(value = "id", required = false) final Long id, @RequestBody Souhait souhait)
        throws URISyntaxException {
        return souhaitService.updateSouhait(id, souhait);
    }

    /**
     * {@code PATCH  /souhaits/:id} : Partial updates given fields of an existing souhait, field will ignore if it is null
     *
     * @param id the id of the souhait to save.
     * @param souhait the souhait to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated souhait,
     * or with status {@code 400 (Bad Request)} if the souhait is not valid,
     * or with status {@code 404 (Not Found)} if the souhait is not found,
     * or with status {@code 500 (Internal Server Error)} if the souhait couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/souhaits/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Souhait> partialUpdateSouhait(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Souhait souhait
    ) throws URISyntaxException {
        return souhaitService.partialUpdateSouhait(id, souhait);
    }

    /**
     * {@code GET  /souhaits} : get all the souhaits.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of souhaits in body.
     */
    @GetMapping("/souhaits")
    public List<Souhait> getAllSouhaits(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        return souhaitService.getAllSouhaits(eagerload);
    }

    /**
     * {@code GET  /souhaits/:id} : get the "id" souhait.
     *
     * @param id the id of the souhait to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the souhait, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/souhaits/{id}")
    public ResponseEntity<Souhait> getSouhait(@PathVariable Long id) {
        return souhaitService.getSouhait(id);
    }

    /**
     * {@code DELETE  /souhaits/:id} : delete the "id" souhait.
     *
     * @param id the id of the souhait to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/souhaits/{id}")
    public ResponseEntity<Void> deleteSouhait(@PathVariable Long id) {
        return souhaitService.deleteSouhait(id);
    }

    @GetMapping("/souhait/{username}/{id}")
    @ResponseBody
    public boolean AjouterVoitureDansSouhait(@PathVariable("username") String username, @PathVariable("id") Long id) {
        return souhaitService.ajouterVoitureDansSouhait(username, id);
    }

    @GetMapping("/souhait/{username}")
    @ResponseBody
    public List<Voiture> getPanier(@PathVariable("username") String username) {
        return souhaitService.getSouhait(username);
    }

    @GetMapping("/souhait_sup/{username}/{id}")
    @ResponseBody
    public boolean SupprimerVoitureDuSouhait(@PathVariable("username") String username, @PathVariable("id") Long id) {
        return souhaitService.supprimerVoitureDuSouhait(username, id);
    }
}
