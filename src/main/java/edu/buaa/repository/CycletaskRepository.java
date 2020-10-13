package edu.buaa.repository;
import edu.buaa.domain.Cycletask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Cycletask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CycletaskRepository extends JpaRepository<Cycletask, Long>, JpaSpecificationExecutor<Cycletask> {
        void deleteAllByTaskid(Long taskid);
}
