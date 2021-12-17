package com.ecom.carstore.service;

import com.ecom.carstore.domain.Categorie;
import com.ecom.carstore.domain.Panier;
import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Etat;
import com.ecom.carstore.domain.enumeration.Statut;
import com.ecom.carstore.repository.PanierRepository;
import com.ecom.carstore.repository.UserRepository;
import com.ecom.carstore.repository.VoitureRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@Service
@Transactional
public class VoitureService {

    private final Logger log = LoggerFactory.getLogger(VoitureService.class);

    private static final String ENTITY_NAME = "voiture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    private VoitureRepository voitureRepository;

    public VoitureService(VoitureRepository voitureRepository) {
        this.voitureRepository = voitureRepository;
    }

    public ResponseEntity<Voiture> createVoiture(Voiture voiture) throws URISyntaxException {
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

    public ResponseEntity<Voiture> updateVoiture(Long id, Voiture voiture) throws URISyntaxException {
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

    public ResponseEntity<Voiture> partialUpdateVoiture(Long id, Voiture voiture) throws URISyntaxException {
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

    public List<Voiture> getAllVoitures(boolean eagerload) {
        log.debug("REST request to get all Voitures");
        return voitureRepository.findAllWithEagerRelationships();
    }

    public ResponseEntity<Voiture> getVoiture(Long id) {
        log.debug("REST request to get Voiture : {}", id);
        Optional<Voiture> voiture = voitureRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(voiture);
    }

    public ResponseEntity<Void> deleteVoiture(Long id) {
        log.debug("REST request to delete Voiture : {}", id);
        voitureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    public List<Voiture> getModelRecent(int debut, int fin) {
        Page<Voiture> v = voitureRepository.derniereVoitureAjouter(PageRequest.of(debut, fin));
        return v.getContent();
    }

    public Voiture findOneById(Long id) {
        return voitureRepository.getById(id);
    }

    private boolean statusModifiable(Long id, int version) {
        return version == voitureRepository.getVoitureVersion(id);
    }

    public boolean reserverVoiture(Long id, int version) {
        Voiture voiture = voitureRepository.getById(id);
        if (voiture.getStatut() == Statut.LIBRE && statusModifiable(id, version)) {
            voiture.setStatut(Statut.RESERVER);
            voiture.setVersion(voiture.getVersion() + 1);
            voitureRepository.save(voiture);
            return true;
        } else {
            return false;
        }
    }

    public void libererVoiture(Voiture voiture) {
        if (voiture.getStatut() == Statut.RESERVER) {
            voiture.setStatut(Statut.LIBRE);
            voiture.setVersion(voiture.getVersion() + 1);
            voitureRepository.save(voiture);
        }
    }

    public List<Voiture> maxPrix(Long max) {
        return voitureRepository.maxPrix(max);
    }

    public List<Voiture> minPrix(Long min) {
        return voitureRepository.minPrix(min);
    }

    public List<Voiture> limitePrix(Long min, Long max) {
        return voitureRepository.limitePrix(min, max);
    }

    public List<Voiture> limiteEtat(Etat etat) {
        return voitureRepository.limiteEtat(etat);
    }

    public List<Voiture> limiteCategorie(String categorie) {
        return voitureRepository.limiteCategorie(categorie);
    }

    public void save(Voiture v) {
        voitureRepository.save(v);
    }
}
