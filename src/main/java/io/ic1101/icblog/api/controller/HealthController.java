package io.ic1101.icblog.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
public class HealthController {

    @GetMapping("/health")
    @Produces(MediaType.TEXT_PLAIN)
    public String getHealth() {
        return "healthy";
    }
}
