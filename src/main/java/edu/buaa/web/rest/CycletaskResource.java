package edu.buaa.web.rest;

import edu.buaa.domain.Cycletask;
import edu.buaa.service.CycletaskService;
import edu.buaa.web.rest.errors.BadRequestAlertException;
import edu.buaa.service.dto.CycletaskCriteria;
import edu.buaa.service.CycletaskQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
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
 * REST controller for managing {@link edu.buaa.domain.Cycletask}.
 */
@RestController
@RequestMapping("/api")
public class CycletaskResource {

    private final Logger log = LoggerFactory.getLogger(CycletaskResource.class);

    private static final String ENTITY_NAME = "gdataCycletask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CycletaskService cycletaskService;

    private final CycletaskQueryService cycletaskQueryService;

    public CycletaskResource(CycletaskService cycletaskService, CycletaskQueryService cycletaskQueryService) {
        this.cycletaskService = cycletaskService;
        this.cycletaskQueryService = cycletaskQueryService;
    }

    /**
     * {@code POST  /cycletasks} : Create a new cycletask.
     *
     * @param cycletask the cycletask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cycletask, or with status {@code 400 (Bad Request)} if the cycletask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cycletasks")
    public ResponseEntity<Cycletask> createCycletask(@RequestBody Cycletask cycletask) throws URISyntaxException {
        log.debug("REST request to save Cycletask : {}", cycletask);
        if (cycletask.getId() != null) {
            throw new BadRequestAlertException("A new cycletask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cycletask result = cycletaskService.save(cycletask);
        return ResponseEntity.created(new URI("/api/cycletasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cycletasks} : Updates an existing cycletask.
     *
     * @param cycletask the cycletask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cycletask,
     * or with status {@code 400 (Bad Request)} if the cycletask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cycletask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cycletasks")
    public ResponseEntity<Cycletask> updateCycletask(@RequestBody Cycletask cycletask) throws URISyntaxException {
        log.debug("REST request to update Cycletask : {}", cycletask);
        if (cycletask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Cycletask result = cycletaskService.save(cycletask);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cycletask.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cycletasks} : get all the cycletasks.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cycletasks in body.
     */
    @GetMapping("/cycletasks")
    public ResponseEntity<List<Cycletask>> getAllCycletasks(CycletaskCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Cycletasks by criteria: {}", criteria);
        Page<Cycletask> page = cycletaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /cycletasks/count} : count all the cycletasks.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/cycletasks/count")
    public ResponseEntity<Long> countCycletasks(CycletaskCriteria criteria) {
        log.debug("REST request to count Cycletasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(cycletaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cycletasks/:id} : get the "id" cycletask.
     *
     * @param id the id of the cycletask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cycletask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cycletasks/{id}")
    public ResponseEntity<Cycletask> getCycletask(@PathVariable Long id) {
        log.debug("REST request to get Cycletask : {}", id);
        Optional<Cycletask> cycletask = cycletaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cycletask);
    }

    /**
     * {@code DELETE  /cycletasks/:id} : delete the "id" cycletask.
     *
     * @param id the id of the cycletask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cycletasks/{id}")
    public ResponseEntity<Void> deleteCycletask(@PathVariable Long id) {
        log.debug("REST request to delete Cycletask : {}", id);
        cycletaskService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
