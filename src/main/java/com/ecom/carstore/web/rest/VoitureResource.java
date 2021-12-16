package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.Categorie;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Etat;
import com.ecom.carstore.domain.enumeration.Statut;
import com.ecom.carstore.repository.VoitureRepository;
import com.ecom.carstore.service.VoitureService;
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
 * REST controller for managing {@link com.ecom.carstore.domain.Voiture}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class VoitureResource {

    private final Logger log = LoggerFactory.getLogger(VoitureResource.class);

    private static final String ENTITY_NAME = "voiture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private VoitureService voitureService;

    public VoitureResource(VoitureService voitureService) {
        this.voitureService = voitureService;
    }

    /**
     * {@code POST  /voitures} : Create a new voiture.
     *
     * @param voiture the voiture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new voiture, or with status {@code 400 (Bad Request)} if the voiture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/voitures")
    public ResponseEntity<Voiture> createVoiture(@Valid @RequestBody Voiture voiture) throws URISyntaxException {
        return voitureService.createVoiture(voiture);
    }

    /**
     * {@code PUT  /voitures/:id} : Updates an existing voiture.
     *
     * @param id the id of the voiture to save.
     * @param voiture the voiture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voiture,
     * or with status {@code 400 (Bad Request)} if the voiture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the voiture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/voitures/{id}")
    public ResponseEntity<Voiture> updateVoiture(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Voiture voiture
    ) throws URISyntaxException {
        return voitureService.updateVoiture(id, voiture);
    }

    /**
     * {@code PATCH  /voitures/:id} : Partial updates given fields of an existing voiture, field will ignore if it is null
     *
     * @param id the id of the voiture to save.
     * @param voiture the voiture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated voiture,
     * or with status {@code 400 (Bad Request)} if the voiture is not valid,
     * or with status {@code 404 (Not Found)} if the voiture is not found,
     * or with status {@code 500 (Internal Server Error)} if the voiture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/voitures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Voiture> partialUpdateVoiture(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Voiture voiture
    ) throws URISyntaxException {
        return voitureService.partialUpdateVoiture(id, voiture);
    }

    /**
     * {@code GET  /voitures} : get all the voitures.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voitures in body.
     */
    @GetMapping("/voitures")
    public List<Voiture> getAllVoitures(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        return voitureService.getAllVoitures(eagerload);
    }

    /**
     * {@code GET  /voitures/:id} : get the "id" voiture.
     *
     * @param id the id of the voiture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voiture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voitures/{id}")
    public ResponseEntity<Voiture> getVoiture(@PathVariable Long id) {
        return voitureService.getVoiture(id);
    }

    /**
     * {@code DELETE  /voitures/:id} : delete the "id" voiture.
     *
     * @param id the id of the voiture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voitures/{id}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        return voitureService.deleteVoiture(id);
    }

    @GetMapping("/voiture/{debut}/{fin}")
    @ResponseBody
    public List<Voiture> getModelRecent(@PathVariable("debut") int debut, @PathVariable("fin") int fin) {
        return voitureService.getModelRecent(debut, fin);
    }

    @GetMapping("maxPrix/{max}")
    public List<Voiture> maxPrix(@PathVariable("max") Long max) {
        return voitureService.maxPrix(max);
    }

    @GetMapping("minPrix/{min}")
    public List<Voiture> minPrix(@PathVariable("min") Long min) {
        return voitureService.minPrix(min);
    }

    @GetMapping("limitePrix/{min}/{max}")
    public List<Voiture> minPrix(@PathVariable("min") Long min, @PathVariable("max") Long max) {
        return voitureService.limitePrix(min, max);
    }

    @GetMapping("limiteEtat/{etat}")
    public List<Voiture> limiteEtat(@PathVariable("etat") Etat etat) {
        return voitureService.limiteEtat(etat);
    }

    @GetMapping("limiteCategorie/{categorie}")
    public List<Voiture> limiteEtat(@PathVariable("categorie") String categorie) {
        return voitureService.limiteCategorie(categorie);
    }
}
