package com.ecom.carstore.service;

import static java.time.ZonedDateTime.now;

import com.ecom.carstore.domain.Commande;
import com.ecom.carstore.domain.Panier;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Livraison;
import com.ecom.carstore.domain.enumeration.Statut;
import com.ecom.carstore.repository.CommandeRepository;
import com.ecom.carstore.repository.PanierRepository;
import com.ecom.carstore.repository.VoitureRepository;
import com.ecom.carstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
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
public class PanierService {

    private final Logger log = LoggerFactory.getLogger(PanierService.class);

    private static final String ENTITY_NAME = "commande";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private PanierRepository panierRepository;
    private CommandeRepository commandeRepository;
    private final VoitureRepository voitureRepository;

    public PanierService(PanierRepository panierRepository, CommandeRepository commandeRepository, VoitureRepository voitureRepository) {
        this.panierRepository = panierRepository;
        this.commandeRepository = commandeRepository;
        this.voitureRepository = voitureRepository;
    }

    public ResponseEntity<Panier> createPanier(Panier panier) throws URISyntaxException {
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

    public ResponseEntity<Panier> updatePanier(Long id, Panier panier) throws URISyntaxException {
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

    public ResponseEntity<Panier> partialUpdatePanier(Long id, Panier panier) throws URISyntaxException {
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

    public List<Panier> getAllPaniers() {
        log.debug("REST request to get all Paniers");
        return panierRepository.findAll();
    }

    public ResponseEntity<Panier> getPanier(Long id) {
        log.debug("REST request to get Panier : {}", id);
        Optional<Panier> panier = panierRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(panier);
    }

    public ResponseEntity<Void> deletePanier(Long id) {
        log.debug("REST request to delete Panier : {}", id);
        panierRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    public int payer(Long idpanier, String livraison) {
        if (!panierRepository.existsById(idpanier)) {
            return 300;
        }
        Panier panier = panierRepository.getById(idpanier);
        Set<Voiture> voitures = panier.getVoitures();
        if (voitures.isEmpty()) {
            return 300;
        }
        Commande commande = new Commande();
        for (Voiture v : voitures) {
            v.setStatut(Statut.VENDU);
            commande.addVoitures(v);
            voitureRepository.save(v);
        }
        commande.setAcheteur(panier.getUtilisateur());
        commande.setDateCommande(now());
        commande.setModeLivraison(Livraison.valueOf(livraison));
        commandeRepository.save(commande);
        voitures.clear();
        panier.setVoitures(voitures);
        panierRepository.save(panier);
        return 200;
    }
}
