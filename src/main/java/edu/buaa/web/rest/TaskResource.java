package edu.buaa.web.rest;

import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Cycletask;
import edu.buaa.domain.Task;
import edu.buaa.repository.CycletaskRepository;
import edu.buaa.repository.TaskRepository;
import edu.buaa.service.CycletaskService;
import edu.buaa.service.TaskService;
import edu.buaa.service.message.ToConsoleProducer;
import edu.buaa.web.rest.errors.BadRequestAlertException;
import edu.buaa.service.dto.TaskCriteria;
import edu.buaa.service.TaskQueryService;


import edu.buaa.web.rest.util.HeaderUtil;
import edu.buaa.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.buaa.domain.Task}.
 */
@RestController
@RequestMapping("/api")
public class TaskResource {

    private final Logger log = LoggerFactory.getLogger(TaskResource.class);

    private static final String ENTITY_NAME = "gdataTask";


    private final TaskService taskService;

    private final TaskQueryService taskQueryService;

    private final CycletaskService cycletaskService;

    private final CycletaskRepository cycletaskRepository;

    private final ToConsoleProducer toConsoleProducer;

    public TaskResource(TaskService taskService, TaskQueryService taskQueryService, CycletaskService cycletaskService,
                        CycletaskRepository cycletaskRepository, ToConsoleProducer toConsoleProducer) {
        this.taskService = taskService;
        this.taskQueryService = taskQueryService;
        this.cycletaskService = cycletaskService;
        this.cycletaskRepository = cycletaskRepository;
        this.toConsoleProducer = toConsoleProducer;
    }

    /**
     * {@code POST  /tasks} : Create a new task.
     *
     * @param task the task to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new task, or with status {@code 400 (Bad Request)} if the task has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to save Task : {}", task);
        if (task.getId() != null) {
            throw new BadRequestAlertException("A new task cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Task result = taskService.save(task);
        return ResponseEntity.created(new URI("/api/tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tasks} : Updates an existing task.
     *
     * @param task the task to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated task,
     * or with status {@code 400 (Bad Request)} if the task is not valid,
     * or with status {@code 500 (Internal Server Error)} if the task couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tasks")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) throws URISyntaxException {
        log.debug("REST request to update Task : {}", task);
        if (task.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Task result = taskService.save(task);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, task.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /tasks} : get all the tasks.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tasks in body.
     */
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks(TaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Tasks by criteria: {}", criteria);
        Page<Task> page = taskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders( page,"/api/tasks");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /tasks/count} : count all the tasks.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/tasks/count")
    public ResponseEntity<Long> countTasks(TaskCriteria criteria) {
        log.debug("REST request to count Tasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(taskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tasks/:id} : get the "id" task.
     *
     * @param id the id of the task to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the task, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tasks/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        log.debug("REST request to get Task : {}", id);
        Optional<Task> task = taskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(task);
    }

    /**
     * {@code DELETE  /tasks/:id} : delete the "id" task.
     *
     * @param id the id of the task to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        log.debug("REST request to delete Task : {}", id);
        taskService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/tasks/add")
    public ResponseEntity<JSONObject> importTask(@RequestBody JSONObject jsonObject) throws Exception {
        log.debug("REST request to add taskinfo : {}", jsonObject);
        Task task = new Task();
        task.setChecknum(jsonObject.getString("checknum"));
        task.setDatanum(jsonObject.getString("datanum"));
        task.setMatrix(jsonObject.getString("matrix"));
        task.setStartime(jsonObject.getString("startime"));
        task.setEndtime(jsonObject.getString("endtime"));
        task.setRealtime(jsonObject.getString("realtime"));
        task.setName(jsonObject.getString("name"));
        task.setType(jsonObject.getString("type"));
        Task task1 = taskService.save(task);
        if(task.getType().equals("cycle")) {
            Cycletask cycletask = new Cycletask();
            cycletask.setCycle(jsonObject.getString("cycle"));
            cycletask.setName(task.getName());
            cycletask.setTaskid(task1.getId());
            log.debug("@@@@@@@:{}",task.getStartime());
            cycletaskService.save(cycletask);
//            cycletask.setNextime();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/tasks/delete")
    public ResponseEntity<JSONObject> deletetaskinfo(@RequestBody Long[] idlist) {
        for (Long aLong : idlist) {

            Optional<Task> taskOptional = taskService.findOne(aLong);
            if(taskOptional.isPresent()){
                Task task = taskOptional.get();
                if(task.getType().equals("cycle")){
                    cycletaskService.deletebytaskid(aLong);
                }
            }
            deleteTask(aLong);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PostMapping("/tasks/sendMsg")
    public ResponseEntity<JSONObject> sendMsg(@RequestBody String str) throws Exception {
        toConsoleProducer.sendMsgToGatewayConsole(str);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
