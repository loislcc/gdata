package edu.buaa.service;

import edu.buaa.domain.Esinfo;
import edu.buaa.repository.EsinfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Esinfo}.
 */
@Service
@Transactional
public class EsinfoService {

    private final Logger log = LoggerFactory.getLogger(EsinfoService.class);

    private final EsinfoRepository esinfoRepository;

    public EsinfoService(EsinfoRepository esinfoRepository) {
        this.esinfoRepository = esinfoRepository;
    }

    /**
     * Save a esinfo.
     *
     * @param esinfo the entity to save.
     * @return the persisted entity.
     */
    public Esinfo save(Esinfo esinfo) {
        log.debug("Request to save Esinfo : {}", esinfo);
        return esinfoRepository.save(esinfo);
    }

    /**
     * Get all the esinfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Esinfo> findAll(Pageable pageable) {
        log.debug("Request to get all Esinfos");
        return esinfoRepository.findAll(pageable);
    }


    /**
     * Get one esinfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Esinfo> findOne(Long id) {
        log.debug("Request to get Esinfo : {}", id);
        return esinfoRepository.findById(id);
    }

    /**
     * Delete the esinfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Esinfo : {}", id);
        esinfoRepository.deleteById(id);
    }
}