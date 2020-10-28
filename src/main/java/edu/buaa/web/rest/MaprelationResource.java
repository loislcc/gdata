package edu.buaa.web.rest;

import edu.buaa.domain.Maprelation;
import edu.buaa.repository.MaprelationRepository;
import edu.buaa.service.MaprelationService;
import edu.buaa.web.rest.errors.BadRequestAlertException;
import edu.buaa.service.dto.MaprelationCriteria;
import edu.buaa.service.MaprelationQueryService;


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
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.buaa.domain.Maprelation}.
 */
@RestController
@RequestMapping("/api")
public class MaprelationResource {

    private final Logger log = LoggerFactory.getLogger(MaprelationResource.class);

    private static final String ENTITY_NAME = "gdataMaprelation";



    private final MaprelationService maprelationService;

    private final MaprelationQueryService maprelationQueryService;

    private final MaprelationRepository maprelationRepository;

    public MaprelationResource(MaprelationService maprelationService, MaprelationQueryService maprelationQueryService, MaprelationRepository maprelationRepository) {
        this.maprelationService = maprelationService;
        this.maprelationQueryService = maprelationQueryService;
        this.maprelationRepository = maprelationRepository;
    }

    /**
     * {@code POST  /maprelations} : Create a new maprelation.
     *
     * @param maprelation the maprelation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new maprelation, or with status {@code 400 (Bad Request)} if the maprelation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/maprelations")
    public ResponseEntity<Maprelation> createMaprelation(@RequestBody Maprelation maprelation) throws URISyntaxException {
        log.debug("REST request to save Maprelation : {}", maprelation);
        if (maprelation.getId() != null) {
            throw new BadRequestAlertException("A new maprelation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Maprelation result = maprelationService.save(maprelation);
        return ResponseEntity.created(new URI("/api/maprelations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /maprelations} : Updates an existing maprelation.
     *
     * @param maprelation the maprelation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated maprelation,
     * or with status {@code 400 (Bad Request)} if the maprelation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the maprelation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/maprelations")
    public ResponseEntity<Maprelation> updateMaprelation(@RequestBody Maprelation maprelation) throws URISyntaxException {
        log.debug("REST request to update Maprelation : {}", maprelation);
        if (maprelation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Maprelation result = maprelationService.save(maprelation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, maprelation.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /maprelations} : get all the maprelations.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of maprelations in body.
     */
    @GetMapping("/maprelations")
    public ResponseEntity<List<Maprelation>> getAllMaprelations(MaprelationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Maprelations by criteria: {}", criteria);
        Page<Maprelation> page = maprelationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders( page, "/api/maprelations");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /maprelations/count} : count all the maprelations.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/maprelations/count")
    public ResponseEntity<Long> countMaprelations(MaprelationCriteria criteria) {
        log.debug("REST request to count Maprelations by criteria: {}", criteria);
        return ResponseEntity.ok().body(maprelationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /maprelations/:id} : get the "id" maprelation.
     *
     * @param id the id of the maprelation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the maprelation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/maprelations/{id}")
    public ResponseEntity<Maprelation> getMaprelation(@PathVariable Long id) {
        log.debug("REST request to get Maprelation : {}", id);
        Optional<Maprelation> maprelation = maprelationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(maprelation);
    }

    /**
     * {@code DELETE  /maprelations/:id} : delete the "id" maprelation.
     *
     * @param id the id of the maprelation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/maprelations/{id}")
    public ResponseEntity<Void> deleteMaprelation(@PathVariable Long id) {
        log.debug("REST request to delete Maprelation : {}", id);
        maprelationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert( ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/maprelations/get")
    public List<Maprelation> getMaprelations() {
        return maprelationRepository.findAll();
    }
}
