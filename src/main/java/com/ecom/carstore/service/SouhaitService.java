package com.ecom.carstore.service;

import com.ecom.carstore.domain.Souhait;
import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Utilisateur;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.repository.SouhaitRepository;
import com.ecom.carstore.repository.UserRepository;
import com.ecom.carstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@Service
@Transactional
public class SouhaitService {

    private final Logger log = LoggerFactory.getLogger(SouhaitService.class);

    private static final String ENTITY_NAME = "souhait";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SouhaitRepository souhaitRepository;
    private final VoitureService voitureService;
    private final UserRepository userRepository;
    private final UtilisateurService utilisateurService;

    public SouhaitService(
        SouhaitRepository souhaitRepository,
        VoitureService voitureService,
        UserRepository userRepository,
        UtilisateurService utilisateurService
    ) {
        this.souhaitRepository = souhaitRepository;
        this.voitureService = voitureService;
        this.userRepository = userRepository;
        this.utilisateurService = utilisateurService;
    }

    public boolean ajouterVoitureDansSouhait(String username, Long idVoiture) {
        User user = userRepository.findOneByUsername(username);
        if (user != null) {
            Utilisateur utilisateur = utilisateurService.getByidcompte(user);
            Souhait souhait = utilisateur.getSouhait();
            Voiture voiture = voitureService.findOneById(idVoiture);
            if (souhait != null && voiture != null) {
                if (souhait.getVoitures().contains(voiture)) {
                    return false;
                } else {
                    souhait.addVoitures(voiture);
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public boolean supprimerVoitureDuSouhait(String username, Long idVoiture) {
        User user = userRepository.findOneByUsername(username);
        if (user != null) {
            Utilisateur utilisateur = utilisateurService.getByidcompte(user);
            Souhait souhait = utilisateur.getSouhait();
            //Voiture voiture = souhaitContains(souhait,idVoiture);
            Voiture voiture = voitureService.findOneById(idVoiture); //il faut modifier
            if (souhait.getVoitures().contains(voiture)) {
                souhait.removeVoitures(voiture);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public Voiture souhaitContains(Souhait souhait, Long idVoiture) {
        for (Voiture voiture : souhait.getVoitures()) {
            if (voiture.getId() == idVoiture) return voiture;
        }
        return null;
    }

    public List<Voiture> getSouhait(String username) {
        List<Voiture> voitures = null;
        User user = userRepository.findOneByUsername(username);
        if (user != null) {
            Utilisateur utilisateur = utilisateurService.getByidcompte(user);
            Souhait souhait = utilisateur.getSouhait();
            if (souhait == null) {
                return voitures;
            } else {
                voitures = new ArrayList<Voiture>(souhait.getVoitures());
                return voitures;
            }
        }
        return voitures;
    }

    public void delete(Souhait souhait) {
        souhaitRepository.delete(souhait);
    }

    public void save(Souhait souhait) {
        souhaitRepository.save(souhait);
    }

    public ResponseEntity<Souhait> createSouhait(Souhait souhait) throws URISyntaxException {
        log.debug("REST request to save Souhait : {}", souhait);
        if (souhait.getId() != null) {
            throw new BadRequestAlertException("A new souhait cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Souhait result = souhaitRepository.save(souhait);
        return ResponseEntity
            .created(new URI("/api/souhaits/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Souhait> updateSouhait(Long id, Souhait souhait) throws URISyntaxException {
        log.debug("REST request to update Souhait : {}, {}", id, souhait);
        if (souhait.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, souhait.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!souhaitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Souhait result = souhaitRepository.save(souhait);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, souhait.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Souhait> partialUpdateSouhait(Long id, Souhait souhait) throws URISyntaxException {
        log.debug("REST request to partial update Souhait partially : {}, {}", id, souhait);
        if (souhait.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, souhait.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!souhaitRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Souhait> result = souhaitRepository
            .findById(souhait.getId())
            .map(existingSouhait -> {
                return existingSouhait;
            })
            .map(souhaitRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, souhait.getId().toString())
        );
    }

    public List<Souhait> getAllSouhaits(boolean eagerload) {
        log.debug("REST request to get all Souhaits");
        return souhaitRepository.findAllWithEagerRelationships();
    }

    public ResponseEntity<Souhait> getSouhait(Long id) {
        log.debug("REST request to get Souhait : {}", id);
        Optional<Souhait> souhait = souhaitRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(souhait);
    }

    public ResponseEntity<Void> deleteSouhait(Long id) {
        log.debug("REST request to delete Souhait : {}", id);
        souhaitRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
