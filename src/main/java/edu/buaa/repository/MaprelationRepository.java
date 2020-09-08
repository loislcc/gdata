package edu.buaa.repository;
import edu.buaa.domain.Maprelation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Maprelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MaprelationRepository extends JpaRepository<Maprelation, Long>, JpaSpecificationExecutor<Maprelation> {

}
