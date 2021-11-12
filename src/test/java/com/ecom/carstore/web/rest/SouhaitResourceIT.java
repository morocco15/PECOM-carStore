package com.ecom.carstore.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.carstore.IntegrationTest;
import com.ecom.carstore.domain.Souhait;
import com.ecom.carstore.repository.SouhaitRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link SouhaitResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SouhaitResourceIT {

    private static final String ENTITY_API_URL = "/api/souhaits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private SouhaitRepository souhaitRepository;

    @Mock
    private SouhaitRepository souhaitRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSouhaitMockMvc;

    private Souhait souhait;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Souhait createEntity(EntityManager em) {
        Souhait souhait = new Souhait();
        return souhait;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Souhait createUpdatedEntity(EntityManager em) {
        Souhait souhait = new Souhait();
        return souhait;
    }

    @BeforeEach
    public void initTest() {
        souhait = createEntity(em);
    }

    @Test
    @Transactional
    void createSouhait() throws Exception {
        int databaseSizeBeforeCreate = souhaitRepository.findAll().size();
        // Create the Souhait
        restSouhaitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(souhait)))
            .andExpect(status().isCreated());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeCreate + 1);
        Souhait testSouhait = souhaitList.get(souhaitList.size() - 1);
    }

    @Test
    @Transactional
    void createSouhaitWithExistingId() throws Exception {
        // Create the Souhait with an existing ID
        souhait.setId(1L);

        int databaseSizeBeforeCreate = souhaitRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSouhaitMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(souhait)))
            .andExpect(status().isBadRequest());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSouhaits() throws Exception {
        // Initialize the database
        souhaitRepository.saveAndFlush(souhait);

        // Get all the souhaitList
        restSouhaitMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(souhait.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSouhaitsWithEagerRelationshipsIsEnabled() throws Exception {
        when(souhaitRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSouhaitMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(souhaitRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSouhaitsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(souhaitRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSouhaitMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(souhaitRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getSouhait() throws Exception {
        // Initialize the database
        souhaitRepository.saveAndFlush(souhait);

        // Get the souhait
        restSouhaitMockMvc
            .perform(get(ENTITY_API_URL_ID, souhait.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(souhait.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSouhait() throws Exception {
        // Get the souhait
        restSouhaitMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewSouhait() throws Exception {
        // Initialize the database
        souhaitRepository.saveAndFlush(souhait);

        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();

        // Update the souhait
        Souhait updatedSouhait = souhaitRepository.findById(souhait.getId()).get();
        // Disconnect from session so that the updates on updatedSouhait are not directly saved in db
        em.detach(updatedSouhait);

        restSouhaitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedSouhait.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedSouhait))
            )
            .andExpect(status().isOk());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
        Souhait testSouhait = souhaitList.get(souhaitList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingSouhait() throws Exception {
        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();
        souhait.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSouhaitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, souhait.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(souhait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSouhait() throws Exception {
        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();
        souhait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouhaitMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(souhait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSouhait() throws Exception {
        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();
        souhait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouhaitMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(souhait)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSouhaitWithPatch() throws Exception {
        // Initialize the database
        souhaitRepository.saveAndFlush(souhait);

        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();

        // Update the souhait using partial update
        Souhait partialUpdatedSouhait = new Souhait();
        partialUpdatedSouhait.setId(souhait.getId());

        restSouhaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSouhait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSouhait))
            )
            .andExpect(status().isOk());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
        Souhait testSouhait = souhaitList.get(souhaitList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateSouhaitWithPatch() throws Exception {
        // Initialize the database
        souhaitRepository.saveAndFlush(souhait);

        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();

        // Update the souhait using partial update
        Souhait partialUpdatedSouhait = new Souhait();
        partialUpdatedSouhait.setId(souhait.getId());

        restSouhaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSouhait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedSouhait))
            )
            .andExpect(status().isOk());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
        Souhait testSouhait = souhaitList.get(souhaitList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingSouhait() throws Exception {
        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();
        souhait.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSouhaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, souhait.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(souhait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSouhait() throws Exception {
        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();
        souhait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouhaitMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(souhait))
            )
            .andExpect(status().isBadRequest());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSouhait() throws Exception {
        int databaseSizeBeforeUpdate = souhaitRepository.findAll().size();
        souhait.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSouhaitMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(souhait)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Souhait in the database
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSouhait() throws Exception {
        // Initialize the database
        souhaitRepository.saveAndFlush(souhait);

        int databaseSizeBeforeDelete = souhaitRepository.findAll().size();

        // Delete the souhait
        restSouhaitMockMvc
            .perform(delete(ENTITY_API_URL_ID, souhait.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Souhait> souhaitList = souhaitRepository.findAll();
        assertThat(souhaitList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
