package com.taskmanagement.backend;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Single entry point for confirming the backend is up: GET / (this
 * replaces the earlier separate /api/health and /actuator/health
 * endpoints, which aren't needed at this stage).
 */
@RestController
public class RootController {

    @GetMapping("/")
    public Map<String, String> root() {
        return Map.of("status", "ok");
    }
}
