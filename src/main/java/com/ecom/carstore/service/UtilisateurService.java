package com.ecom.carstore.service;

import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Utilisateur;
import com.ecom.carstore.repository.UtilisateurRepository;
import com.ecom.carstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
public class UtilisateurService {

    private final Logger log = LoggerFactory.getLogger(UtilisateurService.class);

    private static final String ENTITY_NAME = "utilisateur";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UtilisateurRepository utilisateurRepository;

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    public boolean existsByidcompte(User user) {
        return utilisateurRepository.existsByidcompte(user);
    }

    public Utilisateur getByidcompte(User user) {
        return utilisateurRepository.getByidcompte(user);
    }

    public void save(Utilisateur utilisateur) {
        utilisateurRepository.save(utilisateur);
    }

    public ResponseEntity<Utilisateur> createUtilisateur(Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to save Utilisateur : {}", utilisateur);
        if (utilisateur.getId() != null) {
            throw new BadRequestAlertException("A new utilisateur cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Utilisateur result = utilisateurRepository.save(utilisateur);
        return ResponseEntity
            .created(new URI("/api/utilisateurs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Utilisateur> updateUtilisateur(final Long id, Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to update Utilisateur : {}, {}", id, utilisateur);
        if (utilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Utilisateur result = utilisateurRepository.save(utilisateur);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, utilisateur.getId().toString()))
            .body(result);
    }

    public ResponseEntity<Utilisateur> partialUpdateUtilisateur(Long id, Utilisateur utilisateur) throws URISyntaxException {
        log.debug("REST request to partial update Utilisateur partially : {}, {}", id, utilisateur);
        if (utilisateur.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, utilisateur.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!utilisateurRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Utilisateur> result = utilisateurRepository
            .findById(utilisateur.getId())
            .map(existingUtilisateur -> {
                return existingUtilisateur;
            })
            .map(utilisateurRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, utilisateur.getId().toString())
        );
    }

    public List<Utilisateur> getAllUtilisateurs(String filter) {
        if ("panier-is-null".equals(filter)) {
            log.debug("REST request to get all Utilisateurs where panier is null");
            return StreamSupport
                .stream(utilisateurRepository.findAll().spliterator(), false)
                .filter(utilisateur -> utilisateur.getPanier() == null)
                .collect(Collectors.toList());
        }

        if ("souhait-is-null".equals(filter)) {
            log.debug("REST request to get all Utilisateurs where souhait is null");
            return StreamSupport
                .stream(utilisateurRepository.findAll().spliterator(), false)
                .filter(utilisateur -> utilisateur.getSouhait() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all Utilisateurs");
        return utilisateurRepository.findAll();
    }

    public ResponseEntity<Utilisateur> getUtilisateur(Long id) {
        log.debug("REST request to get Utilisateur : {}", id);
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(utilisateur);
    }

    public ResponseEntity<Void> deleteUtilisateur(Long id) {
        log.debug("REST request to delete Utilisateur : {}", id);
        utilisateurRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
