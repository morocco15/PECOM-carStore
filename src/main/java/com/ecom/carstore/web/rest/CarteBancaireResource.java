package com.ecom.carstore.web.rest;

import com.ecom.carstore.domain.CarteBancaire;
import com.ecom.carstore.repository.CarteBancaireRepository;
import com.ecom.carstore.service.CarteBancaireService;
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
 * REST controller for managing {@link com.ecom.carstore.domain.CarteBancaire}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CarteBancaireResource {

    private final Logger log = LoggerFactory.getLogger(CarteBancaireResource.class);

    private static final String ENTITY_NAME = "carteBancaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarteBancaireService carteBancaireService;

    public CarteBancaireResource(CarteBancaireService carteBancaireService) {
        this.carteBancaireService = carteBancaireService;
    }

    /**
     * {@code POST  /carte-bancaires} : Create a new carteBancaire.
     *
     * @param carteBancaire the carteBancaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carteBancaire, or with status {@code 400 (Bad Request)} if the carteBancaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/carte-bancaires")
    public ResponseEntity<CarteBancaire> createCarteBancaire(@Valid @RequestBody CarteBancaire carteBancaire) throws URISyntaxException {
        return carteBancaireService.createCarteBancaire(carteBancaire);
    }

    /**
     * {@code PUT  /carte-bancaires/:id} : Updates an existing carteBancaire.
     *
     * @param id the id of the carteBancaire to save.
     * @param carteBancaire the carteBancaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteBancaire,
     * or with status {@code 400 (Bad Request)} if the carteBancaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carteBancaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/carte-bancaires/{id}")
    public ResponseEntity<CarteBancaire> updateCarteBancaire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarteBancaire carteBancaire
    ) throws URISyntaxException {
        return carteBancaireService.updateCarteBancaire(id, carteBancaire);
    }

    /**
     * {@code PATCH  /carte-bancaires/:id} : Partial updates given fields of an existing carteBancaire, field will ignore if it is null
     *
     * @param id the id of the carteBancaire to save.
     * @param carteBancaire the carteBancaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteBancaire,
     * or with status {@code 400 (Bad Request)} if the carteBancaire is not valid,
     * or with status {@code 404 (Not Found)} if the carteBancaire is not found,
     * or with status {@code 500 (Internal Server Error)} if the carteBancaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/carte-bancaires/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarteBancaire> partialUpdateCarteBancaire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarteBancaire carteBancaire
    ) throws URISyntaxException {
        return carteBancaireService.partialUpdateCarteBancaire(id, carteBancaire);
    }

    /**
     * {@code GET  /carte-bancaires} : get all the carteBancaires.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carteBancaires in body.
     */
    @GetMapping("/carte-bancaires")
    public List<CarteBancaire> getAllCarteBancaires() {
        return carteBancaireService.getAllCarteBancaires();
    }

    /**
     * {@code GET  /carte-bancaires/:id} : get the "id" carteBancaire.
     *
     * @param id the id of the carteBancaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carteBancaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carte-bancaires/{id}")
    public ResponseEntity<CarteBancaire> getCarteBancaire(@PathVariable Long id) {
        return carteBancaireService.getCarteBancaire(id);
    }

    /**
     * {@code DELETE  /carte-bancaires/:id} : delete the "id" carteBancaire.
     *
     * @param id the id of the carteBancaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/carte-bancaires/{id}")
    public ResponseEntity<Void> deleteCarteBancaire(@PathVariable Long id) {
        return carteBancaireService.deleteCarteBancaire(id);
    }
}
