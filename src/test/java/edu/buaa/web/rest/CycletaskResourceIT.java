package edu.buaa.web.rest;

import edu.buaa.GdataApp;
import edu.buaa.domain.Cycletask;
import edu.buaa.repository.CycletaskRepository;
import edu.buaa.service.CycletaskService;
import edu.buaa.web.rest.errors.ExceptionTranslator;
import edu.buaa.service.dto.CycletaskCriteria;
import edu.buaa.service.CycletaskQueryService;

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
 * Integration tests for the {@link CycletaskResource} REST controller.
 */
@SpringBootTest(classes = GdataApp.class)
public class CycletaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CYCLE = "AAAAAAAAAA";
    private static final String UPDATED_CYCLE = "BBBBBBBBBB";

    private static final String DEFAULT_NEXTIME = "AAAAAAAAAA";
    private static final String UPDATED_NEXTIME = "BBBBBBBBBB";

    private static final String DEFAULT_NEXTENDTIME = "AAAAAAAAAA";
    private static final String UPDATED_NEXTENDTIME = "BBBBBBBBBB";

    private static final Long DEFAULT_TASKID = 1L;
    private static final Long UPDATED_TASKID = 2L;
    private static final Long SMALLER_TASKID = 1L - 1L;

    @Autowired
    private CycletaskRepository cycletaskRepository;

    @Autowired
    private CycletaskService cycletaskService;

    @Autowired
    private CycletaskQueryService cycletaskQueryService;

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

    private MockMvc restCycletaskMockMvc;

    private Cycletask cycletask;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CycletaskResource cycletaskResource = new CycletaskResource(cycletaskService, cycletaskQueryService);
        this.restCycletaskMockMvc = MockMvcBuilders.standaloneSetup(cycletaskResource)
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
    public static Cycletask createEntity(EntityManager em) {
        Cycletask cycletask = new Cycletask()
            .name(DEFAULT_NAME)
            .cycle(DEFAULT_CYCLE)
            .nextime(DEFAULT_NEXTIME)
            .nextendtime(DEFAULT_NEXTENDTIME)
            .taskid(DEFAULT_TASKID);
        return cycletask;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cycletask createUpdatedEntity(EntityManager em) {
        Cycletask cycletask = new Cycletask()
            .name(UPDATED_NAME)
            .cycle(UPDATED_CYCLE)
            .nextime(UPDATED_NEXTIME)
            .nextendtime(UPDATED_NEXTENDTIME)
            .taskid(UPDATED_TASKID);
        return cycletask;
    }

    @BeforeEach
    public void initTest() {
        cycletask = createEntity(em);
    }

    @Test
    @Transactional
    public void createCycletask() throws Exception {
        int databaseSizeBeforeCreate = cycletaskRepository.findAll().size();

        // Create the Cycletask
        restCycletaskMockMvc.perform(post("/api/cycletasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cycletask)))
            .andExpect(status().isCreated());

        // Validate the Cycletask in the database
        List<Cycletask> cycletaskList = cycletaskRepository.findAll();
        assertThat(cycletaskList).hasSize(databaseSizeBeforeCreate + 1);
        Cycletask testCycletask = cycletaskList.get(cycletaskList.size() - 1);
        assertThat(testCycletask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCycletask.getCycle()).isEqualTo(DEFAULT_CYCLE);
        assertThat(testCycletask.getNextime()).isEqualTo(DEFAULT_NEXTIME);
        assertThat(testCycletask.getNextendtime()).isEqualTo(DEFAULT_NEXTENDTIME);
        assertThat(testCycletask.getTaskid()).isEqualTo(DEFAULT_TASKID);
    }

    @Test
    @Transactional
    public void createCycletaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cycletaskRepository.findAll().size();

        // Create the Cycletask with an existing ID
        cycletask.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCycletaskMockMvc.perform(post("/api/cycletasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cycletask)))
            .andExpect(status().isBadRequest());

        // Validate the Cycletask in the database
        List<Cycletask> cycletaskList = cycletaskRepository.findAll();
        assertThat(cycletaskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCycletasks() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList
        restCycletaskMockMvc.perform(get("/api/cycletasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycletask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE)))
            .andExpect(jsonPath("$.[*].nextime").value(hasItem(DEFAULT_NEXTIME)))
            .andExpect(jsonPath("$.[*].nextendtime").value(hasItem(DEFAULT_NEXTENDTIME)))
            .andExpect(jsonPath("$.[*].taskid").value(hasItem(DEFAULT_TASKID.intValue())));
    }
    
    @Test
    @Transactional
    public void getCycletask() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get the cycletask
        restCycletaskMockMvc.perform(get("/api/cycletasks/{id}", cycletask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cycletask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.cycle").value(DEFAULT_CYCLE))
            .andExpect(jsonPath("$.nextime").value(DEFAULT_NEXTIME))
            .andExpect(jsonPath("$.nextendtime").value(DEFAULT_NEXTENDTIME))
            .andExpect(jsonPath("$.taskid").value(DEFAULT_TASKID.intValue()));
    }


    @Test
    @Transactional
    public void getCycletasksByIdFiltering() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        Long id = cycletask.getId();

        defaultCycletaskShouldBeFound("id.equals=" + id);
        defaultCycletaskShouldNotBeFound("id.notEquals=" + id);

        defaultCycletaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCycletaskShouldNotBeFound("id.greaterThan=" + id);

        defaultCycletaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCycletaskShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCycletasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where name equals to DEFAULT_NAME
        defaultCycletaskShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the cycletaskList where name equals to UPDATED_NAME
        defaultCycletaskShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where name not equals to DEFAULT_NAME
        defaultCycletaskShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the cycletaskList where name not equals to UPDATED_NAME
        defaultCycletaskShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCycletaskShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the cycletaskList where name equals to UPDATED_NAME
        defaultCycletaskShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where name is not null
        defaultCycletaskShouldBeFound("name.specified=true");

        // Get all the cycletaskList where name is null
        defaultCycletaskShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCycletasksByNameContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where name contains DEFAULT_NAME
        defaultCycletaskShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the cycletaskList where name contains UPDATED_NAME
        defaultCycletaskShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where name does not contain DEFAULT_NAME
        defaultCycletaskShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the cycletaskList where name does not contain UPDATED_NAME
        defaultCycletaskShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCycletasksByCycleIsEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where cycle equals to DEFAULT_CYCLE
        defaultCycletaskShouldBeFound("cycle.equals=" + DEFAULT_CYCLE);

        // Get all the cycletaskList where cycle equals to UPDATED_CYCLE
        defaultCycletaskShouldNotBeFound("cycle.equals=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllCycletasksByCycleIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where cycle not equals to DEFAULT_CYCLE
        defaultCycletaskShouldNotBeFound("cycle.notEquals=" + DEFAULT_CYCLE);

        // Get all the cycletaskList where cycle not equals to UPDATED_CYCLE
        defaultCycletaskShouldBeFound("cycle.notEquals=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllCycletasksByCycleIsInShouldWork() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where cycle in DEFAULT_CYCLE or UPDATED_CYCLE
        defaultCycletaskShouldBeFound("cycle.in=" + DEFAULT_CYCLE + "," + UPDATED_CYCLE);

        // Get all the cycletaskList where cycle equals to UPDATED_CYCLE
        defaultCycletaskShouldNotBeFound("cycle.in=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllCycletasksByCycleIsNullOrNotNull() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where cycle is not null
        defaultCycletaskShouldBeFound("cycle.specified=true");

        // Get all the cycletaskList where cycle is null
        defaultCycletaskShouldNotBeFound("cycle.specified=false");
    }
                @Test
    @Transactional
    public void getAllCycletasksByCycleContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where cycle contains DEFAULT_CYCLE
        defaultCycletaskShouldBeFound("cycle.contains=" + DEFAULT_CYCLE);

        // Get all the cycletaskList where cycle contains UPDATED_CYCLE
        defaultCycletaskShouldNotBeFound("cycle.contains=" + UPDATED_CYCLE);
    }

    @Test
    @Transactional
    public void getAllCycletasksByCycleNotContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where cycle does not contain DEFAULT_CYCLE
        defaultCycletaskShouldNotBeFound("cycle.doesNotContain=" + DEFAULT_CYCLE);

        // Get all the cycletaskList where cycle does not contain UPDATED_CYCLE
        defaultCycletaskShouldBeFound("cycle.doesNotContain=" + UPDATED_CYCLE);
    }


    @Test
    @Transactional
    public void getAllCycletasksByNextimeIsEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextime equals to DEFAULT_NEXTIME
        defaultCycletaskShouldBeFound("nextime.equals=" + DEFAULT_NEXTIME);

        // Get all the cycletaskList where nextime equals to UPDATED_NEXTIME
        defaultCycletaskShouldNotBeFound("nextime.equals=" + UPDATED_NEXTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextime not equals to DEFAULT_NEXTIME
        defaultCycletaskShouldNotBeFound("nextime.notEquals=" + DEFAULT_NEXTIME);

        // Get all the cycletaskList where nextime not equals to UPDATED_NEXTIME
        defaultCycletaskShouldBeFound("nextime.notEquals=" + UPDATED_NEXTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextimeIsInShouldWork() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextime in DEFAULT_NEXTIME or UPDATED_NEXTIME
        defaultCycletaskShouldBeFound("nextime.in=" + DEFAULT_NEXTIME + "," + UPDATED_NEXTIME);

        // Get all the cycletaskList where nextime equals to UPDATED_NEXTIME
        defaultCycletaskShouldNotBeFound("nextime.in=" + UPDATED_NEXTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextime is not null
        defaultCycletaskShouldBeFound("nextime.specified=true");

        // Get all the cycletaskList where nextime is null
        defaultCycletaskShouldNotBeFound("nextime.specified=false");
    }
                @Test
    @Transactional
    public void getAllCycletasksByNextimeContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextime contains DEFAULT_NEXTIME
        defaultCycletaskShouldBeFound("nextime.contains=" + DEFAULT_NEXTIME);

        // Get all the cycletaskList where nextime contains UPDATED_NEXTIME
        defaultCycletaskShouldNotBeFound("nextime.contains=" + UPDATED_NEXTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextimeNotContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextime does not contain DEFAULT_NEXTIME
        defaultCycletaskShouldNotBeFound("nextime.doesNotContain=" + DEFAULT_NEXTIME);

        // Get all the cycletaskList where nextime does not contain UPDATED_NEXTIME
        defaultCycletaskShouldBeFound("nextime.doesNotContain=" + UPDATED_NEXTIME);
    }


    @Test
    @Transactional
    public void getAllCycletasksByNextendtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextendtime equals to DEFAULT_NEXTENDTIME
        defaultCycletaskShouldBeFound("nextendtime.equals=" + DEFAULT_NEXTENDTIME);

        // Get all the cycletaskList where nextendtime equals to UPDATED_NEXTENDTIME
        defaultCycletaskShouldNotBeFound("nextendtime.equals=" + UPDATED_NEXTENDTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextendtimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextendtime not equals to DEFAULT_NEXTENDTIME
        defaultCycletaskShouldNotBeFound("nextendtime.notEquals=" + DEFAULT_NEXTENDTIME);

        // Get all the cycletaskList where nextendtime not equals to UPDATED_NEXTENDTIME
        defaultCycletaskShouldBeFound("nextendtime.notEquals=" + UPDATED_NEXTENDTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextendtimeIsInShouldWork() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextendtime in DEFAULT_NEXTENDTIME or UPDATED_NEXTENDTIME
        defaultCycletaskShouldBeFound("nextendtime.in=" + DEFAULT_NEXTENDTIME + "," + UPDATED_NEXTENDTIME);

        // Get all the cycletaskList where nextendtime equals to UPDATED_NEXTENDTIME
        defaultCycletaskShouldNotBeFound("nextendtime.in=" + UPDATED_NEXTENDTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextendtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextendtime is not null
        defaultCycletaskShouldBeFound("nextendtime.specified=true");

        // Get all the cycletaskList where nextendtime is null
        defaultCycletaskShouldNotBeFound("nextendtime.specified=false");
    }
                @Test
    @Transactional
    public void getAllCycletasksByNextendtimeContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextendtime contains DEFAULT_NEXTENDTIME
        defaultCycletaskShouldBeFound("nextendtime.contains=" + DEFAULT_NEXTENDTIME);

        // Get all the cycletaskList where nextendtime contains UPDATED_NEXTENDTIME
        defaultCycletaskShouldNotBeFound("nextendtime.contains=" + UPDATED_NEXTENDTIME);
    }

    @Test
    @Transactional
    public void getAllCycletasksByNextendtimeNotContainsSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where nextendtime does not contain DEFAULT_NEXTENDTIME
        defaultCycletaskShouldNotBeFound("nextendtime.doesNotContain=" + DEFAULT_NEXTENDTIME);

        // Get all the cycletaskList where nextendtime does not contain UPDATED_NEXTENDTIME
        defaultCycletaskShouldBeFound("nextendtime.doesNotContain=" + UPDATED_NEXTENDTIME);
    }


    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid equals to DEFAULT_TASKID
        defaultCycletaskShouldBeFound("taskid.equals=" + DEFAULT_TASKID);

        // Get all the cycletaskList where taskid equals to UPDATED_TASKID
        defaultCycletaskShouldNotBeFound("taskid.equals=" + UPDATED_TASKID);
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsNotEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid not equals to DEFAULT_TASKID
        defaultCycletaskShouldNotBeFound("taskid.notEquals=" + DEFAULT_TASKID);

        // Get all the cycletaskList where taskid not equals to UPDATED_TASKID
        defaultCycletaskShouldBeFound("taskid.notEquals=" + UPDATED_TASKID);
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsInShouldWork() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid in DEFAULT_TASKID or UPDATED_TASKID
        defaultCycletaskShouldBeFound("taskid.in=" + DEFAULT_TASKID + "," + UPDATED_TASKID);

        // Get all the cycletaskList where taskid equals to UPDATED_TASKID
        defaultCycletaskShouldNotBeFound("taskid.in=" + UPDATED_TASKID);
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsNullOrNotNull() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid is not null
        defaultCycletaskShouldBeFound("taskid.specified=true");

        // Get all the cycletaskList where taskid is null
        defaultCycletaskShouldNotBeFound("taskid.specified=false");
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid is greater than or equal to DEFAULT_TASKID
        defaultCycletaskShouldBeFound("taskid.greaterThanOrEqual=" + DEFAULT_TASKID);

        // Get all the cycletaskList where taskid is greater than or equal to UPDATED_TASKID
        defaultCycletaskShouldNotBeFound("taskid.greaterThanOrEqual=" + UPDATED_TASKID);
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid is less than or equal to DEFAULT_TASKID
        defaultCycletaskShouldBeFound("taskid.lessThanOrEqual=" + DEFAULT_TASKID);

        // Get all the cycletaskList where taskid is less than or equal to SMALLER_TASKID
        defaultCycletaskShouldNotBeFound("taskid.lessThanOrEqual=" + SMALLER_TASKID);
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsLessThanSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid is less than DEFAULT_TASKID
        defaultCycletaskShouldNotBeFound("taskid.lessThan=" + DEFAULT_TASKID);

        // Get all the cycletaskList where taskid is less than UPDATED_TASKID
        defaultCycletaskShouldBeFound("taskid.lessThan=" + UPDATED_TASKID);
    }

    @Test
    @Transactional
    public void getAllCycletasksByTaskidIsGreaterThanSomething() throws Exception {
        // Initialize the database
        cycletaskRepository.saveAndFlush(cycletask);

        // Get all the cycletaskList where taskid is greater than DEFAULT_TASKID
        defaultCycletaskShouldNotBeFound("taskid.greaterThan=" + DEFAULT_TASKID);

        // Get all the cycletaskList where taskid is greater than SMALLER_TASKID
        defaultCycletaskShouldBeFound("taskid.greaterThan=" + SMALLER_TASKID);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCycletaskShouldBeFound(String filter) throws Exception {
        restCycletaskMockMvc.perform(get("/api/cycletasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cycletask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].cycle").value(hasItem(DEFAULT_CYCLE)))
            .andExpect(jsonPath("$.[*].nextime").value(hasItem(DEFAULT_NEXTIME)))
            .andExpect(jsonPath("$.[*].nextendtime").value(hasItem(DEFAULT_NEXTENDTIME)))
            .andExpect(jsonPath("$.[*].taskid").value(hasItem(DEFAULT_TASKID.intValue())));

        // Check, that the count call also returns 1
        restCycletaskMockMvc.perform(get("/api/cycletasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCycletaskShouldNotBeFound(String filter) throws Exception {
        restCycletaskMockMvc.perform(get("/api/cycletasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCycletaskMockMvc.perform(get("/api/cycletasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCycletask() throws Exception {
        // Get the cycletask
        restCycletaskMockMvc.perform(get("/api/cycletasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCycletask() throws Exception {
        // Initialize the database
        cycletaskService.save(cycletask);

        int databaseSizeBeforeUpdate = cycletaskRepository.findAll().size();

        // Update the cycletask
        Cycletask updatedCycletask = cycletaskRepository.findById(cycletask.getId()).get();
        // Disconnect from session so that the updates on updatedCycletask are not directly saved in db
        em.detach(updatedCycletask);
        updatedCycletask
            .name(UPDATED_NAME)
            .cycle(UPDATED_CYCLE)
            .nextime(UPDATED_NEXTIME)
            .nextendtime(UPDATED_NEXTENDTIME)
            .taskid(UPDATED_TASKID);

        restCycletaskMockMvc.perform(put("/api/cycletasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedCycletask)))
            .andExpect(status().isOk());

        // Validate the Cycletask in the database
        List<Cycletask> cycletaskList = cycletaskRepository.findAll();
        assertThat(cycletaskList).hasSize(databaseSizeBeforeUpdate);
        Cycletask testCycletask = cycletaskList.get(cycletaskList.size() - 1);
        assertThat(testCycletask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCycletask.getCycle()).isEqualTo(UPDATED_CYCLE);
        assertThat(testCycletask.getNextime()).isEqualTo(UPDATED_NEXTIME);
        assertThat(testCycletask.getNextendtime()).isEqualTo(UPDATED_NEXTENDTIME);
        assertThat(testCycletask.getTaskid()).isEqualTo(UPDATED_TASKID);
    }

    @Test
    @Transactional
    public void updateNonExistingCycletask() throws Exception {
        int databaseSizeBeforeUpdate = cycletaskRepository.findAll().size();

        // Create the Cycletask

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCycletaskMockMvc.perform(put("/api/cycletasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cycletask)))
            .andExpect(status().isBadRequest());

        // Validate the Cycletask in the database
        List<Cycletask> cycletaskList = cycletaskRepository.findAll();
        assertThat(cycletaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCycletask() throws Exception {
        // Initialize the database
        cycletaskService.save(cycletask);

        int databaseSizeBeforeDelete = cycletaskRepository.findAll().size();

        // Delete the cycletask
        restCycletaskMockMvc.perform(delete("/api/cycletasks/{id}", cycletask.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cycletask> cycletaskList = cycletaskRepository.findAll();
        assertThat(cycletaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
