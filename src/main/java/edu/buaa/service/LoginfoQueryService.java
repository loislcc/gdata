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

import edu.buaa.domain.Loginfo;
import edu.buaa.domain.*; // for static metamodels
import edu.buaa.repository.LoginfoRepository;
import edu.buaa.service.dto.LoginfoCriteria;

/**
 * Service for executing complex queries for {@link Loginfo} entities in the database.
 * The main input is a {@link LoginfoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Loginfo} or a {@link Page} of {@link Loginfo} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoginfoQueryService extends QueryService<Loginfo> {

    private final Logger log = LoggerFactory.getLogger(LoginfoQueryService.class);

    private final LoginfoRepository loginfoRepository;

    public LoginfoQueryService(LoginfoRepository loginfoRepository) {
        this.loginfoRepository = loginfoRepository;
    }

    /**
     * Return a {@link List} of {@link Loginfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Loginfo> findByCriteria(LoginfoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Loginfo> specification = createSpecification(criteria);
        return loginfoRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Loginfo} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Loginfo> findByCriteria(LoginfoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Loginfo> specification = createSpecification(criteria);
        return loginfoRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoginfoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Loginfo> specification = createSpecification(criteria);
        return loginfoRepository.count(specification);
    }

    /**
     * Function to convert {@link LoginfoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Loginfo> createSpecification(LoginfoCriteria criteria) {
        Specification<Loginfo> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Loginfo_.id));
            }
            if (criteria.getIp() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIp(), Loginfo_.ip));
            }
            if (criteria.getType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getType(), Loginfo_.type));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Loginfo_.name));
            }
            if (criteria.getX() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getX(), Loginfo_.x));
            }
            if (criteria.getY() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getY(), Loginfo_.y));
            }
            if (criteria.getEventime() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEventime(), Loginfo_.eventime));
            }
            if (criteria.getNote() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNote(), Loginfo_.note));
            }
            if (criteria.getOwner() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOwner(), Loginfo_.owner));
            }
            if (criteria.getLevel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLevel(), Loginfo_.level));
            }
        }
        return specification;
    }
}
