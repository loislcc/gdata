package edu.buaa.web.rest;

import edu.buaa.GdataApp;
import edu.buaa.domain.Maprelation;
import edu.buaa.repository.MaprelationRepository;
import edu.buaa.service.MaprelationService;
import edu.buaa.web.rest.errors.ExceptionTranslator;
import edu.buaa.service.dto.MaprelationCriteria;
import edu.buaa.service.MaprelationQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static edu.buaa.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link MaprelationResource} REST controller.
 */
@SpringBootTest(classes = GdataApp.class)
public class MaprelationResourceIT {

    private static final String DEFAULT_VNODE = "AAAAAAAAAA";
    private static final String UPDATED_VNODE = "BBBBBBBBBB";

    private static final String DEFAULT_RNODE = "AAAAAAAAAA";
    private static final String UPDATED_RNODE = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_SIZE = 1D;
    private static final Double UPDATED_SIZE = 2D;
    private static final Double SMALLER_SIZE = 1D - 1D;

    @Autowired
    private MaprelationRepository maprelationRepository;

    @Autowired
    private MaprelationService maprelationService;

    @Autowired
    private MaprelationQueryService maprelationQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMaprelationMockMvc;

    private Maprelation maprelation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MaprelationResource maprelationResource = new MaprelationResource(maprelationService, maprelationQueryService);
        this.restMaprelationMockMvc = MockMvcBuilders.standaloneSetup(maprelationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maprelation createEntity(EntityManager em) {
        Maprelation maprelation = new Maprelation()
            .vnode(DEFAULT_VNODE)
            .rnode(DEFAULT_RNODE)
            .status(DEFAULT_STATUS)
            .size(DEFAULT_SIZE);
        return maprelation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Maprelation createUpdatedEntity(EntityManager em) {
        Maprelation maprelation = new Maprelation()
            .vnode(UPDATED_VNODE)
            .rnode(UPDATED_RNODE)
            .status(UPDATED_STATUS)
            .size(UPDATED_SIZE);
        return maprelation;
    }

    @BeforeEach
    public void initTest() {
        maprelation = createEntity(em);
    }

    @Test
    @Transactional
    public void createMaprelation() throws Exception {
        int databaseSizeBeforeCreate = maprelationRepository.findAll().size();

        // Create the Maprelation
        restMaprelationMockMvc.perform(post("/api/maprelations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maprelation)))
            .andExpect(status().isCreated());

        // Validate the Maprelation in the database
        List<Maprelation> maprelationList = maprelationRepository.findAll();
        assertThat(maprelationList).hasSize(databaseSizeBeforeCreate + 1);
        Maprelation testMaprelation = maprelationList.get(maprelationList.size() - 1);
        assertThat(testMaprelation.getVnode()).isEqualTo(DEFAULT_VNODE);
        assertThat(testMaprelation.getRnode()).isEqualTo(DEFAULT_RNODE);
        assertThat(testMaprelation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMaprelation.getSize()).isEqualTo(DEFAULT_SIZE);
    }

    @Test
    @Transactional
    public void createMaprelationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = maprelationRepository.findAll().size();

        // Create the Maprelation with an existing ID
        maprelation.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaprelationMockMvc.perform(post("/api/maprelations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maprelation)))
            .andExpect(status().isBadRequest());

        // Validate the Maprelation in the database
        List<Maprelation> maprelationList = maprelationRepository.findAll();
        assertThat(maprelationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMaprelations() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList
        restMaprelationMockMvc.perform(get("/api/maprelations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maprelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].vnode").value(hasItem(DEFAULT_VNODE)))
            .andExpect(jsonPath("$.[*].rnode").value(hasItem(DEFAULT_RNODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getMaprelation() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get the maprelation
        restMaprelationMockMvc.perform(get("/api/maprelations/{id}", maprelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(maprelation.getId().intValue()))
            .andExpect(jsonPath("$.vnode").value(DEFAULT_VNODE))
            .andExpect(jsonPath("$.rnode").value(DEFAULT_RNODE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.doubleValue()));
    }


    @Test
    @Transactional
    public void getMaprelationsByIdFiltering() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        Long id = maprelation.getId();

        defaultMaprelationShouldBeFound("id.equals=" + id);
        defaultMaprelationShouldNotBeFound("id.notEquals=" + id);

        defaultMaprelationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMaprelationShouldNotBeFound("id.greaterThan=" + id);

        defaultMaprelationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMaprelationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMaprelationsByVnodeIsEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where vnode equals to DEFAULT_VNODE
        defaultMaprelationShouldBeFound("vnode.equals=" + DEFAULT_VNODE);

        // Get all the maprelationList where vnode equals to UPDATED_VNODE
        defaultMaprelationShouldNotBeFound("vnode.equals=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByVnodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where vnode not equals to DEFAULT_VNODE
        defaultMaprelationShouldNotBeFound("vnode.notEquals=" + DEFAULT_VNODE);

        // Get all the maprelationList where vnode not equals to UPDATED_VNODE
        defaultMaprelationShouldBeFound("vnode.notEquals=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByVnodeIsInShouldWork() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where vnode in DEFAULT_VNODE or UPDATED_VNODE
        defaultMaprelationShouldBeFound("vnode.in=" + DEFAULT_VNODE + "," + UPDATED_VNODE);

        // Get all the maprelationList where vnode equals to UPDATED_VNODE
        defaultMaprelationShouldNotBeFound("vnode.in=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByVnodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where vnode is not null
        defaultMaprelationShouldBeFound("vnode.specified=true");

        // Get all the maprelationList where vnode is null
        defaultMaprelationShouldNotBeFound("vnode.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaprelationsByVnodeContainsSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where vnode contains DEFAULT_VNODE
        defaultMaprelationShouldBeFound("vnode.contains=" + DEFAULT_VNODE);

        // Get all the maprelationList where vnode contains UPDATED_VNODE
        defaultMaprelationShouldNotBeFound("vnode.contains=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByVnodeNotContainsSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where vnode does not contain DEFAULT_VNODE
        defaultMaprelationShouldNotBeFound("vnode.doesNotContain=" + DEFAULT_VNODE);

        // Get all the maprelationList where vnode does not contain UPDATED_VNODE
        defaultMaprelationShouldBeFound("vnode.doesNotContain=" + UPDATED_VNODE);
    }


    @Test
    @Transactional
    public void getAllMaprelationsByRnodeIsEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where rnode equals to DEFAULT_RNODE
        defaultMaprelationShouldBeFound("rnode.equals=" + DEFAULT_RNODE);

        // Get all the maprelationList where rnode equals to UPDATED_RNODE
        defaultMaprelationShouldNotBeFound("rnode.equals=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByRnodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where rnode not equals to DEFAULT_RNODE
        defaultMaprelationShouldNotBeFound("rnode.notEquals=" + DEFAULT_RNODE);

        // Get all the maprelationList where rnode not equals to UPDATED_RNODE
        defaultMaprelationShouldBeFound("rnode.notEquals=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByRnodeIsInShouldWork() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where rnode in DEFAULT_RNODE or UPDATED_RNODE
        defaultMaprelationShouldBeFound("rnode.in=" + DEFAULT_RNODE + "," + UPDATED_RNODE);

        // Get all the maprelationList where rnode equals to UPDATED_RNODE
        defaultMaprelationShouldNotBeFound("rnode.in=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByRnodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where rnode is not null
        defaultMaprelationShouldBeFound("rnode.specified=true");

        // Get all the maprelationList where rnode is null
        defaultMaprelationShouldNotBeFound("rnode.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaprelationsByRnodeContainsSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where rnode contains DEFAULT_RNODE
        defaultMaprelationShouldBeFound("rnode.contains=" + DEFAULT_RNODE);

        // Get all the maprelationList where rnode contains UPDATED_RNODE
        defaultMaprelationShouldNotBeFound("rnode.contains=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByRnodeNotContainsSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where rnode does not contain DEFAULT_RNODE
        defaultMaprelationShouldNotBeFound("rnode.doesNotContain=" + DEFAULT_RNODE);

        // Get all the maprelationList where rnode does not contain UPDATED_RNODE
        defaultMaprelationShouldBeFound("rnode.doesNotContain=" + UPDATED_RNODE);
    }


    @Test
    @Transactional
    public void getAllMaprelationsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where status equals to DEFAULT_STATUS
        defaultMaprelationShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the maprelationList where status equals to UPDATED_STATUS
        defaultMaprelationShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where status not equals to DEFAULT_STATUS
        defaultMaprelationShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the maprelationList where status not equals to UPDATED_STATUS
        defaultMaprelationShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultMaprelationShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the maprelationList where status equals to UPDATED_STATUS
        defaultMaprelationShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where status is not null
        defaultMaprelationShouldBeFound("status.specified=true");

        // Get all the maprelationList where status is null
        defaultMaprelationShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllMaprelationsByStatusContainsSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where status contains DEFAULT_STATUS
        defaultMaprelationShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the maprelationList where status contains UPDATED_STATUS
        defaultMaprelationShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllMaprelationsByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where status does not contain DEFAULT_STATUS
        defaultMaprelationShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the maprelationList where status does not contain UPDATED_STATUS
        defaultMaprelationShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }


    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size equals to DEFAULT_SIZE
        defaultMaprelationShouldBeFound("size.equals=" + DEFAULT_SIZE);

        // Get all the maprelationList where size equals to UPDATED_SIZE
        defaultMaprelationShouldNotBeFound("size.equals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size not equals to DEFAULT_SIZE
        defaultMaprelationShouldNotBeFound("size.notEquals=" + DEFAULT_SIZE);

        // Get all the maprelationList where size not equals to UPDATED_SIZE
        defaultMaprelationShouldBeFound("size.notEquals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsInShouldWork() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size in DEFAULT_SIZE or UPDATED_SIZE
        defaultMaprelationShouldBeFound("size.in=" + DEFAULT_SIZE + "," + UPDATED_SIZE);

        // Get all the maprelationList where size equals to UPDATED_SIZE
        defaultMaprelationShouldNotBeFound("size.in=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size is not null
        defaultMaprelationShouldBeFound("size.specified=true");

        // Get all the maprelationList where size is null
        defaultMaprelationShouldNotBeFound("size.specified=false");
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size is greater than or equal to DEFAULT_SIZE
        defaultMaprelationShouldBeFound("size.greaterThanOrEqual=" + DEFAULT_SIZE);

        // Get all the maprelationList where size is greater than or equal to UPDATED_SIZE
        defaultMaprelationShouldNotBeFound("size.greaterThanOrEqual=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size is less than or equal to DEFAULT_SIZE
        defaultMaprelationShouldBeFound("size.lessThanOrEqual=" + DEFAULT_SIZE);

        // Get all the maprelationList where size is less than or equal to SMALLER_SIZE
        defaultMaprelationShouldNotBeFound("size.lessThanOrEqual=" + SMALLER_SIZE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsLessThanSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size is less than DEFAULT_SIZE
        defaultMaprelationShouldNotBeFound("size.lessThan=" + DEFAULT_SIZE);

        // Get all the maprelationList where size is less than UPDATED_SIZE
        defaultMaprelationShouldBeFound("size.lessThan=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllMaprelationsBySizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        maprelationRepository.saveAndFlush(maprelation);

        // Get all the maprelationList where size is greater than DEFAULT_SIZE
        defaultMaprelationShouldNotBeFound("size.greaterThan=" + DEFAULT_SIZE);

        // Get all the maprelationList where size is greater than SMALLER_SIZE
        defaultMaprelationShouldBeFound("size.greaterThan=" + SMALLER_SIZE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaprelationShouldBeFound(String filter) throws Exception {
        restMaprelationMockMvc.perform(get("/api/maprelations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maprelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].vnode").value(hasItem(DEFAULT_VNODE)))
            .andExpect(jsonPath("$.[*].rnode").value(hasItem(DEFAULT_RNODE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.doubleValue())));

        // Check, that the count call also returns 1
        restMaprelationMockMvc.perform(get("/api/maprelations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaprelationShouldNotBeFound(String filter) throws Exception {
        restMaprelationMockMvc.perform(get("/api/maprelations?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaprelationMockMvc.perform(get("/api/maprelations/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMaprelation() throws Exception {
        // Get the maprelation
        restMaprelationMockMvc.perform(get("/api/maprelations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMaprelation() throws Exception {
        // Initialize the database
        maprelationService.save(maprelation);

        int databaseSizeBeforeUpdate = maprelationRepository.findAll().size();

        // Update the maprelation
        Maprelation updatedMaprelation = maprelationRepository.findById(maprelation.getId()).get();
        // Disconnect from session so that the updates on updatedMaprelation are not directly saved in db
        em.detach(updatedMaprelation);
        updatedMaprelation
            .vnode(UPDATED_VNODE)
            .rnode(UPDATED_RNODE)
            .status(UPDATED_STATUS)
            .size(UPDATED_SIZE);

        restMaprelationMockMvc.perform(put("/api/maprelations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMaprelation)))
            .andExpect(status().isOk());

        // Validate the Maprelation in the database
        List<Maprelation> maprelationList = maprelationRepository.findAll();
        assertThat(maprelationList).hasSize(databaseSizeBeforeUpdate);
        Maprelation testMaprelation = maprelationList.get(maprelationList.size() - 1);
        assertThat(testMaprelation.getVnode()).isEqualTo(UPDATED_VNODE);
        assertThat(testMaprelation.getRnode()).isEqualTo(UPDATED_RNODE);
        assertThat(testMaprelation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMaprelation.getSize()).isEqualTo(UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void updateNonExistingMaprelation() throws Exception {
        int databaseSizeBeforeUpdate = maprelationRepository.findAll().size();

        // Create the Maprelation

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaprelationMockMvc.perform(put("/api/maprelations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(maprelation)))
            .andExpect(status().isBadRequest());

        // Validate the Maprelation in the database
        List<Maprelation> maprelationList = maprelationRepository.findAll();
        assertThat(maprelationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMaprelation() throws Exception {
        // Initialize the database
        maprelationService.save(maprelation);

        int databaseSizeBeforeDelete = maprelationRepository.findAll().size();

        // Delete the maprelation
        restMaprelationMockMvc.perform(delete("/api/maprelations/{id}", maprelation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Maprelation> maprelationList = maprelationRepository.findAll();
        assertThat(maprelationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
