package edu.buaa.repository;
import edu.buaa.domain.Maprelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;


/**
 * Spring Data  repository for the Maprelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaprelationRepository extends JpaRepository<Maprelation, Long>, JpaSpecificationExecutor<Maprelation> {

}
