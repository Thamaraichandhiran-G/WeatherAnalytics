package com.securin.weatheranalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseDTO {
    private LocalDateTime dateTime;
    private String weatherCondition;
    private Double temperature;
    private Double humidity;
    private Double pressure;
}
