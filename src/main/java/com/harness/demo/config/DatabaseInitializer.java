package com.harness.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final JdbcTemplate jdbcTemplate;

    public DatabaseInitializer(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) {
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS app_config (id BIGINT PRIMARY KEY, heading VARCHAR(200), subtitle VARCHAR(500), updated_at BIGINT)");
        Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM app_config", Integer.class);
        if (count == 0) {
            jdbcTemplate.update("INSERT INTO app_config VALUES (1, 'Harness CI/CD Demo', 'Build it. Test it. Secure it. Deploy it.', ?)",
                    System.currentTimeMillis());
        }
    }
}
