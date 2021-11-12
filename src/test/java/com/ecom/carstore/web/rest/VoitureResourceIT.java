package com.ecom.carstore.web.rest;

import static com.ecom.carstore.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.ecom.carstore.IntegrationTest;
import com.ecom.carstore.domain.Voiture;
import com.ecom.carstore.domain.enumeration.Carburant;
import com.ecom.carstore.domain.enumeration.Etat;
import com.ecom.carstore.domain.enumeration.Statut;
import com.ecom.carstore.repository.VoitureRepository;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link VoitureResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class VoitureResourceIT {

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final Long DEFAULT_PRIX = 1L;
    private static final Long UPDATED_PRIX = 2L;

    private static final byte[] DEFAULT_IMAGE_1 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_1 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_1_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_1_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_2 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_2 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_2_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_2_CONTENT_TYPE = "image/png";

    private static final byte[] DEFAULT_IMAGE_3 = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE_3 = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_3_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_3_CONTENT_TYPE = "image/png";

    private static final Statut DEFAULT_STATUT = Statut.RESERVER;
    private static final Statut UPDATED_STATUT = Statut.LIBRE;

    private static final ZonedDateTime DEFAULT_MISE_EN_VENTE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_MISE_EN_VENTE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Etat DEFAULT_ETAT = Etat.NONROULANT;
    private static final Etat UPDATED_ETAT = Etat.ENDOMMAGE;

    private static final Integer DEFAULT_PORTE = 5;
    private static final Integer UPDATED_PORTE = 4;

    private static final Integer DEFAULT_BOITE_VITESSE = 1;
    private static final Integer UPDATED_BOITE_VITESSE = 2;

    private static final Integer DEFAULT_CO_2 = 1;
    private static final Integer UPDATED_CO_2 = 2;

    private static final Integer DEFAULT_CHEVAUX = 1;
    private static final Integer UPDATED_CHEVAUX = 2;

    private static final Carburant DEFAULT_CARBURANT = Carburant.ESSENCE;
    private static final Carburant UPDATED_CARBURANT = Carburant.DIESEL;

    private static final String ENTITY_API_URL = "/api/voitures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private VoitureRepository voitureRepository;

    @Mock
    private VoitureRepository voitureRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restVoitureMockMvc;

    private Voiture voiture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voiture createEntity(EntityManager em) {
        Voiture voiture = new Voiture()
            .model(DEFAULT_MODEL)
            .prix(DEFAULT_PRIX)
            .image1(DEFAULT_IMAGE_1)
            .image1ContentType(DEFAULT_IMAGE_1_CONTENT_TYPE)
            .image2(DEFAULT_IMAGE_2)
            .image2ContentType(DEFAULT_IMAGE_2_CONTENT_TYPE)
            .image3(DEFAULT_IMAGE_3)
            .image3ContentType(DEFAULT_IMAGE_3_CONTENT_TYPE)
            .statut(DEFAULT_STATUT)
            .miseEnVente(DEFAULT_MISE_EN_VENTE)
            .etat(DEFAULT_ETAT)
            .porte(DEFAULT_PORTE)
            .boiteVitesse(DEFAULT_BOITE_VITESSE)
            .co2(DEFAULT_CO_2)
            .chevaux(DEFAULT_CHEVAUX)
            .carburant(DEFAULT_CARBURANT);
        return voiture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Voiture createUpdatedEntity(EntityManager em) {
        Voiture voiture = new Voiture()
            .model(UPDATED_MODEL)
            .prix(UPDATED_PRIX)
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image3(UPDATED_IMAGE_3)
            .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE)
            .statut(UPDATED_STATUT)
            .miseEnVente(UPDATED_MISE_EN_VENTE)
            .etat(UPDATED_ETAT)
            .porte(UPDATED_PORTE)
            .boiteVitesse(UPDATED_BOITE_VITESSE)
            .co2(UPDATED_CO_2)
            .chevaux(UPDATED_CHEVAUX)
            .carburant(UPDATED_CARBURANT);
        return voiture;
    }

    @BeforeEach
    public void initTest() {
        voiture = createEntity(em);
    }

    @Test
    @Transactional
    void createVoiture() throws Exception {
        int databaseSizeBeforeCreate = voitureRepository.findAll().size();
        // Create the Voiture
        restVoitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isCreated());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeCreate + 1);
        Voiture testVoiture = voitureList.get(voitureList.size() - 1);
        assertThat(testVoiture.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testVoiture.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testVoiture.getImage1()).isEqualTo(DEFAULT_IMAGE_1);
        assertThat(testVoiture.getImage1ContentType()).isEqualTo(DEFAULT_IMAGE_1_CONTENT_TYPE);
        assertThat(testVoiture.getImage2()).isEqualTo(DEFAULT_IMAGE_2);
        assertThat(testVoiture.getImage2ContentType()).isEqualTo(DEFAULT_IMAGE_2_CONTENT_TYPE);
        assertThat(testVoiture.getImage3()).isEqualTo(DEFAULT_IMAGE_3);
        assertThat(testVoiture.getImage3ContentType()).isEqualTo(DEFAULT_IMAGE_3_CONTENT_TYPE);
        assertThat(testVoiture.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testVoiture.getMiseEnVente()).isEqualTo(DEFAULT_MISE_EN_VENTE);
        assertThat(testVoiture.getEtat()).isEqualTo(DEFAULT_ETAT);
        assertThat(testVoiture.getPorte()).isEqualTo(DEFAULT_PORTE);
        assertThat(testVoiture.getBoiteVitesse()).isEqualTo(DEFAULT_BOITE_VITESSE);
        assertThat(testVoiture.getCo2()).isEqualTo(DEFAULT_CO_2);
        assertThat(testVoiture.getChevaux()).isEqualTo(DEFAULT_CHEVAUX);
        assertThat(testVoiture.getCarburant()).isEqualTo(DEFAULT_CARBURANT);
    }

    @Test
    @Transactional
    void createVoitureWithExistingId() throws Exception {
        // Create the Voiture with an existing ID
        voiture.setId(1L);

        int databaseSizeBeforeCreate = voitureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restVoitureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllVoitures() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        // Get all the voitureList
        restVoitureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(voiture.getId().intValue())))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].prix").value(hasItem(DEFAULT_PRIX.intValue())))
            .andExpect(jsonPath("$.[*].image1ContentType").value(hasItem(DEFAULT_IMAGE_1_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image1").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_1))))
            .andExpect(jsonPath("$.[*].image2ContentType").value(hasItem(DEFAULT_IMAGE_2_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image2").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_2))))
            .andExpect(jsonPath("$.[*].image3ContentType").value(hasItem(DEFAULT_IMAGE_3_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image3").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE_3))))
            .andExpect(jsonPath("$.[*].statut").value(hasItem(DEFAULT_STATUT.toString())))
            .andExpect(jsonPath("$.[*].miseEnVente").value(hasItem(sameInstant(DEFAULT_MISE_EN_VENTE))))
            .andExpect(jsonPath("$.[*].etat").value(hasItem(DEFAULT_ETAT.toString())))
            .andExpect(jsonPath("$.[*].porte").value(hasItem(DEFAULT_PORTE)))
            .andExpect(jsonPath("$.[*].boiteVitesse").value(hasItem(DEFAULT_BOITE_VITESSE)))
            .andExpect(jsonPath("$.[*].co2").value(hasItem(DEFAULT_CO_2)))
            .andExpect(jsonPath("$.[*].chevaux").value(hasItem(DEFAULT_CHEVAUX)))
            .andExpect(jsonPath("$.[*].carburant").value(hasItem(DEFAULT_CARBURANT.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVoituresWithEagerRelationshipsIsEnabled() throws Exception {
        when(voitureRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVoitureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(voitureRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllVoituresWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(voitureRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restVoitureMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(voitureRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    void getVoiture() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        // Get the voiture
        restVoitureMockMvc
            .perform(get(ENTITY_API_URL_ID, voiture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(voiture.getId().intValue()))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.prix").value(DEFAULT_PRIX.intValue()))
            .andExpect(jsonPath("$.image1ContentType").value(DEFAULT_IMAGE_1_CONTENT_TYPE))
            .andExpect(jsonPath("$.image1").value(Base64Utils.encodeToString(DEFAULT_IMAGE_1)))
            .andExpect(jsonPath("$.image2ContentType").value(DEFAULT_IMAGE_2_CONTENT_TYPE))
            .andExpect(jsonPath("$.image2").value(Base64Utils.encodeToString(DEFAULT_IMAGE_2)))
            .andExpect(jsonPath("$.image3ContentType").value(DEFAULT_IMAGE_3_CONTENT_TYPE))
            .andExpect(jsonPath("$.image3").value(Base64Utils.encodeToString(DEFAULT_IMAGE_3)))
            .andExpect(jsonPath("$.statut").value(DEFAULT_STATUT.toString()))
            .andExpect(jsonPath("$.miseEnVente").value(sameInstant(DEFAULT_MISE_EN_VENTE)))
            .andExpect(jsonPath("$.etat").value(DEFAULT_ETAT.toString()))
            .andExpect(jsonPath("$.porte").value(DEFAULT_PORTE))
            .andExpect(jsonPath("$.boiteVitesse").value(DEFAULT_BOITE_VITESSE))
            .andExpect(jsonPath("$.co2").value(DEFAULT_CO_2))
            .andExpect(jsonPath("$.chevaux").value(DEFAULT_CHEVAUX))
            .andExpect(jsonPath("$.carburant").value(DEFAULT_CARBURANT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingVoiture() throws Exception {
        // Get the voiture
        restVoitureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewVoiture() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();

        // Update the voiture
        Voiture updatedVoiture = voitureRepository.findById(voiture.getId()).get();
        // Disconnect from session so that the updates on updatedVoiture are not directly saved in db
        em.detach(updatedVoiture);
        updatedVoiture
            .model(UPDATED_MODEL)
            .prix(UPDATED_PRIX)
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image3(UPDATED_IMAGE_3)
            .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE)
            .statut(UPDATED_STATUT)
            .miseEnVente(UPDATED_MISE_EN_VENTE)
            .etat(UPDATED_ETAT)
            .porte(UPDATED_PORTE)
            .boiteVitesse(UPDATED_BOITE_VITESSE)
            .co2(UPDATED_CO_2)
            .chevaux(UPDATED_CHEVAUX)
            .carburant(UPDATED_CARBURANT);

        restVoitureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedVoiture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedVoiture))
            )
            .andExpect(status().isOk());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
        Voiture testVoiture = voitureList.get(voitureList.size() - 1);
        assertThat(testVoiture.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testVoiture.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testVoiture.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testVoiture.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testVoiture.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testVoiture.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testVoiture.getImage3()).isEqualTo(UPDATED_IMAGE_3);
        assertThat(testVoiture.getImage3ContentType()).isEqualTo(UPDATED_IMAGE_3_CONTENT_TYPE);
        assertThat(testVoiture.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testVoiture.getMiseEnVente()).isEqualTo(UPDATED_MISE_EN_VENTE);
        assertThat(testVoiture.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testVoiture.getPorte()).isEqualTo(UPDATED_PORTE);
        assertThat(testVoiture.getBoiteVitesse()).isEqualTo(UPDATED_BOITE_VITESSE);
        assertThat(testVoiture.getCo2()).isEqualTo(UPDATED_CO_2);
        assertThat(testVoiture.getChevaux()).isEqualTo(UPDATED_CHEVAUX);
        assertThat(testVoiture.getCarburant()).isEqualTo(UPDATED_CARBURANT);
    }

    @Test
    @Transactional
    void putNonExistingVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();
        voiture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoitureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, voiture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voiture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();
        voiture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoitureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(voiture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();
        voiture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoitureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateVoitureWithPatch() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();

        // Update the voiture using partial update
        Voiture partialUpdatedVoiture = new Voiture();
        partialUpdatedVoiture.setId(voiture.getId());

        partialUpdatedVoiture.image1(UPDATED_IMAGE_1).image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE).etat(UPDATED_ETAT).co2(UPDATED_CO_2);

        restVoitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoiture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoiture))
            )
            .andExpect(status().isOk());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
        Voiture testVoiture = voitureList.get(voitureList.size() - 1);
        assertThat(testVoiture.getModel()).isEqualTo(DEFAULT_MODEL);
        assertThat(testVoiture.getPrix()).isEqualTo(DEFAULT_PRIX);
        assertThat(testVoiture.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testVoiture.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testVoiture.getImage2()).isEqualTo(DEFAULT_IMAGE_2);
        assertThat(testVoiture.getImage2ContentType()).isEqualTo(DEFAULT_IMAGE_2_CONTENT_TYPE);
        assertThat(testVoiture.getImage3()).isEqualTo(DEFAULT_IMAGE_3);
        assertThat(testVoiture.getImage3ContentType()).isEqualTo(DEFAULT_IMAGE_3_CONTENT_TYPE);
        assertThat(testVoiture.getStatut()).isEqualTo(DEFAULT_STATUT);
        assertThat(testVoiture.getMiseEnVente()).isEqualTo(DEFAULT_MISE_EN_VENTE);
        assertThat(testVoiture.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testVoiture.getPorte()).isEqualTo(DEFAULT_PORTE);
        assertThat(testVoiture.getBoiteVitesse()).isEqualTo(DEFAULT_BOITE_VITESSE);
        assertThat(testVoiture.getCo2()).isEqualTo(UPDATED_CO_2);
        assertThat(testVoiture.getChevaux()).isEqualTo(DEFAULT_CHEVAUX);
        assertThat(testVoiture.getCarburant()).isEqualTo(DEFAULT_CARBURANT);
    }

    @Test
    @Transactional
    void fullUpdateVoitureWithPatch() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();

        // Update the voiture using partial update
        Voiture partialUpdatedVoiture = new Voiture();
        partialUpdatedVoiture.setId(voiture.getId());

        partialUpdatedVoiture
            .model(UPDATED_MODEL)
            .prix(UPDATED_PRIX)
            .image1(UPDATED_IMAGE_1)
            .image1ContentType(UPDATED_IMAGE_1_CONTENT_TYPE)
            .image2(UPDATED_IMAGE_2)
            .image2ContentType(UPDATED_IMAGE_2_CONTENT_TYPE)
            .image3(UPDATED_IMAGE_3)
            .image3ContentType(UPDATED_IMAGE_3_CONTENT_TYPE)
            .statut(UPDATED_STATUT)
            .miseEnVente(UPDATED_MISE_EN_VENTE)
            .etat(UPDATED_ETAT)
            .porte(UPDATED_PORTE)
            .boiteVitesse(UPDATED_BOITE_VITESSE)
            .co2(UPDATED_CO_2)
            .chevaux(UPDATED_CHEVAUX)
            .carburant(UPDATED_CARBURANT);

        restVoitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedVoiture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedVoiture))
            )
            .andExpect(status().isOk());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
        Voiture testVoiture = voitureList.get(voitureList.size() - 1);
        assertThat(testVoiture.getModel()).isEqualTo(UPDATED_MODEL);
        assertThat(testVoiture.getPrix()).isEqualTo(UPDATED_PRIX);
        assertThat(testVoiture.getImage1()).isEqualTo(UPDATED_IMAGE_1);
        assertThat(testVoiture.getImage1ContentType()).isEqualTo(UPDATED_IMAGE_1_CONTENT_TYPE);
        assertThat(testVoiture.getImage2()).isEqualTo(UPDATED_IMAGE_2);
        assertThat(testVoiture.getImage2ContentType()).isEqualTo(UPDATED_IMAGE_2_CONTENT_TYPE);
        assertThat(testVoiture.getImage3()).isEqualTo(UPDATED_IMAGE_3);
        assertThat(testVoiture.getImage3ContentType()).isEqualTo(UPDATED_IMAGE_3_CONTENT_TYPE);
        assertThat(testVoiture.getStatut()).isEqualTo(UPDATED_STATUT);
        assertThat(testVoiture.getMiseEnVente()).isEqualTo(UPDATED_MISE_EN_VENTE);
        assertThat(testVoiture.getEtat()).isEqualTo(UPDATED_ETAT);
        assertThat(testVoiture.getPorte()).isEqualTo(UPDATED_PORTE);
        assertThat(testVoiture.getBoiteVitesse()).isEqualTo(UPDATED_BOITE_VITESSE);
        assertThat(testVoiture.getCo2()).isEqualTo(UPDATED_CO_2);
        assertThat(testVoiture.getChevaux()).isEqualTo(UPDATED_CHEVAUX);
        assertThat(testVoiture.getCarburant()).isEqualTo(UPDATED_CARBURANT);
    }

    @Test
    @Transactional
    void patchNonExistingVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();
        voiture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVoitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, voiture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voiture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();
        voiture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoitureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(voiture))
            )
            .andExpect(status().isBadRequest());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamVoiture() throws Exception {
        int databaseSizeBeforeUpdate = voitureRepository.findAll().size();
        voiture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restVoitureMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(voiture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Voiture in the database
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteVoiture() throws Exception {
        // Initialize the database
        voitureRepository.saveAndFlush(voiture);

        int databaseSizeBeforeDelete = voitureRepository.findAll().size();

        // Delete the voiture
        restVoitureMockMvc
            .perform(delete(ENTITY_API_URL_ID, voiture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Voiture> voitureList = voitureRepository.findAll();
        assertThat(voitureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
