package edu.buaa.service;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "edge2")
public interface Edge2Client {
}
