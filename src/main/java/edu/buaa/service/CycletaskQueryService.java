package edu.buaa.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import edu.buaa.domain.Cycletask;
import edu.buaa.domain.*; // for static metamodels
import edu.buaa.repository.CycletaskRepository;
import edu.buaa.service.dto.CycletaskCriteria;

/**
 * Service for executing complex queries for {@link Cycletask} entities in the database.
 * The main input is a {@link CycletaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Cycletask} or a {@link Page} of {@link Cycletask} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CycletaskQueryService extends QueryService<Cycletask> {

    private final Logger log = LoggerFactory.getLogger(CycletaskQueryService.class);

    private final CycletaskRepository cycletaskRepository;

    public CycletaskQueryService(CycletaskRepository cycletaskRepository) {
        this.cycletaskRepository = cycletaskRepository;
    }

    /**
     * Return a {@link List} of {@link Cycletask} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Cycletask> findByCriteria(CycletaskCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Cycletask> specification = createSpecification(criteria);
        return cycletaskRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Cycletask} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Cycletask> findByCriteria(CycletaskCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cycletask> specification = createSpecification(criteria);
        return cycletaskRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CycletaskCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Cycletask> specification = createSpecification(criteria);
        return cycletaskRepository.count(specification);
    }

    /**
     * Function to convert {@link CycletaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cycletask> createSpecification(CycletaskCriteria criteria) {
        Specification<Cycletask> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Cycletask_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Cycletask_.name));
            }
            if (criteria.getCycle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCycle(), Cycletask_.cycle));
            }
            if (criteria.getNextime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNextime(), Cycletask_.nextime));
            }
            if (criteria.getNextendtime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNextendtime(), Cycletask_.nextendtime));
            }
            if (criteria.getTaskid() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaskid(), Cycletask_.taskid));
            }
        }
        return specification;
    }
}
