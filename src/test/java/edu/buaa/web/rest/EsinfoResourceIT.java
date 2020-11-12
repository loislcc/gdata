package edu.buaa.web.rest;

import edu.buaa.GdataApp;
import edu.buaa.domain.Esinfo;
import edu.buaa.repository.EsinfoRepository;
import edu.buaa.service.EsinfoService;
import edu.buaa.service.LoginfoService;
import edu.buaa.service.TaskService;
import edu.buaa.service.message.ToConsoleProducer;
import edu.buaa.web.rest.errors.ExceptionTranslator;
import edu.buaa.service.dto.EsinfoCriteria;
import edu.buaa.service.EsinfoQueryService;

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
 * Integration tests for the {@link EsinfoResource} REST controller.
 */
@SpringBootTest(classes = GdataApp.class)
public class EsinfoResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_VNODE = "AAAAAAAAAA";
    private static final String UPDATED_VNODE = "BBBBBBBBBB";

    private static final String DEFAULT_RNODE = "AAAAAAAAAA";
    private static final String UPDATED_RNODE = "BBBBBBBBBB";

    private static final String DEFAULT_DATE = "AAAAAAAAAA";
    private static final String UPDATED_DATE = "BBBBBBBBBB";

    private static final String DEFAULT_PNAME = "AAAAAAAAAA";
    private static final String UPDATED_PNAME = "BBBBBBBBBB";

    @Autowired
    private EsinfoRepository esinfoRepository;

    @Autowired
    private EsinfoService esinfoService;

    @Autowired
    private EsinfoQueryService esinfoQueryService;

    @Autowired
    private  TaskService taskService;

    @Autowired
    private ToConsoleProducer toConsoleProducer;

    @Autowired
    private  LoginfoService loginfoService;

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

    private MockMvc restEsinfoMockMvc;

    private Esinfo esinfo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EsinfoResource esinfoResource = new EsinfoResource(esinfoService, esinfoQueryService,esinfoRepository,taskService,toConsoleProducer,loginfoService);
        this.restEsinfoMockMvc = MockMvcBuilders.standaloneSetup(esinfoResource)
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
    public static Esinfo createEntity(EntityManager em) {
        Esinfo esinfo = new Esinfo()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .vnode(DEFAULT_VNODE)
            .rnode(DEFAULT_RNODE)
            .date(DEFAULT_DATE)
            .pname(DEFAULT_PNAME);
        return esinfo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Esinfo createUpdatedEntity(EntityManager em) {
        Esinfo esinfo = new Esinfo()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .vnode(UPDATED_VNODE)
            .rnode(UPDATED_RNODE)
            .date(UPDATED_DATE)
            .pname(UPDATED_PNAME);
        return esinfo;
    }

    @BeforeEach
    public void initTest() {
        esinfo = createEntity(em);
    }

    @Test
    @Transactional
    public void createEsinfo() throws Exception {
        int databaseSizeBeforeCreate = esinfoRepository.findAll().size();

        // Create the Esinfo
        restEsinfoMockMvc.perform(post("/api/esinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(esinfo)))
            .andExpect(status().isCreated());

        // Validate the Esinfo in the database
        List<Esinfo> esinfoList = esinfoRepository.findAll();
        assertThat(esinfoList).hasSize(databaseSizeBeforeCreate + 1);
        Esinfo testEsinfo = esinfoList.get(esinfoList.size() - 1);
        assertThat(testEsinfo.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEsinfo.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testEsinfo.getVnode()).isEqualTo(DEFAULT_VNODE);
        assertThat(testEsinfo.getRnode()).isEqualTo(DEFAULT_RNODE);
        assertThat(testEsinfo.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testEsinfo.getPname()).isEqualTo(DEFAULT_PNAME);
    }

    @Test
    @Transactional
    public void createEsinfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = esinfoRepository.findAll().size();

        // Create the Esinfo with an existing ID
        esinfo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEsinfoMockMvc.perform(post("/api/esinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(esinfo)))
            .andExpect(status().isBadRequest());

        // Validate the Esinfo in the database
        List<Esinfo> esinfoList = esinfoRepository.findAll();
        assertThat(esinfoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEsinfos() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList
        restEsinfoMockMvc.perform(get("/api/esinfos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(esinfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].vnode").value(hasItem(DEFAULT_VNODE)))
            .andExpect(jsonPath("$.[*].rnode").value(hasItem(DEFAULT_RNODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].pname").value(hasItem(DEFAULT_PNAME)));
    }
    
    @Test
    @Transactional
    public void getEsinfo() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get the esinfo
        restEsinfoMockMvc.perform(get("/api/esinfos/{id}", esinfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(esinfo.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.vnode").value(DEFAULT_VNODE))
            .andExpect(jsonPath("$.rnode").value(DEFAULT_RNODE))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE))
            .andExpect(jsonPath("$.pname").value(DEFAULT_PNAME));
    }


    @Test
    @Transactional
    public void getEsinfosByIdFiltering() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        Long id = esinfo.getId();

        defaultEsinfoShouldBeFound("id.equals=" + id);
        defaultEsinfoShouldNotBeFound("id.notEquals=" + id);

        defaultEsinfoShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultEsinfoShouldNotBeFound("id.greaterThan=" + id);

        defaultEsinfoShouldBeFound("id.lessThanOrEqual=" + id);
        defaultEsinfoShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllEsinfosByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where name equals to DEFAULT_NAME
        defaultEsinfoShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the esinfoList where name equals to UPDATED_NAME
        defaultEsinfoShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where name not equals to DEFAULT_NAME
        defaultEsinfoShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the esinfoList where name not equals to UPDATED_NAME
        defaultEsinfoShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByNameIsInShouldWork() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where name in DEFAULT_NAME or UPDATED_NAME
        defaultEsinfoShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the esinfoList where name equals to UPDATED_NAME
        defaultEsinfoShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where name is not null
        defaultEsinfoShouldBeFound("name.specified=true");

        // Get all the esinfoList where name is null
        defaultEsinfoShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllEsinfosByNameContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where name contains DEFAULT_NAME
        defaultEsinfoShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the esinfoList where name contains UPDATED_NAME
        defaultEsinfoShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByNameNotContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where name does not contain DEFAULT_NAME
        defaultEsinfoShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the esinfoList where name does not contain UPDATED_NAME
        defaultEsinfoShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllEsinfosByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where type equals to DEFAULT_TYPE
        defaultEsinfoShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the esinfoList where type equals to UPDATED_TYPE
        defaultEsinfoShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where type not equals to DEFAULT_TYPE
        defaultEsinfoShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the esinfoList where type not equals to UPDATED_TYPE
        defaultEsinfoShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultEsinfoShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the esinfoList where type equals to UPDATED_TYPE
        defaultEsinfoShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where type is not null
        defaultEsinfoShouldBeFound("type.specified=true");

        // Get all the esinfoList where type is null
        defaultEsinfoShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllEsinfosByTypeContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where type contains DEFAULT_TYPE
        defaultEsinfoShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the esinfoList where type contains UPDATED_TYPE
        defaultEsinfoShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where type does not contain DEFAULT_TYPE
        defaultEsinfoShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the esinfoList where type does not contain UPDATED_TYPE
        defaultEsinfoShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllEsinfosByVnodeIsEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where vnode equals to DEFAULT_VNODE
        defaultEsinfoShouldBeFound("vnode.equals=" + DEFAULT_VNODE);

        // Get all the esinfoList where vnode equals to UPDATED_VNODE
        defaultEsinfoShouldNotBeFound("vnode.equals=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByVnodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where vnode not equals to DEFAULT_VNODE
        defaultEsinfoShouldNotBeFound("vnode.notEquals=" + DEFAULT_VNODE);

        // Get all the esinfoList where vnode not equals to UPDATED_VNODE
        defaultEsinfoShouldBeFound("vnode.notEquals=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByVnodeIsInShouldWork() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where vnode in DEFAULT_VNODE or UPDATED_VNODE
        defaultEsinfoShouldBeFound("vnode.in=" + DEFAULT_VNODE + "," + UPDATED_VNODE);

        // Get all the esinfoList where vnode equals to UPDATED_VNODE
        defaultEsinfoShouldNotBeFound("vnode.in=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByVnodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where vnode is not null
        defaultEsinfoShouldBeFound("vnode.specified=true");

        // Get all the esinfoList where vnode is null
        defaultEsinfoShouldNotBeFound("vnode.specified=false");
    }
                @Test
    @Transactional
    public void getAllEsinfosByVnodeContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where vnode contains DEFAULT_VNODE
        defaultEsinfoShouldBeFound("vnode.contains=" + DEFAULT_VNODE);

        // Get all the esinfoList where vnode contains UPDATED_VNODE
        defaultEsinfoShouldNotBeFound("vnode.contains=" + UPDATED_VNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByVnodeNotContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where vnode does not contain DEFAULT_VNODE
        defaultEsinfoShouldNotBeFound("vnode.doesNotContain=" + DEFAULT_VNODE);

        // Get all the esinfoList where vnode does not contain UPDATED_VNODE
        defaultEsinfoShouldBeFound("vnode.doesNotContain=" + UPDATED_VNODE);
    }


    @Test
    @Transactional
    public void getAllEsinfosByRnodeIsEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where rnode equals to DEFAULT_RNODE
        defaultEsinfoShouldBeFound("rnode.equals=" + DEFAULT_RNODE);

        // Get all the esinfoList where rnode equals to UPDATED_RNODE
        defaultEsinfoShouldNotBeFound("rnode.equals=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByRnodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where rnode not equals to DEFAULT_RNODE
        defaultEsinfoShouldNotBeFound("rnode.notEquals=" + DEFAULT_RNODE);

        // Get all the esinfoList where rnode not equals to UPDATED_RNODE
        defaultEsinfoShouldBeFound("rnode.notEquals=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByRnodeIsInShouldWork() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where rnode in DEFAULT_RNODE or UPDATED_RNODE
        defaultEsinfoShouldBeFound("rnode.in=" + DEFAULT_RNODE + "," + UPDATED_RNODE);

        // Get all the esinfoList where rnode equals to UPDATED_RNODE
        defaultEsinfoShouldNotBeFound("rnode.in=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByRnodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where rnode is not null
        defaultEsinfoShouldBeFound("rnode.specified=true");

        // Get all the esinfoList where rnode is null
        defaultEsinfoShouldNotBeFound("rnode.specified=false");
    }
                @Test
    @Transactional
    public void getAllEsinfosByRnodeContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where rnode contains DEFAULT_RNODE
        defaultEsinfoShouldBeFound("rnode.contains=" + DEFAULT_RNODE);

        // Get all the esinfoList where rnode contains UPDATED_RNODE
        defaultEsinfoShouldNotBeFound("rnode.contains=" + UPDATED_RNODE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByRnodeNotContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where rnode does not contain DEFAULT_RNODE
        defaultEsinfoShouldNotBeFound("rnode.doesNotContain=" + DEFAULT_RNODE);

        // Get all the esinfoList where rnode does not contain UPDATED_RNODE
        defaultEsinfoShouldBeFound("rnode.doesNotContain=" + UPDATED_RNODE);
    }


    @Test
    @Transactional
    public void getAllEsinfosByDateIsEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where date equals to DEFAULT_DATE
        defaultEsinfoShouldBeFound("date.equals=" + DEFAULT_DATE);

        // Get all the esinfoList where date equals to UPDATED_DATE
        defaultEsinfoShouldNotBeFound("date.equals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where date not equals to DEFAULT_DATE
        defaultEsinfoShouldNotBeFound("date.notEquals=" + DEFAULT_DATE);

        // Get all the esinfoList where date not equals to UPDATED_DATE
        defaultEsinfoShouldBeFound("date.notEquals=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByDateIsInShouldWork() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where date in DEFAULT_DATE or UPDATED_DATE
        defaultEsinfoShouldBeFound("date.in=" + DEFAULT_DATE + "," + UPDATED_DATE);

        // Get all the esinfoList where date equals to UPDATED_DATE
        defaultEsinfoShouldNotBeFound("date.in=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where date is not null
        defaultEsinfoShouldBeFound("date.specified=true");

        // Get all the esinfoList where date is null
        defaultEsinfoShouldNotBeFound("date.specified=false");
    }
                @Test
    @Transactional
    public void getAllEsinfosByDateContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where date contains DEFAULT_DATE
        defaultEsinfoShouldBeFound("date.contains=" + DEFAULT_DATE);

        // Get all the esinfoList where date contains UPDATED_DATE
        defaultEsinfoShouldNotBeFound("date.contains=" + UPDATED_DATE);
    }

    @Test
    @Transactional
    public void getAllEsinfosByDateNotContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where date does not contain DEFAULT_DATE
        defaultEsinfoShouldNotBeFound("date.doesNotContain=" + DEFAULT_DATE);

        // Get all the esinfoList where date does not contain UPDATED_DATE
        defaultEsinfoShouldBeFound("date.doesNotContain=" + UPDATED_DATE);
    }


    @Test
    @Transactional
    public void getAllEsinfosByPnameIsEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where pname equals to DEFAULT_PNAME
        defaultEsinfoShouldBeFound("pname.equals=" + DEFAULT_PNAME);

        // Get all the esinfoList where pname equals to UPDATED_PNAME
        defaultEsinfoShouldNotBeFound("pname.equals=" + UPDATED_PNAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByPnameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where pname not equals to DEFAULT_PNAME
        defaultEsinfoShouldNotBeFound("pname.notEquals=" + DEFAULT_PNAME);

        // Get all the esinfoList where pname not equals to UPDATED_PNAME
        defaultEsinfoShouldBeFound("pname.notEquals=" + UPDATED_PNAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByPnameIsInShouldWork() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where pname in DEFAULT_PNAME or UPDATED_PNAME
        defaultEsinfoShouldBeFound("pname.in=" + DEFAULT_PNAME + "," + UPDATED_PNAME);

        // Get all the esinfoList where pname equals to UPDATED_PNAME
        defaultEsinfoShouldNotBeFound("pname.in=" + UPDATED_PNAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByPnameIsNullOrNotNull() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where pname is not null
        defaultEsinfoShouldBeFound("pname.specified=true");

        // Get all the esinfoList where pname is null
        defaultEsinfoShouldNotBeFound("pname.specified=false");
    }
                @Test
    @Transactional
    public void getAllEsinfosByPnameContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where pname contains DEFAULT_PNAME
        defaultEsinfoShouldBeFound("pname.contains=" + DEFAULT_PNAME);

        // Get all the esinfoList where pname contains UPDATED_PNAME
        defaultEsinfoShouldNotBeFound("pname.contains=" + UPDATED_PNAME);
    }

    @Test
    @Transactional
    public void getAllEsinfosByPnameNotContainsSomething() throws Exception {
        // Initialize the database
        esinfoRepository.saveAndFlush(esinfo);

        // Get all the esinfoList where pname does not contain DEFAULT_PNAME
        defaultEsinfoShouldNotBeFound("pname.doesNotContain=" + DEFAULT_PNAME);

        // Get all the esinfoList where pname does not contain UPDATED_PNAME
        defaultEsinfoShouldBeFound("pname.doesNotContain=" + UPDATED_PNAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEsinfoShouldBeFound(String filter) throws Exception {
        restEsinfoMockMvc.perform(get("/api/esinfos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(esinfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].vnode").value(hasItem(DEFAULT_VNODE)))
            .andExpect(jsonPath("$.[*].rnode").value(hasItem(DEFAULT_RNODE)))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE)))
            .andExpect(jsonPath("$.[*].pname").value(hasItem(DEFAULT_PNAME)));

        // Check, that the count call also returns 1
        restEsinfoMockMvc.perform(get("/api/esinfos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEsinfoShouldNotBeFound(String filter) throws Exception {
        restEsinfoMockMvc.perform(get("/api/esinfos?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEsinfoMockMvc.perform(get("/api/esinfos/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingEsinfo() throws Exception {
        // Get the esinfo
        restEsinfoMockMvc.perform(get("/api/esinfos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEsinfo() throws Exception {
        // Initialize the database
        esinfoService.save(esinfo);

        int databaseSizeBeforeUpdate = esinfoRepository.findAll().size();

        // Update the esinfo
        Esinfo updatedEsinfo = esinfoRepository.findById(esinfo.getId()).get();
        // Disconnect from session so that the updates on updatedEsinfo are not directly saved in db
        em.detach(updatedEsinfo);
        updatedEsinfo
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .vnode(UPDATED_VNODE)
            .rnode(UPDATED_RNODE)
            .date(UPDATED_DATE)
            .pname(UPDATED_PNAME);

        restEsinfoMockMvc.perform(put("/api/esinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEsinfo)))
            .andExpect(status().isOk());

        // Validate the Esinfo in the database
        List<Esinfo> esinfoList = esinfoRepository.findAll();
        assertThat(esinfoList).hasSize(databaseSizeBeforeUpdate);
        Esinfo testEsinfo = esinfoList.get(esinfoList.size() - 1);
        assertThat(testEsinfo.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEsinfo.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testEsinfo.getVnode()).isEqualTo(UPDATED_VNODE);
        assertThat(testEsinfo.getRnode()).isEqualTo(UPDATED_RNODE);
        assertThat(testEsinfo.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testEsinfo.getPname()).isEqualTo(UPDATED_PNAME);
    }

    @Test
    @Transactional
    public void updateNonExistingEsinfo() throws Exception {
        int databaseSizeBeforeUpdate = esinfoRepository.findAll().size();

        // Create the Esinfo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEsinfoMockMvc.perform(put("/api/esinfos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(esinfo)))
            .andExpect(status().isBadRequest());

        // Validate the Esinfo in the database
        List<Esinfo> esinfoList = esinfoRepository.findAll();
        assertThat(esinfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEsinfo() throws Exception {
        // Initialize the database
        esinfoService.save(esinfo);

        int databaseSizeBeforeDelete = esinfoRepository.findAll().size();

        // Delete the esinfo
        restEsinfoMockMvc.perform(delete("/api/esinfos/{id}", esinfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Esinfo> esinfoList = esinfoRepository.findAll();
        assertThat(esinfoList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
