package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.Panier;
import com.ecom.carstore.domain.User;
import com.ecom.carstore.domain.Utilisateur;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Livraison;
import com.ecom.carstore.repository.PanierRepository;
import com.ecom.carstore.repository.UserRepository;
import com.ecom.carstore.repository.UtilisateurRepository;
import com.ecom.carstore.service.PanierService;
import com.ecom.carstore.service.UserService;
import com.ecom.carstore.service.VoitureService;
import java.net.URISyntaxException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing {@link com.ecom.carstore.domain.Panier}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PanierResource {

    private final Logger log = LoggerFactory.getLogger(PanierResource.class);

    private static final String ENTITY_NAME = "panier";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private VoitureService voitureService;
    private PanierService panierService;
    private final UserService userService;

    public PanierResource(
        PanierRepository panierRepository,
        VoitureService voitureService,
        UserRepository userRepository,
        UtilisateurRepository utilisateurRepository,
        PanierService panierService,
        UserService userService
    ) {
        this.voitureService = voitureService;
        this.panierService = panierService;
        this.userService = userService;
    }

    /**
     * {@code POST  /paniers} : Create a new panier.
     *
     * @param panier the panier to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new panier, or with status {@code 400 (Bad Request)} if the panier has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/paniers")
    public ResponseEntity<Panier> createPanier(@RequestBody Panier panier) throws URISyntaxException {
        return panierService.createPanier(panier);
    }

    /**
     * {@code PUT  /paniers/:id} : Updates an existing panier.
     *
     * @param id the id of the panier to save.
     * @param panier the panier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panier,
     * or with status {@code 400 (Bad Request)} if the panier is not valid,
     * or with status {@code 500 (Internal Server Error)} if the panier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/paniers/{id}")
    public ResponseEntity<Panier> updatePanier(@PathVariable(value = "id", required = false) final Long id, @RequestBody Panier panier)
        throws URISyntaxException {
        return panierService.updatePanier(id, panier);
    }

    /**
     * {@code PATCH  /paniers/:id} : Partial updates given fields of an existing panier, field will ignore if it is null
     *
     * @param id the id of the panier to save.
     * @param panier the panier to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated panier,
     * or with status {@code 400 (Bad Request)} if the panier is not valid,
     * or with status {@code 404 (Not Found)} if the panier is not found,
     * or with status {@code 500 (Internal Server Error)} if the panier couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/paniers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Panier> partialUpdatePanier(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Panier panier
    ) throws URISyntaxException {
        return panierService.partialUpdatePanier(id, panier);
    }

    /**
     * {@code GET  /paniers} : get all the paniers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paniers in body.
     */
    @GetMapping("/paniers")
    public List<Panier> getAllPaniers() {
        return panierService.getAllPaniers();
    }

    /**
     * {@code GET  /paniers/:id} : get the "id" panier.
     *
     * @param id the id of the panier to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the panier, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/paniers/{id}")
    public ResponseEntity<Panier> getPanier(@PathVariable Long id) {
        return panierService.getPanier(id);
    }

    /**
     * {@code DELETE  /paniers/:id} : delete the "id" panier.
     *
     * @param id the id of the panier to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/paniers/{id}")
    public ResponseEntity<Void> deletePanier(@PathVariable Long id) {
        return panierService.deletePanier(id);
    }

    @GetMapping("/panier/{username}/{id}/{version}")
    @ResponseBody
    public boolean AjouterVoitureDansPanier(
        @PathVariable("username") String username,
        @PathVariable("id") Long id,
        @PathVariable("version") int version
    ) {
        User user = userService.findOneByUsername(username);
        if (user != null && voitureService.reserverVoiture(id, version)) {
            Utilisateur utilisateur = userService.getByidcompte(user);
            Panier panier = utilisateur.getPanier();
            Voiture voiture = voitureService.findOneById(id);
            if (!panier.getVoitures().contains(voiture)) {
                panier.addVoitures(voiture);
            }
            panierService.save(panier);
            return true;
        }
        return false;
    }

    @GetMapping("/getpanier/{username}")
    @ResponseBody
    public List<Voiture> getPanier(@PathVariable("username") String username) {
        List<Voiture> voitures = null;

        User user = userService.findOneByUsername(username);
        if (user != null) {
            Utilisateur utilisateur = userService.getByidcompte(user);
            Panier panier = utilisateur.getPanier();
            if (panier == null) {
                return voitures;
            } else {
                return panierService.getVoitures(panier);
            }
        }
        return voitures;
    }

    @GetMapping("/getpanier/{username}/{idVoiture}")
    @ResponseBody
    public boolean SupprimerVoitureDuPanier(@PathVariable("username") String username, @PathVariable("idVoiture") Long idVoiture) {
        return panierService.supprimerVoitureDuPanier(username, idVoiture);
    }

    @GetMapping("/payerpanier/{idpanier}/{livraison}")
    public int PayerPanier(@PathVariable("idpanier") Long idpanier, @PathVariable("livraison") Livraison livraison)
        throws URISyntaxException {
        return panierService.payer(idpanier, livraison);
    }
}
