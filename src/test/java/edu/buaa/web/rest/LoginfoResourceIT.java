package edu.buaa.web.rest;

import edu.buaa.GdataApp;
import edu.buaa.domain.Loginfo;
import edu.buaa.repository.LoginfoRepository;
import edu.buaa.service.LoginfoService;
import edu.buaa.web.rest.errors.ExceptionTranslator;
import edu.buaa.service.dto.LoginfoCriteria;
import edu.buaa.service.LoginfoQueryService;

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
 * Integration tests for the {@link LoginfoResource} REST controller.
 */
@SpringBootTest(classes = GdataApp.class)
public class LoginfoResourceIT {

    private static final String DEFAULT_IP = "AAAAAAAAAA";
    private static final String UPDATED_IP = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_X = 1D;
    private static final Double UPDATED_X = 2D;
    private static final Double SMALLER_X = 1D - 1D;

    private static final Double DEFAULT_Y = 1D;
    private static final Double UPDATED_Y = 2D;
    private static final Double SMALLER_Y = 1D - 1D;

    private static final String DEFAULT_EVENTIME = "AAAAAAAAAA";
    private static final String UPDATED_EVENTIME = "BBBBBBBBBB";

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final String DEFAULT_OWNER = "AAAAAAAAAA";
    private static final String UPDATED_OWNER = "BBBBBBBBBB";

    private static final String DEFAULT_LEVEL = "AAAAAAAAAA";
    private static final String UPDATED_LEVEL = "BBBBBBBBBB";

    @Autowired
    private LoginfoRepository loginfoRepository;

    @Autowired
    private LoginfoService loginfoService;

    @Autowired
    private LoginfoQueryService loginfoQueryService;

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

    private MockMvc restLoginfoMockMvc;

