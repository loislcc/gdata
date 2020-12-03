package edu.buaa.service;

import edu.buaa.domain.Loginfo;
import edu.buaa.repository.LoginfoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Loginfo}.
 */
@Service
@Transactional
public class LoginfoService {

    private final Logger log = LoggerFactory.getLogger(LoginfoService.class);

    private final LoginfoRepository loginfoRepository;

    public LoginfoService(LoginfoRepository loginfoRepository) {
        this.loginfoRepository = loginfoRepository;
    }

    /**
     * Save a loginfo.
     *
     * @param loginfo the entity to save.
     * @return the persisted entity.
     */
    public Loginfo save(Loginfo loginfo) {
        log.debug("Request to save Loginfo : {}", loginfo);
        return loginfoRepository.save(loginfo);
    }

    /**
     * Get all the loginfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Loginfo> findAll(Pageable pageable) {
        log.debug("Request to get all Loginfos");
        return loginfoRepository.findAll(pageable);
    }


    /**
     * Get one loginfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Loginfo> findOne(Long id) {
        log.debug("Request to get Loginfo : {}", id);
        return loginfoRepository.findById(id);
    }

    /**
     * Delete the loginfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Loginfo : {}", id);
        loginfoRepository.deleteById(id);
    }

    public List<Loginfo> findall(String startime, String endtime) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startime = startime+" 00:00:00";
        endtime = endtime+" 00:00:00";

        Date a = sdf.parse(startime);
        Date b = sdf.parse(endtime);

        List<Loginfo> res = new ArrayList<>();
        List<Loginfo> loginfoList =  loginfoRepository.findAll();

        for (Loginfo logs: loginfoList) {
            Date event = sdf.parse(logs.getEventime());
            if(event.compareTo(a)>=0 && event.compareTo(b)<0) {
                res.add(logs);
            }

        }
        return res;
    }


    public Boolean existbyNameandIp(String name, String ip)   {
        return loginfoRepository.existsByIpAndName(ip,name);
    }

}
