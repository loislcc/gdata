package edu.buaa.web.rest;

import edu.buaa.domain.Loginfo;
import edu.buaa.repository.LoginfoRepository;
import edu.buaa.service.LoginfoService;
import edu.buaa.web.rest.errors.BadRequestAlertException;
import edu.buaa.service.dto.LoginfoCriteria;
import edu.buaa.service.LoginfoQueryService;

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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.buaa.domain.Loginfo}.
 */
@RestController
@RequestMapping("/api")
public class LoginfoResource {

    private final Logger log = LoggerFactory.getLogger(LoginfoResource.class);

    private static final String ENTITY_NAME = "gdataLoginfo";


    private final LoginfoService loginfoService;

    private final LoginfoQueryService loginfoQueryService;

    private final LoginfoRepository loginfoRepository;

    public LoginfoResource(LoginfoService loginfoService, LoginfoQueryService loginfoQueryService, LoginfoRepository loginfoRepository) {
        this.loginfoService = loginfoService;
        this.loginfoQueryService = loginfoQueryService;
        this.loginfoRepository = loginfoRepository;

    }

    /**
     * {@code POST  /loginfos} : Create a new loginfo.
     *
     * @param loginfo the loginfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loginfo, or with status {@code 400 (Bad Request)} if the loginfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loginfos")
    public ResponseEntity<Loginfo> createLoginfo(@RequestBody Loginfo loginfo) throws URISyntaxException {
        log.debug("REST request to save Loginfo : {}", loginfo);
        if (loginfo.getId() != null) {
            throw new BadRequestAlertException("A new loginfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Loginfo result = loginfoService.save(loginfo);
        return ResponseEntity.created(new URI("/api/loginfos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loginfos} : Updates an existing loginfo.
     *
     * @param loginfo the loginfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loginfo,
     * or with status {@code 400 (Bad Request)} if the loginfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loginfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loginfos")
    public ResponseEntity<Loginfo> updateLoginfo(@RequestBody Loginfo loginfo) throws URISyntaxException {
        log.debug("REST request to update Loginfo : {}", loginfo);
        if (loginfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Loginfo result = loginfoService.save(loginfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, loginfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /loginfos} : get all the loginfos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loginfos in body.
     */
    @GetMapping("/loginfos")
    public ResponseEntity<List<Loginfo>> getAllLoginfos(LoginfoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Loginfos by criteria: {}", criteria);
        Page<Loginfo> page = loginfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders( page,"/api/loginfos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /loginfos/count} : count all the loginfos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/loginfos/count")
    public ResponseEntity<Long> countLoginfos(LoginfoCriteria criteria) {
        log.debug("REST request to count Loginfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(loginfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loginfos/:id} : get the "id" loginfo.
     *
     * @param id the id of the loginfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loginfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loginfos/{id}")
    public ResponseEntity<Loginfo> getLoginfo(@PathVariable Long id) {
        log.debug("REST request to get Loginfo : {}", id);
        Optional<Loginfo> loginfo = loginfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loginfo);
    }

    /**
     * {@code DELETE  /loginfos/:id} : delete the "id" loginfo.
     *
     * @param id the id of the loginfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loginfos/{id}")
    public ResponseEntity<Void> deleteLoginfo(@PathVariable Long id) {
        log.debug("REST request to delete Loginfo : {}", id);
        loginfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/loginfos/add")
    public ResponseEntity<JSONObject> importLog(@RequestBody JSONObject jsonObject) throws Exception {
        log.debug("REST request to add loginfo : {}", jsonObject);
        JSONObject temp = new JSONObject();
        Loginfo loginfo = new Loginfo();
        String ip = jsonObject.getString("ip");
        loginfo.setIp(ip);
        loginfo.setEventime(jsonObject.getString("currentTime"));
        loginfo.setType("Device");
        loginfo.setName(ip);
        loginfo.setX(jsonObject.getDouble("selfLongitude"));
        loginfo.setY(jsonObject.getDouble("selfLatitude"));
        if(!loginfoRepository.existsByIpAndType(ip,"Device")){
            loginfoService.save(loginfo);
        }else {
            Loginfo loginfo1 = loginfoRepository.findByIpAndType(ip,"Device");
            loginfo.setId(loginfo1.getId());
            loginfoService.save(loginfo);
        }

        Loginfo loginfo2 = new Loginfo();
        String type = jsonObject.getString("category");
        String name = jsonObject.getString("name");
        loginfo2.setIp(ip);
        loginfo2.setName(name);
        loginfo2.setType(type);
        loginfo2.setX(jsonObject.getDouble("longitude"));
        loginfo2.setY(jsonObject.getDouble("latitude"));
        loginfo2.setOwner(jsonObject.getString("owner"));
        loginfo2.setLevel(jsonObject.getString("level"));
        loginfo2.setNote(jsonObject.getString("note"));
        loginfo2.setEventime(jsonObject.getString("currentTime"));
        if(!loginfoRepository.existsByTypeAndName(type,name)){
            loginfoService.save(loginfo2);
        }else {
            Loginfo loginfo3 = loginfoRepository.findByTypeAndName(type,name);
            loginfo2.setId(loginfo3.getId());
            loginfoService.save(loginfo2);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