    private Loginfo loginfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final LoginfoResource loginfoResource = new LoginfoResource(loginfoService, loginfoQueryService,loginfoRepository);
        this.restLoginfoMockMvc = MockMvcBuilders.standaloneSetup(loginfoResource)
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
    public static Loginfo createEntity(EntityManager em) {
        Loginfo loginfo = new Loginfo()
            .ip(DEFAULT_IP)
            .type(DEFAULT_TYPE)
            .name(DEFAULT_NAME)
            .x(DEFAULT_X)
            .y(DEFAULT_Y)
            .eventime(DEFAULT_EVENTIME)
            .note(DEFAULT_NOTE)
            .owner(DEFAULT_OWNER)
            .level(DEFAULT_LEVEL);
        return loginfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Loginfo createUpdatedEntity(EntityManager em) {
        Loginfo loginfo = new Loginfo()
            .ip(UPDATED_IP)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .eventime(UPDATED_EVENTIME)
            .note(UPDATED_NOTE)
            .owner(UPDATED_OWNER)
            .level(UPDATED_LEVEL);
        return loginfo;
    }

    @BeforeEach
    public void initTest() {
        loginfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createLoginfo() throws Exception {
        int databaseSizeBeforeCreate = loginfoRepository.findAll().size();

        // Create the Loginfo
        restLoginfoMockMvc.perform(post("/api/loginfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginfo)))
            .andExpect(status().isCreated());

        // Validate the Loginfo in the database
        List<Loginfo> loginfoList = loginfoRepository.findAll();
        assertThat(loginfoList).hasSize(databaseSizeBeforeCreate + 1);
        Loginfo testLoginfo = loginfoList.get(loginfoList.size() - 1);
        assertThat(testLoginfo.getIp()).isEqualTo(DEFAULT_IP);
        assertThat(testLoginfo.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testLoginfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testLoginfo.getX()).isEqualTo(DEFAULT_X);
        assertThat(testLoginfo.getY()).isEqualTo(DEFAULT_Y);
        assertThat(testLoginfo.getEventime()).isEqualTo(DEFAULT_EVENTIME);
        assertThat(testLoginfo.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testLoginfo.getOwner()).isEqualTo(DEFAULT_OWNER);
        assertThat(testLoginfo.getLevel()).isEqualTo(DEFAULT_LEVEL);
    }

    @Test
    @Transactional
    public void createLoginfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = loginfoRepository.findAll().size();

        // Create the Loginfo with an existing ID
        loginfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restLoginfoMockMvc.perform(post("/api/loginfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginfo)))
            .andExpect(status().isBadRequest());

        // Validate the Loginfo in the database
        List<Loginfo> loginfoList = loginfoRepository.findAll();
        assertThat(loginfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllLoginfos() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList
        restLoginfoMockMvc.perform(get("/api/loginfos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].eventime").value(hasItem(DEFAULT_EVENTIME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));
    }
    
    @Test
    @Transactional
    public void getLoginfo() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get the loginfo
        restLoginfoMockMvc.perform(get("/api/loginfos/{id}", loginfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(loginfo.getId().intValue()))
            .andExpect(jsonPath("$.ip").value(DEFAULT_IP))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.x").value(DEFAULT_X.doubleValue()))
            .andExpect(jsonPath("$.y").value(DEFAULT_Y.doubleValue()))
            .andExpect(jsonPath("$.eventime").value(DEFAULT_EVENTIME))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.owner").value(DEFAULT_OWNER))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL));
    }


    @Test
    @Transactional
    public void getLoginfosByIdFiltering() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        Long id = loginfo.getId();

        defaultLoginfoShouldBeFound("id.equals=" + id);
        defaultLoginfoShouldNotBeFound("id.notEquals=" + id);

        defaultLoginfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultLoginfoShouldNotBeFound("id.greaterThan=" + id);

        defaultLoginfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultLoginfoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllLoginfosByIpIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where ip equals to DEFAULT_IP
        defaultLoginfoShouldBeFound("ip.equals=" + DEFAULT_IP);

        // Get all the loginfoList where ip equals to UPDATED_IP
        defaultLoginfoShouldNotBeFound("ip.equals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllLoginfosByIpIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where ip not equals to DEFAULT_IP
        defaultLoginfoShouldNotBeFound("ip.notEquals=" + DEFAULT_IP);

        // Get all the loginfoList where ip not equals to UPDATED_IP
        defaultLoginfoShouldBeFound("ip.notEquals=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllLoginfosByIpIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where ip in DEFAULT_IP or UPDATED_IP
        defaultLoginfoShouldBeFound("ip.in=" + DEFAULT_IP + "," + UPDATED_IP);

        // Get all the loginfoList where ip equals to UPDATED_IP
        defaultLoginfoShouldNotBeFound("ip.in=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllLoginfosByIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where ip is not null
        defaultLoginfoShouldBeFound("ip.specified=true");

        // Get all the loginfoList where ip is null
        defaultLoginfoShouldNotBeFound("ip.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByIpContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where ip contains DEFAULT_IP
        defaultLoginfoShouldBeFound("ip.contains=" + DEFAULT_IP);

        // Get all the loginfoList where ip contains UPDATED_IP
        defaultLoginfoShouldNotBeFound("ip.contains=" + UPDATED_IP);
    }

    @Test
    @Transactional
    public void getAllLoginfosByIpNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where ip does not contain DEFAULT_IP
        defaultLoginfoShouldNotBeFound("ip.doesNotContain=" + DEFAULT_IP);

        // Get all the loginfoList where ip does not contain UPDATED_IP
        defaultLoginfoShouldBeFound("ip.doesNotContain=" + UPDATED_IP);
    }


    @Test
    @Transactional
    public void getAllLoginfosByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where type equals to DEFAULT_TYPE
        defaultLoginfoShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the loginfoList where type equals to UPDATED_TYPE
        defaultLoginfoShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where type not equals to DEFAULT_TYPE
        defaultLoginfoShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the loginfoList where type not equals to UPDATED_TYPE
        defaultLoginfoShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultLoginfoShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the loginfoList where type equals to UPDATED_TYPE
        defaultLoginfoShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where type is not null
        defaultLoginfoShouldBeFound("type.specified=true");

        // Get all the loginfoList where type is null
        defaultLoginfoShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByTypeContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where type contains DEFAULT_TYPE
        defaultLoginfoShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the loginfoList where type contains UPDATED_TYPE
        defaultLoginfoShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where type does not contain DEFAULT_TYPE
        defaultLoginfoShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the loginfoList where type does not contain UPDATED_TYPE
        defaultLoginfoShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllLoginfosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where name equals to DEFAULT_NAME
        defaultLoginfoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the loginfoList where name equals to UPDATED_NAME
        defaultLoginfoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where name not equals to DEFAULT_NAME
        defaultLoginfoShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the loginfoList where name not equals to UPDATED_NAME
        defaultLoginfoShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultLoginfoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the loginfoList where name equals to UPDATED_NAME
        defaultLoginfoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where name is not null
        defaultLoginfoShouldBeFound("name.specified=true");

        // Get all the loginfoList where name is null
        defaultLoginfoShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByNameContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where name contains DEFAULT_NAME
        defaultLoginfoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the loginfoList where name contains UPDATED_NAME
        defaultLoginfoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where name does not contain DEFAULT_NAME
        defaultLoginfoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the loginfoList where name does not contain UPDATED_NAME
        defaultLoginfoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllLoginfosByXIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x equals to DEFAULT_X
        defaultLoginfoShouldBeFound("x.equals=" + DEFAULT_X);

        // Get all the loginfoList where x equals to UPDATED_X
        defaultLoginfoShouldNotBeFound("x.equals=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x not equals to DEFAULT_X
        defaultLoginfoShouldNotBeFound("x.notEquals=" + DEFAULT_X);

        // Get all the loginfoList where x not equals to UPDATED_X
        defaultLoginfoShouldBeFound("x.notEquals=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x in DEFAULT_X or UPDATED_X
        defaultLoginfoShouldBeFound("x.in=" + DEFAULT_X + "," + UPDATED_X);

        // Get all the loginfoList where x equals to UPDATED_X
        defaultLoginfoShouldNotBeFound("x.in=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x is not null
        defaultLoginfoShouldBeFound("x.specified=true");

        // Get all the loginfoList where x is null
        defaultLoginfoShouldNotBeFound("x.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x is greater than or equal to DEFAULT_X
        defaultLoginfoShouldBeFound("x.greaterThanOrEqual=" + DEFAULT_X);

        // Get all the loginfoList where x is greater than or equal to UPDATED_X
        defaultLoginfoShouldNotBeFound("x.greaterThanOrEqual=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x is less than or equal to DEFAULT_X
        defaultLoginfoShouldBeFound("x.lessThanOrEqual=" + DEFAULT_X);

        // Get all the loginfoList where x is less than or equal to SMALLER_X
        defaultLoginfoShouldNotBeFound("x.lessThanOrEqual=" + SMALLER_X);
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsLessThanSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x is less than DEFAULT_X
        defaultLoginfoShouldNotBeFound("x.lessThan=" + DEFAULT_X);

        // Get all the loginfoList where x is less than UPDATED_X
        defaultLoginfoShouldBeFound("x.lessThan=" + UPDATED_X);
    }

    @Test
    @Transactional
    public void getAllLoginfosByXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where x is greater than DEFAULT_X
        defaultLoginfoShouldNotBeFound("x.greaterThan=" + DEFAULT_X);

        // Get all the loginfoList where x is greater than SMALLER_X
        defaultLoginfoShouldBeFound("x.greaterThan=" + SMALLER_X);
    }


    @Test
    @Transactional
    public void getAllLoginfosByYIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y equals to DEFAULT_Y
        defaultLoginfoShouldBeFound("y.equals=" + DEFAULT_Y);

        // Get all the loginfoList where y equals to UPDATED_Y
        defaultLoginfoShouldNotBeFound("y.equals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y not equals to DEFAULT_Y
        defaultLoginfoShouldNotBeFound("y.notEquals=" + DEFAULT_Y);

        // Get all the loginfoList where y not equals to UPDATED_Y
        defaultLoginfoShouldBeFound("y.notEquals=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y in DEFAULT_Y or UPDATED_Y
        defaultLoginfoShouldBeFound("y.in=" + DEFAULT_Y + "," + UPDATED_Y);

        // Get all the loginfoList where y equals to UPDATED_Y
        defaultLoginfoShouldNotBeFound("y.in=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y is not null
        defaultLoginfoShouldBeFound("y.specified=true");

        // Get all the loginfoList where y is null
        defaultLoginfoShouldNotBeFound("y.specified=false");
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y is greater than or equal to DEFAULT_Y
        defaultLoginfoShouldBeFound("y.greaterThanOrEqual=" + DEFAULT_Y);

        // Get all the loginfoList where y is greater than or equal to UPDATED_Y
        defaultLoginfoShouldNotBeFound("y.greaterThanOrEqual=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y is less than or equal to DEFAULT_Y
        defaultLoginfoShouldBeFound("y.lessThanOrEqual=" + DEFAULT_Y);

        // Get all the loginfoList where y is less than or equal to SMALLER_Y
        defaultLoginfoShouldNotBeFound("y.lessThanOrEqual=" + SMALLER_Y);
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsLessThanSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y is less than DEFAULT_Y
        defaultLoginfoShouldNotBeFound("y.lessThan=" + DEFAULT_Y);

        // Get all the loginfoList where y is less than UPDATED_Y
        defaultLoginfoShouldBeFound("y.lessThan=" + UPDATED_Y);
    }

    @Test
    @Transactional
    public void getAllLoginfosByYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where y is greater than DEFAULT_Y
        defaultLoginfoShouldNotBeFound("y.greaterThan=" + DEFAULT_Y);

        // Get all the loginfoList where y is greater than SMALLER_Y
        defaultLoginfoShouldBeFound("y.greaterThan=" + SMALLER_Y);
    }


    @Test
    @Transactional
    public void getAllLoginfosByEventimeIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where eventime equals to DEFAULT_EVENTIME
        defaultLoginfoShouldBeFound("eventime.equals=" + DEFAULT_EVENTIME);

        // Get all the loginfoList where eventime equals to UPDATED_EVENTIME
        defaultLoginfoShouldNotBeFound("eventime.equals=" + UPDATED_EVENTIME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByEventimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where eventime not equals to DEFAULT_EVENTIME
        defaultLoginfoShouldNotBeFound("eventime.notEquals=" + DEFAULT_EVENTIME);

        // Get all the loginfoList where eventime not equals to UPDATED_EVENTIME
        defaultLoginfoShouldBeFound("eventime.notEquals=" + UPDATED_EVENTIME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByEventimeIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where eventime in DEFAULT_EVENTIME or UPDATED_EVENTIME
        defaultLoginfoShouldBeFound("eventime.in=" + DEFAULT_EVENTIME + "," + UPDATED_EVENTIME);

        // Get all the loginfoList where eventime equals to UPDATED_EVENTIME
        defaultLoginfoShouldNotBeFound("eventime.in=" + UPDATED_EVENTIME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByEventimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where eventime is not null
        defaultLoginfoShouldBeFound("eventime.specified=true");

        // Get all the loginfoList where eventime is null
        defaultLoginfoShouldNotBeFound("eventime.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByEventimeContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where eventime contains DEFAULT_EVENTIME
        defaultLoginfoShouldBeFound("eventime.contains=" + DEFAULT_EVENTIME);

        // Get all the loginfoList where eventime contains UPDATED_EVENTIME
        defaultLoginfoShouldNotBeFound("eventime.contains=" + UPDATED_EVENTIME);
    }

    @Test
    @Transactional
    public void getAllLoginfosByEventimeNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where eventime does not contain DEFAULT_EVENTIME
        defaultLoginfoShouldNotBeFound("eventime.doesNotContain=" + DEFAULT_EVENTIME);

        // Get all the loginfoList where eventime does not contain UPDATED_EVENTIME
        defaultLoginfoShouldBeFound("eventime.doesNotContain=" + UPDATED_EVENTIME);
    }


    @Test
    @Transactional
    public void getAllLoginfosByNoteIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where note equals to DEFAULT_NOTE
        defaultLoginfoShouldBeFound("note.equals=" + DEFAULT_NOTE);

        // Get all the loginfoList where note equals to UPDATED_NOTE
        defaultLoginfoShouldNotBeFound("note.equals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNoteIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where note not equals to DEFAULT_NOTE
        defaultLoginfoShouldNotBeFound("note.notEquals=" + DEFAULT_NOTE);

        // Get all the loginfoList where note not equals to UPDATED_NOTE
        defaultLoginfoShouldBeFound("note.notEquals=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNoteIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where note in DEFAULT_NOTE or UPDATED_NOTE
        defaultLoginfoShouldBeFound("note.in=" + DEFAULT_NOTE + "," + UPDATED_NOTE);

        // Get all the loginfoList where note equals to UPDATED_NOTE
        defaultLoginfoShouldNotBeFound("note.in=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNoteIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where note is not null
        defaultLoginfoShouldBeFound("note.specified=true");

        // Get all the loginfoList where note is null
        defaultLoginfoShouldNotBeFound("note.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByNoteContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where note contains DEFAULT_NOTE
        defaultLoginfoShouldBeFound("note.contains=" + DEFAULT_NOTE);

        // Get all the loginfoList where note contains UPDATED_NOTE
        defaultLoginfoShouldNotBeFound("note.contains=" + UPDATED_NOTE);
    }

    @Test
    @Transactional
    public void getAllLoginfosByNoteNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where note does not contain DEFAULT_NOTE
        defaultLoginfoShouldNotBeFound("note.doesNotContain=" + DEFAULT_NOTE);

        // Get all the loginfoList where note does not contain UPDATED_NOTE
        defaultLoginfoShouldBeFound("note.doesNotContain=" + UPDATED_NOTE);
    }


    @Test
    @Transactional
    public void getAllLoginfosByOwnerIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where owner equals to DEFAULT_OWNER
        defaultLoginfoShouldBeFound("owner.equals=" + DEFAULT_OWNER);

        // Get all the loginfoList where owner equals to UPDATED_OWNER
        defaultLoginfoShouldNotBeFound("owner.equals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllLoginfosByOwnerIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where owner not equals to DEFAULT_OWNER
        defaultLoginfoShouldNotBeFound("owner.notEquals=" + DEFAULT_OWNER);

        // Get all the loginfoList where owner not equals to UPDATED_OWNER
        defaultLoginfoShouldBeFound("owner.notEquals=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllLoginfosByOwnerIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where owner in DEFAULT_OWNER or UPDATED_OWNER
        defaultLoginfoShouldBeFound("owner.in=" + DEFAULT_OWNER + "," + UPDATED_OWNER);

        // Get all the loginfoList where owner equals to UPDATED_OWNER
        defaultLoginfoShouldNotBeFound("owner.in=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllLoginfosByOwnerIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where owner is not null
        defaultLoginfoShouldBeFound("owner.specified=true");

        // Get all the loginfoList where owner is null
        defaultLoginfoShouldNotBeFound("owner.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByOwnerContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where owner contains DEFAULT_OWNER
        defaultLoginfoShouldBeFound("owner.contains=" + DEFAULT_OWNER);

        // Get all the loginfoList where owner contains UPDATED_OWNER
        defaultLoginfoShouldNotBeFound("owner.contains=" + UPDATED_OWNER);
    }

    @Test
    @Transactional
    public void getAllLoginfosByOwnerNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where owner does not contain DEFAULT_OWNER
        defaultLoginfoShouldNotBeFound("owner.doesNotContain=" + DEFAULT_OWNER);

        // Get all the loginfoList where owner does not contain UPDATED_OWNER
        defaultLoginfoShouldBeFound("owner.doesNotContain=" + UPDATED_OWNER);
    }


    @Test
    @Transactional
    public void getAllLoginfosByLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where level equals to DEFAULT_LEVEL
        defaultLoginfoShouldBeFound("level.equals=" + DEFAULT_LEVEL);

        // Get all the loginfoList where level equals to UPDATED_LEVEL
        defaultLoginfoShouldNotBeFound("level.equals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllLoginfosByLevelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where level not equals to DEFAULT_LEVEL
        defaultLoginfoShouldNotBeFound("level.notEquals=" + DEFAULT_LEVEL);

        // Get all the loginfoList where level not equals to UPDATED_LEVEL
        defaultLoginfoShouldBeFound("level.notEquals=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllLoginfosByLevelIsInShouldWork() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where level in DEFAULT_LEVEL or UPDATED_LEVEL
        defaultLoginfoShouldBeFound("level.in=" + DEFAULT_LEVEL + "," + UPDATED_LEVEL);

        // Get all the loginfoList where level equals to UPDATED_LEVEL
        defaultLoginfoShouldNotBeFound("level.in=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllLoginfosByLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where level is not null
        defaultLoginfoShouldBeFound("level.specified=true");

        // Get all the loginfoList where level is null
        defaultLoginfoShouldNotBeFound("level.specified=false");
    }
                @Test
    @Transactional
    public void getAllLoginfosByLevelContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where level contains DEFAULT_LEVEL
        defaultLoginfoShouldBeFound("level.contains=" + DEFAULT_LEVEL);

        // Get all the loginfoList where level contains UPDATED_LEVEL
        defaultLoginfoShouldNotBeFound("level.contains=" + UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void getAllLoginfosByLevelNotContainsSomething() throws Exception {
        // Initialize the database
        loginfoRepository.saveAndFlush(loginfo);

        // Get all the loginfoList where level does not contain DEFAULT_LEVEL
        defaultLoginfoShouldNotBeFound("level.doesNotContain=" + DEFAULT_LEVEL);

        // Get all the loginfoList where level does not contain UPDATED_LEVEL
        defaultLoginfoShouldBeFound("level.doesNotContain=" + UPDATED_LEVEL);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLoginfoShouldBeFound(String filter) throws Exception {
        restLoginfoMockMvc.perform(get("/api/loginfos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(loginfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].ip").value(hasItem(DEFAULT_IP)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].x").value(hasItem(DEFAULT_X.doubleValue())))
            .andExpect(jsonPath("$.[*].y").value(hasItem(DEFAULT_Y.doubleValue())))
            .andExpect(jsonPath("$.[*].eventime").value(hasItem(DEFAULT_EVENTIME)))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].owner").value(hasItem(DEFAULT_OWNER)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)));

        // Check, that the count call also returns 1
        restLoginfoMockMvc.perform(get("/api/loginfos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLoginfoShouldNotBeFound(String filter) throws Exception {
        restLoginfoMockMvc.perform(get("/api/loginfos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLoginfoMockMvc.perform(get("/api/loginfos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingLoginfo() throws Exception {
        // Get the loginfo
        restLoginfoMockMvc.perform(get("/api/loginfos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateLoginfo() throws Exception {
        // Initialize the database
        loginfoService.save(loginfo);

        int databaseSizeBeforeUpdate = loginfoRepository.findAll().size();

        // Update the loginfo
        Loginfo updatedLoginfo = loginfoRepository.findById(loginfo.getId()).get();
        // Disconnect from session so that the updates on updatedLoginfo are not directly saved in db
        em.detach(updatedLoginfo);
        updatedLoginfo
            .ip(UPDATED_IP)
            .type(UPDATED_TYPE)
            .name(UPDATED_NAME)
            .x(UPDATED_X)
            .y(UPDATED_Y)
            .eventime(UPDATED_EVENTIME)
            .note(UPDATED_NOTE)
            .owner(UPDATED_OWNER)
            .level(UPDATED_LEVEL);

        restLoginfoMockMvc.perform(put("/api/loginfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedLoginfo)))
            .andExpect(status().isOk());

        // Validate the Loginfo in the database
        List<Loginfo> loginfoList = loginfoRepository.findAll();
        assertThat(loginfoList).hasSize(databaseSizeBeforeUpdate);
        Loginfo testLoginfo = loginfoList.get(loginfoList.size() - 1);
        assertThat(testLoginfo.getIp()).isEqualTo(UPDATED_IP);
        assertThat(testLoginfo.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testLoginfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testLoginfo.getX()).isEqualTo(UPDATED_X);
        assertThat(testLoginfo.getY()).isEqualTo(UPDATED_Y);
        assertThat(testLoginfo.getEventime()).isEqualTo(UPDATED_EVENTIME);
        assertThat(testLoginfo.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testLoginfo.getOwner()).isEqualTo(UPDATED_OWNER);
        assertThat(testLoginfo.getLevel()).isEqualTo(UPDATED_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingLoginfo() throws Exception {
        int databaseSizeBeforeUpdate = loginfoRepository.findAll().size();

        // Create the Loginfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLoginfoMockMvc.perform(put("/api/loginfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(loginfo)))
            .andExpect(status().isBadRequest());

        // Validate the Loginfo in the database
        List<Loginfo> loginfoList = loginfoRepository.findAll();
        assertThat(loginfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteLoginfo() throws Exception {
        // Initialize the database
        loginfoService.save(loginfo);

        int databaseSizeBeforeDelete = loginfoRepository.findAll().size();

        // Delete the loginfo
        restLoginfoMockMvc.perform(delete("/api/loginfos/{id}", loginfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Loginfo> loginfoList = loginfoRepository.findAll();
        assertThat(loginfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
