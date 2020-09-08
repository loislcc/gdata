package edu.buaa.service;

import edu.buaa.domain.Maprelation;
import edu.buaa.repository.MaprelationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Maprelation}.
 */
@Service
@Transactional
public class MaprelationService {

    private final Logger log = LoggerFactory.getLogger(MaprelationService.class);

    private final MaprelationRepository maprelationRepository;

    public MaprelationService(MaprelationRepository maprelationRepository) {
        this.maprelationRepository = maprelationRepository;
    }

    /**
     * Save a maprelation.
     *
     * @param maprelation the entity to save.
     * @return the persisted entity.
     */
    public Maprelation save(Maprelation maprelation) {
        log.debug("Request to save Maprelation : {}", maprelation);
        return maprelationRepository.save(maprelation);
    }

    /**
     * Get all the maprelations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Maprelation> findAll(Pageable pageable) {
        log.debug("Request to get all Maprelations");
        return maprelationRepository.findAll(pageable);
    }


    /**
     * Get one maprelation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Maprelation> findOne(Long id) {
        log.debug("Request to get Maprelation : {}", id);
        return maprelationRepository.findById(id);
    }

    /**
     * Delete the maprelation by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Maprelation : {}", id);
        maprelationRepository.deleteById(id);
    }
}
