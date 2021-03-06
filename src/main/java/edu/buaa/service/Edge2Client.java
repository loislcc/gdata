package edu.buaa.service;

import com.alibaba.fastjson.JSONObject;
import edu.buaa.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "edge2",configuration = FeignConfig.class)
public interface Edge2Client {
    @RequestMapping(value = "/api/PostFile",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JSONObject> PostFile(@RequestPart("file") MultipartFile files);

    @RequestMapping(value = "/api/getFile",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JSONObject> getFile(@RequestParam("name") String name);

    @RequestMapping(value = "/api/PostFileforimg",method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<JSONObject> PostFileforimg(@RequestPart("uploadfile") MultipartFile files);
}
