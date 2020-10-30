package edu.buaa.repository;
import edu.buaa.domain.Loginfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Loginfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoginfoRepository extends JpaRepository<Loginfo, Long>, JpaSpecificationExecutor<Loginfo> {
    Boolean existsByIpAndType(String ip,String type);
    Loginfo findByIpAndType(String ip,String type);
    Boolean existsByTypeAndName(String type, String name);
    Loginfo findByTypeAndName(String type, String name);

}
