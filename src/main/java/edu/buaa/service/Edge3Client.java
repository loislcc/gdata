package edu.buaa.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "edge3")
public interface Edge3Client {
}
