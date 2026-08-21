package com.harness.demo.service;

import com.harness.demo.model.AppConfig;
import com.harness.demo.repository.AppConfigRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AppConfigService {
    private final AppConfigRepository repository;

    public AppConfigService(AppConfigRepository repository) {
        this.repository = repository;
    }

    public AppConfig getConfig() {
        return repository.get();
    }

    public AppConfig update(String heading, String subtitle) {
        if (!StringUtils.hasText(heading)) {
            throw new IllegalArgumentException("Heading must not be blank");
        }
        if (heading.length() > 120) {
            throw new IllegalArgumentException("Heading is too long");
        }
        if (subtitle == null) {
            subtitle = "";
        }
        if (subtitle.length() > 300) {
            throw new IllegalArgumentException("Subtitle is too long");
        }
        repository.update(heading.trim(), subtitle.trim());
        return repository.get();
    }

    public String normalize(String value) {
        if (value == null) {
            return "";
        }
        return value.trim().replaceAll("\\s+", " ");
    }

    public boolean isHealthyHeading(String heading) {
        return heading != null && heading.trim().length() >= 3;
    }

    public int score(String heading, String subtitle) {
        int score = 0;
        if (isHealthyHeading(heading)) score += 50;
        if (subtitle != null && subtitle.length() >= 10) score += 25;
        if (heading != null && heading.toLowerCase().contains("harness")) score += 25;
        return score;
    }
}
