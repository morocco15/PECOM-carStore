package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.Voiture;
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

    private final VoitureRepository voitureRepository;

    private VoitureService voitureService;

    public VoitureResource(VoitureRepository voitureRepository,VoitureService voitureService) {
        this.voitureRepository = voitureRepository;
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
        log.debug("REST request to save Voiture : {}", voiture);
        if (voiture.getId() != null) {
            throw new BadRequestAlertException("A new voiture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Voiture result = voitureRepository.save(voiture);
        return ResponseEntity
            .created(new URI("/api/voitures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
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
        log.debug("REST request to update Voiture : {}, {}", id, voiture);
        if (voiture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voiture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voitureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Voiture result = voitureRepository.save(voiture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, voiture.getId().toString()))
            .body(result);
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
        log.debug("REST request to partial update Voiture partially : {}, {}", id, voiture);
        if (voiture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, voiture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!voitureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Voiture> result = voitureRepository
            .findById(voiture.getId())
            .map(existingVoiture -> {
                if (voiture.getModel() != null) {
                    existingVoiture.setModel(voiture.getModel());
                }
                if (voiture.getPrix() != null) {
                    existingVoiture.setPrix(voiture.getPrix());
                }
                if (voiture.getImage1() != null) {
                    existingVoiture.setImage1(voiture.getImage1());
                }
                if (voiture.getImage2() != null) {
                    existingVoiture.setImage2(voiture.getImage2());
                }
                if (voiture.getImage3() != null) {
                    existingVoiture.setImage3(voiture.getImage3());
                }
                if (voiture.getStatut() != null) {
                    existingVoiture.setStatut(voiture.getStatut());
                }
                if (voiture.getVersion() != null) {
                    existingVoiture.setVersion(voiture.getVersion());
                }
                if (voiture.getMiseEnVente() != null) {
                    existingVoiture.setMiseEnVente(voiture.getMiseEnVente());
                }
                if (voiture.getEtat() != null) {
                    existingVoiture.setEtat(voiture.getEtat());
                }
                if (voiture.getPorte() != null) {
                    existingVoiture.setPorte(voiture.getPorte());
                }
                if (voiture.getBoiteVitesse() != null) {
                    existingVoiture.setBoiteVitesse(voiture.getBoiteVitesse());
                }
                if (voiture.getCo2() != null) {
                    existingVoiture.setCo2(voiture.getCo2());
                }
                if (voiture.getChevaux() != null) {
                    existingVoiture.setChevaux(voiture.getChevaux());
                }
                if (voiture.getCarburant() != null) {
                    existingVoiture.setCarburant(voiture.getCarburant());
                }
                if (voiture.getAnnees() != null) {
                    existingVoiture.setAnnees(voiture.getAnnees());
                }
                if (voiture.getVille() != null) {
                    existingVoiture.setVille(voiture.getVille());
                }
                if (voiture.getCodePostal() != null) {
                    existingVoiture.setCodePostal(voiture.getCodePostal());
                }
                if (voiture.getDescription() != null) {
                    existingVoiture.setDescription(voiture.getDescription());
                }

                return existingVoiture;
            })
            .map(voitureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, voiture.getId().toString())
        );
    }

    /**
     * {@code GET  /voitures} : get all the voitures.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of voitures in body.
     */
    @GetMapping("/voitures")
    public List<Voiture> getAllVoitures(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Voitures");
        return voitureRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /voitures/:id} : get the "id" voiture.
     *
     * @param id the id of the voiture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the voiture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/voitures/{id}")
    public ResponseEntity<Voiture> getVoiture(@PathVariable Long id) {
        log.debug("REST request to get Voiture : {}", id);
        Optional<Voiture> voiture = voitureRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(voiture);
    }

    /**
     * {@code DELETE  /voitures/:id} : delete the "id" voiture.
     *
     * @param id the id of the voiture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/voitures/{id}")
    public ResponseEntity<Void> deleteVoiture(@PathVariable Long id) {
        log.debug("REST request to delete Voiture : {}", id);
        voitureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
    @GetMapping("/voiture/{debut}/{fin}")
    @ResponseBody
    public List<Voiture> getModelRecent(@PathVariable("debut") int debut, @PathVariable("fin") int fin) {
        return voitureService.getModelRecent(debut, fin);
    }
}
