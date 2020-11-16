package edu.buaa.web.rest;

import edu.buaa.service.Edge2Client;
import edu.buaa.service.Edge3Client;
import edu.buaa.service.EdgeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FileResource {

    private final Logger log = LoggerFactory.getLogger(FileResource.class);

    private EdgeClient edgeClient;
    private Edge2Client edge2Client;
    private Edge3Client edge3Client;

    public FileResource(EdgeClient edgeClient,Edge2Client edge2Client, Edge3Client edge3Client) {
        this.edgeClient = edgeClient;
        this.edge2Client = edge2Client;
        this.edge3Client = edge3Client;
    }

    @PostMapping(value = "/importimage", consumes = "multipart/form-data")
    public ResponseEntity<String> importTopology (MultipartHttpServletRequest request){
        log.debug("REST request to upload files");

        MultipartFile multipartFile = request.getFile("uploadfile");
        //判断文件是否为空
        if (multipartFile == null) {
            log.error("null multipart");
            return new ResponseEntity<String>("", HttpStatus.BAD_REQUEST);
        }
        //获取文件名
        String name = multipartFile.getOriginalFilename();
        //进一步判断文件是否为空（即判断其大小是否为0或其名称是否为null）
        long size = multipartFile.getSize();
        if (name == null || ("").equals(name) || size == 0) {
            log.error("name error multipart");
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
        edgeClient.PostFileforimg(multipartFile);
//        edge2Client.PostFileforimg(multipartFile);
//        edge3Client.PostFileforimg(multipartFile);
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
