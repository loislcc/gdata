package edu.buaa.repository;
import edu.buaa.domain.Esinfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Esinfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EsinfoRepository extends JpaRepository<Esinfo, Long>, JpaSpecificationExecutor<Esinfo> {
    Optional<List<Esinfo>> findAllByPname(String pname);

}
