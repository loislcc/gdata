package edu.buaa.web.rest;

import com.alibaba.fastjson.JSONObject;
import edu.buaa.domain.Constants;
import edu.buaa.domain.Esinfo;
import edu.buaa.domain.Loginfo;
import edu.buaa.domain.Task;
import edu.buaa.repository.EsinfoRepository;
import edu.buaa.rsutils.jerasure.Decoder;
import edu.buaa.service.EsinfoService;
import edu.buaa.service.TaskService;
import edu.buaa.service.message.ToConsoleProducer;
import edu.buaa.web.rest.errors.BadRequestAlertException;
import edu.buaa.service.dto.EsinfoCriteria;
import edu.buaa.service.EsinfoQueryService;

import edu.buaa.web.rest.util.HeaderUtil;
import edu.buaa.web.rest.util.PaginationUtil;
import edu.buaa.web.rest.util.utils;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link edu.buaa.domain.Esinfo}.
 */
@RestController
@RequestMapping("/api")
public class EsinfoResource {

    private final Logger log = LoggerFactory.getLogger(EsinfoResource.class);

    private static final String ENTITY_NAME = "gdataEsinfo";

    private final EsinfoService esinfoService;

    private final EsinfoQueryService esinfoQueryService;

    private final EsinfoRepository esinfoRepository;

    private final TaskService taskService;

    private final ToConsoleProducer toConsoleProducer;

    public EsinfoResource(EsinfoService esinfoService, EsinfoQueryService esinfoQueryService,
                          EsinfoRepository esinfoRepository,TaskService taskService,ToConsoleProducer toConsoleProducer) {
        this.esinfoService = esinfoService;
        this.esinfoQueryService = esinfoQueryService;
        this.esinfoRepository = esinfoRepository;
        this.taskService = taskService;
        this.toConsoleProducer = toConsoleProducer;
    }

    /**
     * {@code POST  /esinfos} : Create a new esinfo.
     *
     * @param esinfo the esinfo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new esinfo, or with status {@code 400 (Bad Request)} if the esinfo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/esinfos")
    public ResponseEntity<Esinfo> createEsinfo(@RequestBody Esinfo esinfo) throws URISyntaxException {
        log.debug("REST request to save Esinfo : {}", esinfo);
        if (esinfo.getId() != null) {
            throw new BadRequestAlertException("A new esinfo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Esinfo result = esinfoService.save(esinfo);
        return ResponseEntity.created(new URI("/api/esinfos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /esinfos} : Updates an existing esinfo.
     *
     * @param esinfo the esinfo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated esinfo,
     * or with status {@code 400 (Bad Request)} if the esinfo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the esinfo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/esinfos")
    public ResponseEntity<Esinfo> updateEsinfo(@RequestBody Esinfo esinfo) throws URISyntaxException {
        log.debug("REST request to update Esinfo : {}", esinfo);
        if (esinfo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Esinfo result = esinfoService.save(esinfo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert( ENTITY_NAME, esinfo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /esinfos} : get all the esinfos.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of esinfos in body.
     */
    @GetMapping("/esinfos")
    public ResponseEntity<List<Esinfo>> getAllEsinfos(EsinfoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Esinfos by criteria: {}", criteria);
        Page<Esinfo> page = esinfoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders( page,"/api/esinfos");
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /esinfos/count} : count all the esinfos.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/esinfos/count")
    public ResponseEntity<Long> countEsinfos(EsinfoCriteria criteria) {
        log.debug("REST request to count Esinfos by criteria: {}", criteria);
        return ResponseEntity.ok().body(esinfoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /esinfos/:id} : get the "id" esinfo.
     *
     * @param id the id of the esinfo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the esinfo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/esinfos/{id}")
    public ResponseEntity<Esinfo> getEsinfo(@PathVariable Long id) {
        log.debug("REST request to get Esinfo : {}", id);
        Optional<Esinfo> esinfo = esinfoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(esinfo);
    }

    /**
     * {@code DELETE  /esinfos/:id} : delete the "id" esinfo.
     *
     * @param id the id of the esinfo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/esinfos/{id}")
    public ResponseEntity<Void> deleteEsinfo(@PathVariable Long id) {
        log.debug("REST request to delete Esinfo : {}", id);
        esinfoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    @PostMapping("/esinfos/findByname")
    public List<Esinfo> findEsinfo(@RequestBody String pname)  {
        log.debug("REST request to find Esinfo bypname : {}", pname);
        Optional<List<Esinfo>> optionalEsinfoList = esinfoRepository.findAllByPname(pname);
        return optionalEsinfoList.orElse(null);
    }

    @PostMapping("/esinfos/processback")
    public ResponseEntity<Void> processback(@RequestBody String pname)  {
        log.debug("REST request to back esinfo : {}", pname);
        Optional<List<Esinfo>> optionalEsinfoList = esinfoRepository.findAllByPname(pname);
        // 从edge获取所有数据块文件
        if(optionalEsinfoList.isPresent()){
            List<Esinfo> esinfos = optionalEsinfoList.get();
            for (Esinfo esinfo: esinfos){
                String rnode = esinfo.getRnode();
                String name = esinfo.getName();
                if(rnode.equals("edge")){
                    esinfoService.getEsFile(name);
                }else {
                    if(rnode.equals("edge2")){
                        esinfoService.getEsFile2(name);
                    }else {
                        if(rnode.equals("edge3")){
                            esinfoService.getEsFile3(name);
                        }
                    }
                }
            }
        }
//         解码工作
        Optional<Task> taskOptional = taskService.findbyName(pname);
        if(taskOptional.isPresent()){
            Task task = taskOptional.get();
            int k= Integer.parseInt(task.getDatanum());
            int m = Integer.parseInt(task.getChecknum());
            int w = Constants.jerasurew;
            String path = Constants.esfilepathtotmp+File.separator+pname+File.separator+pname+".txt";
            Decoder dec = new Decoder(path, k, m, w);
            dec.decode(task.getSize());
            List<Loginfo> res = new ArrayList<>();
            String path2 = path.replace(".txt","back.txt");
            res = utils.FileInputListneed(path2);
            for (Loginfo e:res) {
                toConsoleProducer.sendMsgToGatewayConsole(e.toString());
            }
            toConsoleProducer.sendMsgToGatewayConsole(task.getName() +" Decoding " );
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/esinfos/test")
    public ResponseEntity<JSONObject> test()  {
//        String path = "/Users/lois/Desktop/ErasureCode/2020-10-30/test1.txt";
//            taskService.sendEsFile(path);
        esinfoService.getEsFile("tsk1103_k01");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/esinfos/FiletoGateway")
    public ResponseEntity<JSONObject> postFiletoGateway(@RequestParam("file") MultipartFile files) throws Exception {
        String filename = files.getOriginalFilename();
        String pname = filename.split("_")[0];
        String path = Constants.esfilepathtotmp+File.separator+pname;
        File file = new  File ( path );
        String  pathFile = path + File.separator + filename;
        File  newFile = new  File(pathFile);
        //判断文件夹是否存在，不存在则创建
        if( !file.exists( ) ){
            //创建文件夹
            file.mkdirs();
        }
        try{
            //文件传输到本地
            files.transferTo(newFile);

        }catch(IOException e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
