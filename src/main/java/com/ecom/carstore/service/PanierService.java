package com.ecom.carstore.service;

import com.ecom.carstore.domain.*;
import com.ecom.carstore.domain.enumeration.Statut;
import com.ecom.carstore.repository.*;
import com.ecom.carstore.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    private UserRepository userRepository;
    private UtilisateurRepository utilisateurRepository;
    private VoitureService voitureService;
    public PanierService(PanierRepository panierRepository,
                         CommandeRepository commandeRepository,
                         UserRepository userRepository,
                         UtilisateurRepository utilisateurRepository,
                         VoitureService voitureService) {
        this.panierRepository = panierRepository;
        this.commandeRepository = commandeRepository;
        this.userRepository = userRepository;
        this.utilisateurRepository = utilisateurRepository;
        this.voitureService = voitureService;
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

    public ResponseEntity<Commande> payer(Panier panier, Commande commande) throws URISyntaxException {
        panierRepository.delete(panier);
        Commande result = commandeRepository.save(commande);
        return ResponseEntity
            .created(new URI("/api/commandes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }



    public List<Voiture> getVoitures(Panier panier) {
        //Page<Voiture> v = panierRepository.getVoituresDuPanier(panier);
        //return v.getContent();

        Set<Voiture> voitures = panier.getVoitures();
        List<Voiture> res = new ArrayList<Voiture>(voitures); ;
        return res;
    }


    public boolean supprimerVoitureDuPanier(String username,Long idVoiture){
        User user = userRepository.findOneByUsername(username);
        if(user!=null){
            Utilisateur utilisateur = utilisateurRepository.getByidcompte(user);
            Panier panier = utilisateur.getPanier();
            Voiture voiture = voitureService.findOneById(idVoiture);
            if(panier!=null && voiture!=null){
                if(panier.getVoitures().contains(voiture)){
                    panier.removeVoitures(voiture);
                    voitureService.libererVoiture(voiture);
                    return true;
                }else {
                    return false;
                }
            }else{
                return false;
            }
        }
        return false;
    }
}
/*
        User user = userRepository.findOneByUsername(username);
        if(user!=null){
            Utilisateur utilisateur = utilisateurRepository.getByidcompte(user);
            Panier panier = utilisateur.getPanier();
            if(panier==null){
                return voitures;
            }else {
                return panierService.getVoitures(panier);
                //return voitures;
            }
        }
        return voitures;
 */
