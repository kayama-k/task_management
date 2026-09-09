package com.taskmanagement.backend;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Matches the existing Node.js backend's health check contract
 * (see docs/requirements.md, 9.1) so the two backends stay
 * interchangeable from the frontend's point of view during migration.
 */
@RestController
public class HealthController {

    @GetMapping("/api/health")
    public Map<String, String> health() {
        return Map.of("status", "ok");
    }
}
