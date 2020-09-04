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

import edu.buaa.domain.Esinfo;
import edu.buaa.domain.*; // for static metamodels
import edu.buaa.repository.EsinfoRepository;
import edu.buaa.service.dto.EsinfoCriteria;

/**
 * Service for executing complex queries for {@link Esinfo} entities in the database.
 * The main input is a {@link EsinfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Esinfo} or a {@link Page} of {@link Esinfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EsinfoQueryService extends QueryService<Esinfo> {

    private final Logger log = LoggerFactory.getLogger(EsinfoQueryService.class);

    private final EsinfoRepository esinfoRepository;

    public EsinfoQueryService(EsinfoRepository esinfoRepository) {
        this.esinfoRepository = esinfoRepository;
    }

    /**
     * Return a {@link List} of {@link Esinfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Esinfo> findByCriteria(EsinfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Esinfo> specification = createSpecification(criteria);
        return esinfoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Esinfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Esinfo> findByCriteria(EsinfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Esinfo> specification = createSpecification(criteria);
        return esinfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EsinfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Esinfo> specification = createSpecification(criteria);
        return esinfoRepository.count(specification);
    }

    /**
     * Function to convert {@link EsinfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Esinfo> createSpecification(EsinfoCriteria criteria) {
        Specification<Esinfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Esinfo_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Esinfo_.name));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Esinfo_.type));
            }
            if (criteria.getVnode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getVnode(), Esinfo_.vnode));
            }
            if (criteria.getRnode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRnode(), Esinfo_.rnode));
            }
            if (criteria.getDate() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDate(), Esinfo_.date));
            }
            if (criteria.getPname() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPname(), Esinfo_.pname));
            }
        }
        return specification;
    }
}
