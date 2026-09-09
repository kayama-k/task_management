package com.taskmanagement.backend;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;

/**
 * There is intentionally no real datasource wired up yet: the target
 * database (PostgreSQL, per docs/requirements.md 10.3) hasn't been set up
 * at this scaffold stage, and using H2 as a stand-in would make
 * /actuator/health falsely report the database as healthy. This reports
 * it honestly as DOWN instead, so the health check reflects reality.
 */
@Component
public class DatabaseHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        return Health.down()
                .withDetail("reason", "PostgreSQL is not set up yet (scaffold stage); no datasource is configured")
                .build();
    }
}
