package edu.buaa.web.rest;

import edu.buaa.GdataApp;
import edu.buaa.domain.Task;
import edu.buaa.repository.CycletaskRepository;
import edu.buaa.repository.TaskRepository;
import edu.buaa.service.CycletaskService;
import edu.buaa.service.LoginfoService;
import edu.buaa.service.TaskService;
import edu.buaa.service.message.ToConsoleProducer;
import edu.buaa.web.rest.errors.ExceptionTranslator;
import edu.buaa.service.dto.TaskCriteria;
import edu.buaa.service.TaskQueryService;

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
 * Integration tests for the {@link TaskResource} REST controller.
 */
@SpringBootTest(classes = GdataApp.class)
public class TaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_STARTIME = "AAAAAAAAAA";
    private static final String UPDATED_STARTIME = "BBBBBBBBBB";

    private static final String DEFAULT_ENDTIME = "AAAAAAAAAA";
    private static final String UPDATED_ENDTIME = "BBBBBBBBBB";

    private static final String DEFAULT_REALTIME = "AAAAAAAAAA";
    private static final String UPDATED_REALTIME = "BBBBBBBBBB";

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskQueryService taskQueryService;

    @Autowired
    private CycletaskService cycletaskService;

    @Autowired
    private CycletaskRepository cycletaskRepository;

    @Autowired
    private ToConsoleProducer toConsoleProducer;

    private LoginfoService loginfoService;
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

    private MockMvc restTaskMockMvc;

    private Task task;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final TaskResource taskResource = new TaskResource(taskService, taskQueryService, cycletaskService,cycletaskRepository,toConsoleProducer, loginfoService);
        this.restTaskMockMvc = MockMvcBuilders.standaloneSetup(taskResource)
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
    public static Task createEntity(EntityManager em) {
        Task task = new Task()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .startime(DEFAULT_STARTIME)
            .endtime(DEFAULT_ENDTIME)
            .realtime(DEFAULT_REALTIME)
            .status(DEFAULT_STATUS);
        return task;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Task createUpdatedEntity(EntityManager em) {
        Task task = new Task()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startime(UPDATED_STARTIME)
            .endtime(UPDATED_ENDTIME)
            .realtime(UPDATED_REALTIME)
            .status(UPDATED_STATUS);
        return task;
    }

    @BeforeEach
    public void initTest() {
        task = createEntity(em);
    }

    @Test
    @Transactional
    public void createTask() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isCreated());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate + 1);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTask.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTask.getStartime()).isEqualTo(DEFAULT_STARTIME);
        assertThat(testTask.getEndtime()).isEqualTo(DEFAULT_ENDTIME);
        assertThat(testTask.getRealtime()).isEqualTo(DEFAULT_REALTIME);
        assertThat(testTask.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    public void createTaskWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = taskRepository.findAll().size();

        // Create the Task with an existing ID
        task.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTaskMockMvc.perform(post("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllTasks() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].startime").value(hasItem(DEFAULT_STARTIME)))
            .andExpect(jsonPath("$.[*].endtime").value(hasItem(DEFAULT_ENDTIME)))
            .andExpect(jsonPath("$.[*].realtime").value(hasItem(DEFAULT_REALTIME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));
    }
    
    @Test
    @Transactional
    public void getTask() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", task.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(task.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.startime").value(DEFAULT_STARTIME))
            .andExpect(jsonPath("$.endtime").value(DEFAULT_ENDTIME))
            .andExpect(jsonPath("$.realtime").value(DEFAULT_REALTIME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS));
    }


    @Test
    @Transactional
    public void getTasksByIdFiltering() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        Long id = task.getId();

        defaultTaskShouldBeFound("id.equals=" + id);
        defaultTaskShouldNotBeFound("id.notEquals=" + id);

        defaultTaskShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.greaterThan=" + id);

        defaultTaskShouldBeFound("id.lessThanOrEqual=" + id);
        defaultTaskShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where name equals to DEFAULT_NAME
        defaultTaskShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the taskList where name equals to UPDATED_NAME
        defaultTaskShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where name not equals to DEFAULT_NAME
        defaultTaskShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the taskList where name not equals to UPDATED_NAME
        defaultTaskShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where name in DEFAULT_NAME or UPDATED_NAME
        defaultTaskShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the taskList where name equals to UPDATED_NAME
        defaultTaskShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where name is not null
        defaultTaskShouldBeFound("name.specified=true");

        // Get all the taskList where name is null
        defaultTaskShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByNameContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where name contains DEFAULT_NAME
        defaultTaskShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the taskList where name contains UPDATED_NAME
        defaultTaskShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllTasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where name does not contain DEFAULT_NAME
        defaultTaskShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the taskList where name does not contain UPDATED_NAME
        defaultTaskShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllTasksByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where type equals to DEFAULT_TYPE
        defaultTaskShouldBeFound("type.equals=" + DEFAULT_TYPE);

        // Get all the taskList where type equals to UPDATED_TYPE
        defaultTaskShouldNotBeFound("type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTypeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where type not equals to DEFAULT_TYPE
        defaultTaskShouldNotBeFound("type.notEquals=" + DEFAULT_TYPE);

        // Get all the taskList where type not equals to UPDATED_TYPE
        defaultTaskShouldBeFound("type.notEquals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where type in DEFAULT_TYPE or UPDATED_TYPE
        defaultTaskShouldBeFound("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE);

        // Get all the taskList where type equals to UPDATED_TYPE
        defaultTaskShouldNotBeFound("type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where type is not null
        defaultTaskShouldBeFound("type.specified=true");

        // Get all the taskList where type is null
        defaultTaskShouldNotBeFound("type.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByTypeContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where type contains DEFAULT_TYPE
        defaultTaskShouldBeFound("type.contains=" + DEFAULT_TYPE);

        // Get all the taskList where type contains UPDATED_TYPE
        defaultTaskShouldNotBeFound("type.contains=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    public void getAllTasksByTypeNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where type does not contain DEFAULT_TYPE
        defaultTaskShouldNotBeFound("type.doesNotContain=" + DEFAULT_TYPE);

        // Get all the taskList where type does not contain UPDATED_TYPE
        defaultTaskShouldBeFound("type.doesNotContain=" + UPDATED_TYPE);
    }


    @Test
    @Transactional
    public void getAllTasksByStartimeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where startime equals to DEFAULT_STARTIME
        defaultTaskShouldBeFound("startime.equals=" + DEFAULT_STARTIME);

        // Get all the taskList where startime equals to UPDATED_STARTIME
        defaultTaskShouldNotBeFound("startime.equals=" + UPDATED_STARTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByStartimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where startime not equals to DEFAULT_STARTIME
        defaultTaskShouldNotBeFound("startime.notEquals=" + DEFAULT_STARTIME);

        // Get all the taskList where startime not equals to UPDATED_STARTIME
        defaultTaskShouldBeFound("startime.notEquals=" + UPDATED_STARTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByStartimeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where startime in DEFAULT_STARTIME or UPDATED_STARTIME
        defaultTaskShouldBeFound("startime.in=" + DEFAULT_STARTIME + "," + UPDATED_STARTIME);

        // Get all the taskList where startime equals to UPDATED_STARTIME
        defaultTaskShouldNotBeFound("startime.in=" + UPDATED_STARTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByStartimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where startime is not null
        defaultTaskShouldBeFound("startime.specified=true");

        // Get all the taskList where startime is null
        defaultTaskShouldNotBeFound("startime.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByStartimeContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where startime contains DEFAULT_STARTIME
        defaultTaskShouldBeFound("startime.contains=" + DEFAULT_STARTIME);

        // Get all the taskList where startime contains UPDATED_STARTIME
        defaultTaskShouldNotBeFound("startime.contains=" + UPDATED_STARTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByStartimeNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where startime does not contain DEFAULT_STARTIME
        defaultTaskShouldNotBeFound("startime.doesNotContain=" + DEFAULT_STARTIME);

        // Get all the taskList where startime does not contain UPDATED_STARTIME
        defaultTaskShouldBeFound("startime.doesNotContain=" + UPDATED_STARTIME);
    }


    @Test
    @Transactional
    public void getAllTasksByEndtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where endtime equals to DEFAULT_ENDTIME
        defaultTaskShouldBeFound("endtime.equals=" + DEFAULT_ENDTIME);

        // Get all the taskList where endtime equals to UPDATED_ENDTIME
        defaultTaskShouldNotBeFound("endtime.equals=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEndtimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where endtime not equals to DEFAULT_ENDTIME
        defaultTaskShouldNotBeFound("endtime.notEquals=" + DEFAULT_ENDTIME);

        // Get all the taskList where endtime not equals to UPDATED_ENDTIME
        defaultTaskShouldBeFound("endtime.notEquals=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEndtimeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where endtime in DEFAULT_ENDTIME or UPDATED_ENDTIME
        defaultTaskShouldBeFound("endtime.in=" + DEFAULT_ENDTIME + "," + UPDATED_ENDTIME);

        // Get all the taskList where endtime equals to UPDATED_ENDTIME
        defaultTaskShouldNotBeFound("endtime.in=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEndtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where endtime is not null
        defaultTaskShouldBeFound("endtime.specified=true");

        // Get all the taskList where endtime is null
        defaultTaskShouldNotBeFound("endtime.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByEndtimeContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where endtime contains DEFAULT_ENDTIME
        defaultTaskShouldBeFound("endtime.contains=" + DEFAULT_ENDTIME);

        // Get all the taskList where endtime contains UPDATED_ENDTIME
        defaultTaskShouldNotBeFound("endtime.contains=" + UPDATED_ENDTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByEndtimeNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where endtime does not contain DEFAULT_ENDTIME
        defaultTaskShouldNotBeFound("endtime.doesNotContain=" + DEFAULT_ENDTIME);

        // Get all the taskList where endtime does not contain UPDATED_ENDTIME
        defaultTaskShouldBeFound("endtime.doesNotContain=" + UPDATED_ENDTIME);
    }


    @Test
    @Transactional
    public void getAllTasksByRealtimeIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where realtime equals to DEFAULT_REALTIME
        defaultTaskShouldBeFound("realtime.equals=" + DEFAULT_REALTIME);

        // Get all the taskList where realtime equals to UPDATED_REALTIME
        defaultTaskShouldNotBeFound("realtime.equals=" + UPDATED_REALTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByRealtimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where realtime not equals to DEFAULT_REALTIME
        defaultTaskShouldNotBeFound("realtime.notEquals=" + DEFAULT_REALTIME);

        // Get all the taskList where realtime not equals to UPDATED_REALTIME
        defaultTaskShouldBeFound("realtime.notEquals=" + UPDATED_REALTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByRealtimeIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where realtime in DEFAULT_REALTIME or UPDATED_REALTIME
        defaultTaskShouldBeFound("realtime.in=" + DEFAULT_REALTIME + "," + UPDATED_REALTIME);

        // Get all the taskList where realtime equals to UPDATED_REALTIME
        defaultTaskShouldNotBeFound("realtime.in=" + UPDATED_REALTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByRealtimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where realtime is not null
        defaultTaskShouldBeFound("realtime.specified=true");

        // Get all the taskList where realtime is null
        defaultTaskShouldNotBeFound("realtime.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByRealtimeContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where realtime contains DEFAULT_REALTIME
        defaultTaskShouldBeFound("realtime.contains=" + DEFAULT_REALTIME);

        // Get all the taskList where realtime contains UPDATED_REALTIME
        defaultTaskShouldNotBeFound("realtime.contains=" + UPDATED_REALTIME);
    }

    @Test
    @Transactional
    public void getAllTasksByRealtimeNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where realtime does not contain DEFAULT_REALTIME
        defaultTaskShouldNotBeFound("realtime.doesNotContain=" + DEFAULT_REALTIME);

        // Get all the taskList where realtime does not contain UPDATED_REALTIME
        defaultTaskShouldBeFound("realtime.doesNotContain=" + UPDATED_REALTIME);
    }


    @Test
    @Transactional
    public void getAllTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status equals to DEFAULT_STATUS
        defaultTaskShouldBeFound("status.equals=" + DEFAULT_STATUS);

        // Get all the taskList where status equals to UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status not equals to DEFAULT_STATUS
        defaultTaskShouldNotBeFound("status.notEquals=" + DEFAULT_STATUS);

        // Get all the taskList where status not equals to UPDATED_STATUS
        defaultTaskShouldBeFound("status.notEquals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status in DEFAULT_STATUS or UPDATED_STATUS
        defaultTaskShouldBeFound("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS);

        // Get all the taskList where status equals to UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status is not null
        defaultTaskShouldBeFound("status.specified=true");

        // Get all the taskList where status is null
        defaultTaskShouldNotBeFound("status.specified=false");
    }
                @Test
    @Transactional
    public void getAllTasksByStatusContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status contains DEFAULT_STATUS
        defaultTaskShouldBeFound("status.contains=" + DEFAULT_STATUS);

        // Get all the taskList where status contains UPDATED_STATUS
        defaultTaskShouldNotBeFound("status.contains=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void getAllTasksByStatusNotContainsSomething() throws Exception {
        // Initialize the database
        taskRepository.saveAndFlush(task);

        // Get all the taskList where status does not contain DEFAULT_STATUS
        defaultTaskShouldNotBeFound("status.doesNotContain=" + DEFAULT_STATUS);

        // Get all the taskList where status does not contain UPDATED_STATUS
        defaultTaskShouldBeFound("status.doesNotContain=" + UPDATED_STATUS);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTaskShouldBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(task.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].startime").value(hasItem(DEFAULT_STARTIME)))
            .andExpect(jsonPath("$.[*].endtime").value(hasItem(DEFAULT_ENDTIME)))
            .andExpect(jsonPath("$.[*].realtime").value(hasItem(DEFAULT_REALTIME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)));

        // Check, that the count call also returns 1
        restTaskMockMvc.perform(get("/api/tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTaskShouldNotBeFound(String filter) throws Exception {
        restTaskMockMvc.perform(get("/api/tasks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTaskMockMvc.perform(get("/api/tasks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingTask() throws Exception {
        // Get the task
        restTaskMockMvc.perform(get("/api/tasks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask() throws Exception {
        // Initialize the database
        taskService.save(task);

        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Update the task
        Task updatedTask = taskRepository.findById(task.getId()).get();
        // Disconnect from session so that the updates on updatedTask are not directly saved in db
        em.detach(updatedTask);
        updatedTask
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startime(UPDATED_STARTIME)
            .endtime(UPDATED_ENDTIME)
            .realtime(UPDATED_REALTIME)
            .status(UPDATED_STATUS);

        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedTask)))
            .andExpect(status().isOk());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
        Task testTask = taskList.get(taskList.size() - 1);
        assertThat(testTask.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTask.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTask.getStartime()).isEqualTo(UPDATED_STARTIME);
        assertThat(testTask.getEndtime()).isEqualTo(UPDATED_ENDTIME);
        assertThat(testTask.getRealtime()).isEqualTo(UPDATED_REALTIME);
        assertThat(testTask.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void updateNonExistingTask() throws Exception {
        int databaseSizeBeforeUpdate = taskRepository.findAll().size();

        // Create the Task

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTaskMockMvc.perform(put("/api/tasks")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(task)))
            .andExpect(status().isBadRequest());

        // Validate the Task in the database
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteTask() throws Exception {
        // Initialize the database
        taskService.save(task);

        int databaseSizeBeforeDelete = taskRepository.findAll().size();

        // Delete the task
        restTaskMockMvc.perform(delete("/api/tasks/{id}", task.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Task> taskList = taskRepository.findAll();
        assertThat(taskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
