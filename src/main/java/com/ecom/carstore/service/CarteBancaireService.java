package com.ecom.carstore.service;

import com.ecom.carstore.domain.CarteBancaire;
import com.ecom.carstore.repository.CarteBancaireRepository;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

@Service
@Transactional
public class CarteBancaireService {

    private final Logger log = LoggerFactory.getLogger(CarteBancaireService.class);

    private static final String ENTITY_NAME = "carteBancaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarteBancaireRepository carteBancaireRepository;

    public CarteBancaireService(CarteBancaireRepository carteBancaireRepository) {
        this.carteBancaireRepository = carteBancaireRepository;
    }

    public ResponseEntity<CarteBancaire> createCarteBancaire(CarteBancaire carteBancaire) throws URISyntaxException {
        log.debug("REST request to save CarteBancaire : {}", carteBancaire);
        if (carteBancaire.getId() != null) {
            throw new BadRequestAlertException("A new carteBancaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CarteBancaire result = carteBancaireRepository.save(carteBancaire);
        return ResponseEntity
            .created(new URI("/api/carte-bancaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    public ResponseEntity<CarteBancaire> updateCarteBancaire(Long id, CarteBancaire carteBancaire) throws URISyntaxException {
        log.debug("REST request to update CarteBancaire : {}, {}", id, carteBancaire);
        if (carteBancaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteBancaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carteBancaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CarteBancaire result = carteBancaireRepository.save(carteBancaire);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carteBancaire.getId().toString()))
            .body(result);
    }

    public ResponseEntity<CarteBancaire> partialUpdateCarteBancaire(Long id, CarteBancaire carteBancaire) throws URISyntaxException {
        log.debug("REST request to partial update CarteBancaire partially : {}, {}", id, carteBancaire);
        if (carteBancaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteBancaire.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carteBancaireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarteBancaire> result = carteBancaireRepository
            .findById(carteBancaire.getId())
            .map(existingCarteBancaire -> {
                if (carteBancaire.getCode() != null) {
                    existingCarteBancaire.setCode(carteBancaire.getCode());
                }
                if (carteBancaire.getExpiration() != null) {
                    existingCarteBancaire.setExpiration(carteBancaire.getExpiration());
                }
                if (carteBancaire.getPrenom() != null) {
                    existingCarteBancaire.setPrenom(carteBancaire.getPrenom());
                }
                if (carteBancaire.getNom() != null) {
                    existingCarteBancaire.setNom(carteBancaire.getNom());
                }

                return existingCarteBancaire;
            })
            .map(carteBancaireRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, carteBancaire.getId().toString())
        );
    }

    public List<CarteBancaire> getAllCarteBancaires() {
        log.debug("REST request to get all CarteBancaires");
        return carteBancaireRepository.findAll();
    }

    public ResponseEntity<CarteBancaire> getCarteBancaire(Long id) {
        log.debug("REST request to get CarteBancaire : {}", id);
        Optional<CarteBancaire> carteBancaire = carteBancaireRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(carteBancaire);
    }

    public ResponseEntity<Void> deleteCarteBancaire(Long id) {
        log.debug("REST request to delete CarteBancaire : {}", id);
        carteBancaireRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    public void delete(CarteBancaire carteBancaire) {
        carteBancaireRepository.delete(carteBancaire);
    }
}
