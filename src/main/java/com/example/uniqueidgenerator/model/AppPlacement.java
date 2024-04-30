package com.example.uniqueidgenerator.model;

import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@ToString
@Getter
public class AppPlacement {

    private int datacenterNum;

    private int podOrdinal;

    public AppPlacement() {
        String datacenterNumString = getFromEnvOrDefault("DATA_CENTER_NUM", "1");
        this.datacenterNum = Integer.parseInt(datacenterNumString);

        String podFullName = getFromEnvOrDefault("POD_NAME_FULL", "id-generator-0");
        String podOrdinalPrefix = getFromEnvOrDefault("POD_ORDINAL_PREFIX", "id-generator-");

        this.podOrdinal = Integer.parseInt(podFullName.substring(podOrdinalPrefix.length()));
    }

    private String getFromEnvOrDefault(String envVarName, String defaultValue) {
        String value = System.getenv(envVarName);

        if (Objects.isNull(value)) {
            log.warn("Unable to find value for env var {}; using default value {}", envVarName, defaultValue);
            return defaultValue;
        }

        return value;
    }

}
