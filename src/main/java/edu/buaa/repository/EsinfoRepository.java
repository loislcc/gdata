package edu.buaa.repository;
import edu.buaa.domain.Esinfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Esinfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsinfoRepository extends JpaRepository<Esinfo, Long>, JpaSpecificationExecutor<Esinfo> {

}
