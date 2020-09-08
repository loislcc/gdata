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

import edu.buaa.domain.Maprelation;
import edu.buaa.domain.*; // for static metamodels
import edu.buaa.repository.MaprelationRepository;
import edu.buaa.service.dto.MaprelationCriteria;

/**
 * Service for executing complex queries for {@link Maprelation} entities in the database.
 * The main input is a {@link MaprelationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Maprelation} or a {@link Page} of {@link Maprelation} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MaprelationQueryService extends QueryService<Maprelation> {

    private final Logger log = LoggerFactory.getLogger(MaprelationQueryService.class);

    private final MaprelationRepository maprelationRepository;

    public MaprelationQueryService(MaprelationRepository maprelationRepository) {
        this.maprelationRepository = maprelationRepository;
    }

    /**
     * Return a {@link List} of {@link Maprelation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Maprelation> findByCriteria(MaprelationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Maprelation> specification = createSpecification(criteria);
        return maprelationRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Maprelation} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Maprelation> findByCriteria(MaprelationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Maprelation> specification = createSpecification(criteria);
        return maprelationRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MaprelationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Maprelation> specification = createSpecification(criteria);
        return maprelationRepository.count(specification);
    }

    /**
     * Function to convert {@link MaprelationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Maprelation> createSpecification(MaprelationCriteria criteria) {
        Specification<Maprelation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Maprelation_.id));
            }
            if (criteria.getVnode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVnode(), Maprelation_.vnode));
            }
            if (criteria.getRnode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRnode(), Maprelation_.rnode));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatus(), Maprelation_.status));
            }
            if (criteria.getSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSize(), Maprelation_.size));
            }
        }
        return specification;
    }
}
