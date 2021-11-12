package com.ecom.carstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.carstore.IntegrationTest;
import com.ecom.carstore.domain.Vendeur;
import com.ecom.carstore.repository.VendeurRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link VendeurResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class VendeurResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_COORDONNEE = "AAAAAAAAAA";
    private static final String UPDATED_COORDONNEE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/vendeurs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VendeurRepository vendeurRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVendeurMockMvc;

    private Vendeur vendeur;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendeur createEntity(EntityManager em) {
        Vendeur vendeur = new Vendeur().login(DEFAULT_LOGIN).coordonnee(DEFAULT_COORDONNEE);
        return vendeur;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Vendeur createUpdatedEntity(EntityManager em) {
        Vendeur vendeur = new Vendeur().login(UPDATED_LOGIN).coordonnee(UPDATED_COORDONNEE);
        return vendeur;
    }

    @BeforeEach
    public void initTest() {
        vendeur = createEntity(em);
    }

    @Test
    @Transactional
    void createVendeur() throws Exception {
        int databaseSizeBeforeCreate = vendeurRepository.findAll().size();
        // Create the Vendeur
        restVendeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendeur)))
            .andExpect(status().isCreated());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeCreate + 1);
        Vendeur testVendeur = vendeurList.get(vendeurList.size() - 1);
        assertThat(testVendeur.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(testVendeur.getCoordonnee()).isEqualTo(DEFAULT_COORDONNEE);
    }

    @Test
    @Transactional
    void createVendeurWithExistingId() throws Exception {
        // Create the Vendeur with an existing ID
        vendeur.setId(1L);

        int databaseSizeBeforeCreate = vendeurRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVendeurMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendeur)))
            .andExpect(status().isBadRequest());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVendeurs() throws Exception {
        // Initialize the database
        vendeurRepository.saveAndFlush(vendeur);

        // Get all the vendeurList
        restVendeurMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(vendeur.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].coordonnee").value(hasItem(DEFAULT_COORDONNEE)));
    }

    @Test
    @Transactional
    void getVendeur() throws Exception {
        // Initialize the database
        vendeurRepository.saveAndFlush(vendeur);

        // Get the vendeur
        restVendeurMockMvc
            .perform(get(ENTITY_API_URL_ID, vendeur.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(vendeur.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.coordonnee").value(DEFAULT_COORDONNEE));
    }

    @Test
    @Transactional
    void getNonExistingVendeur() throws Exception {
        // Get the vendeur
        restVendeurMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVendeur() throws Exception {
        // Initialize the database
        vendeurRepository.saveAndFlush(vendeur);

        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();

        // Update the vendeur
        Vendeur updatedVendeur = vendeurRepository.findById(vendeur.getId()).get();
        // Disconnect from session so that the updates on updatedVendeur are not directly saved in db
        em.detach(updatedVendeur);
        updatedVendeur.login(UPDATED_LOGIN).coordonnee(UPDATED_COORDONNEE);

        restVendeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVendeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVendeur))
            )
            .andExpect(status().isOk());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
        Vendeur testVendeur = vendeurList.get(vendeurList.size() - 1);
        assertThat(testVendeur.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testVendeur.getCoordonnee()).isEqualTo(UPDATED_COORDONNEE);
    }

    @Test
    @Transactional
    void putNonExistingVendeur() throws Exception {
        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();
        vendeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, vendeur.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVendeur() throws Exception {
        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();
        vendeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendeurMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(vendeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVendeur() throws Exception {
        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();
        vendeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendeurMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(vendeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVendeurWithPatch() throws Exception {
        // Initialize the database
        vendeurRepository.saveAndFlush(vendeur);

        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();

        // Update the vendeur using partial update
        Vendeur partialUpdatedVendeur = new Vendeur();
        partialUpdatedVendeur.setId(vendeur.getId());

        partialUpdatedVendeur.login(UPDATED_LOGIN);

        restVendeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendeur))
            )
            .andExpect(status().isOk());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
        Vendeur testVendeur = vendeurList.get(vendeurList.size() - 1);
        assertThat(testVendeur.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testVendeur.getCoordonnee()).isEqualTo(DEFAULT_COORDONNEE);
    }

    @Test
    @Transactional
    void fullUpdateVendeurWithPatch() throws Exception {
        // Initialize the database
        vendeurRepository.saveAndFlush(vendeur);

        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();

        // Update the vendeur using partial update
        Vendeur partialUpdatedVendeur = new Vendeur();
        partialUpdatedVendeur.setId(vendeur.getId());

        partialUpdatedVendeur.login(UPDATED_LOGIN).coordonnee(UPDATED_COORDONNEE);

        restVendeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVendeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVendeur))
            )
            .andExpect(status().isOk());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
        Vendeur testVendeur = vendeurList.get(vendeurList.size() - 1);
        assertThat(testVendeur.getLogin()).isEqualTo(UPDATED_LOGIN);
        assertThat(testVendeur.getCoordonnee()).isEqualTo(UPDATED_COORDONNEE);
    }

    @Test
    @Transactional
    void patchNonExistingVendeur() throws Exception {
        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();
        vendeur.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVendeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, vendeur.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVendeur() throws Exception {
        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();
        vendeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendeurMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(vendeur))
            )
            .andExpect(status().isBadRequest());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVendeur() throws Exception {
        int databaseSizeBeforeUpdate = vendeurRepository.findAll().size();
        vendeur.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVendeurMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(vendeur)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Vendeur in the database
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVendeur() throws Exception {
        // Initialize the database
        vendeurRepository.saveAndFlush(vendeur);

        int databaseSizeBeforeDelete = vendeurRepository.findAll().size();

        // Delete the vendeur
        restVendeurMockMvc
            .perform(delete(ENTITY_API_URL_ID, vendeur.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Vendeur> vendeurList = vendeurRepository.findAll();
        assertThat(vendeurList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
