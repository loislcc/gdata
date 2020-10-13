package edu.buaa.service;

import edu.buaa.domain.Cycletask;
import edu.buaa.repository.CycletaskRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Cycletask}.
 */
@Service
@Transactional
public class CycletaskService {

    private final Logger log = LoggerFactory.getLogger(CycletaskService.class);

    private final CycletaskRepository cycletaskRepository;

    public CycletaskService(CycletaskRepository cycletaskRepository) {
        this.cycletaskRepository = cycletaskRepository;
    }

    /**
     * Save a cycletask.
     *
     * @param cycletask the entity to save.
     * @return the persisted entity.
     */
    public Cycletask save(Cycletask cycletask) {
        log.debug("Request to save Cycletask : {}", cycletask);
        return cycletaskRepository.save(cycletask);
    }

    /**
     * Get all the cycletasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Cycletask> findAll(Pageable pageable) {
        log.debug("Request to get all Cycletasks");
        return cycletaskRepository.findAll(pageable);
    }


    /**
     * Get one cycletask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Cycletask> findOne(Long id) {
        log.debug("Request to get Cycletask : {}", id);
        return cycletaskRepository.findById(id);
    }

    /**
     * Delete the cycletask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Cycletask : {}", id);
        cycletaskRepository.deleteById(id);
    }

    @Transactional(readOnly = false)
    public void deletebytaskid(Long taskid) {
        cycletaskRepository.deleteAllByTaskid(taskid);


    }
}
