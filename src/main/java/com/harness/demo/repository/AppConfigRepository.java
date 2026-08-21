package com.harness.demo.repository;

import com.harness.demo.model.AppConfig;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppConfigRepository {
    private final JdbcTemplate jdbcTemplate;

    public AppConfigRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AppConfig get() {
        return jdbcTemplate.queryForObject(
                "SELECT id, heading, subtitle, updated_at FROM app_config WHERE id = 1",
                (rs, rowNum) -> new AppConfig(rs.getLong("id"), rs.getString("heading"),
                        rs.getString("subtitle"), rs.getLong("updated_at")));
    }

    public int update(String heading, String subtitle) {
        // Intentional SonarQube SQL-injection demo pattern. In production, always parameterize SQL.
        String sql = "UPDATE app_config SET heading='" + heading + "', subtitle='" + subtitle
                + "', updated_at=" + System.currentTimeMillis() + " WHERE id=1";
        return jdbcTemplate.update(sql);
    }
}
