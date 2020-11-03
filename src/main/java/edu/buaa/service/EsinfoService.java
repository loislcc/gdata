package edu.buaa.service;

import edu.buaa.domain.Constants;
import edu.buaa.domain.Esinfo;
import edu.buaa.repository.EsinfoRepository;
import edu.buaa.web.rest.util.utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Esinfo}.
 */
@Service
@Transactional
public class EsinfoService {

    private final Logger log = LoggerFactory.getLogger(EsinfoService.class);

    private final EsinfoRepository esinfoRepository;

    private final EdgeClient edgeClient;

    private final Edge2Client edge2Client;

    private final Edge3Client edge3Client;


    public EsinfoService(EsinfoRepository esinfoRepository, EdgeClient edgeClient, Edge2Client edge2Client, Edge3Client edge3Client) {
        this.esinfoRepository = esinfoRepository;
        this.edgeClient = edgeClient;
        this.edge2Client = edge2Client;
        this.edge3Client = edge3Client;
    }

    /**
     * Save a esinfo.
     *
     * @param esinfo the entity to save.
     * @return the persisted entity.
     */
    public Esinfo save(Esinfo esinfo) {
        log.debug("Request to save Esinfo : {}", esinfo);
        return esinfoRepository.save(esinfo);
    }

    /**
     * Get all the esinfos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Esinfo> findAll(Pageable pageable) {
        log.debug("Request to get all Esinfos");
        return esinfoRepository.findAll(pageable);
    }


    /**
     * Get one esinfo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Esinfo> findOne(Long id) {
        log.debug("Request to get Esinfo : {}", id);
        return esinfoRepository.findById(id);
    }

    /**
     * Delete the esinfo by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Esinfo : {}", id);
        esinfoRepository.deleteById(id);
    }

    public void getEsFile(String name)  {
        edgeClient.getFile(name);
    }

    public String storeFile(MultipartFile multipartFile, String pname) {
        String path = Constants.esfilepathtotmp+pname;
        File file = new  File ( path );
        String filename = multipartFile.getOriginalFilename();
        String  pathFile = path + File.separator + filename;
        File  newFile = new  File(pathFile);
        //判断文件夹是否存在，不存在则创建
        if( !file.exists( ) ){
            //创建文件夹
            file.mkdirs();
        }
        try{
            //文件传输到本地
            multipartFile.transferTo(newFile);

        }catch(IOException e){
            e.printStackTrace();
        }
        return pathFile;

    }
}
