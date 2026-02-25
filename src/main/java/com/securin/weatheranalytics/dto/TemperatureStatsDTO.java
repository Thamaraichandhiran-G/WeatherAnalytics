package com.securin.weatheranalytics.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TemperatureStatsDTO {

    private int year;
    private int month;
    private double minTemperature;
    private double maxTemperature;
    private double medianTemperature;
}