package com.securin.weatheranalytics.controller;

import com.securin.weatheranalytics.dto.TemperatureStatsDTO;
import com.securin.weatheranalytics.dto.WeatherResponseDTO;
import com.securin.weatheranalytics.service.WeatherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weather")
@RequiredArgsConstructor
@Validated
@Tag(name = "Weather Analytics", description = "Endpoints for weather data management and analytics")
public class WeatherController {

    private final WeatherService service;

    @PostMapping("/load")
    @Operation(summary = "Load weather data from CSV", description = "Parses the testset.csv and saves records to the database")
    public String loadData() {
        service.loadCsvData();
        return "CSV Loaded Successfully!";
    }

    @GetMapping("/date")
    @Operation(summary = "Get weather by specific date", description = "Retrieves weather details for a specific date in YYYY-MM-DD format")
    public List<WeatherResponseDTO> getByDate(
            @Parameter(description = "Date in format YYYY-MM-DD", example = "1996-11-01") @RequestParam @NotBlank String date) {
        return service.getWeatherByDate(date);
    }

    @GetMapping("/month")
    @Operation(summary = "Get weather by month", description = "Retrieves weather details for a specific month across all years")
    public List<WeatherResponseDTO> getByMonth(
            @Parameter(description = "Month of the year (1-12)", example = "11") @RequestParam @Min(1) @Max(12) int month) {
        return service.getWeatherByMonth(month);
    }

    @GetMapping("/stats")
    @Operation(summary = "Get monthly temperature stats for a year", description = "Provides high, min, and median temperatures for each month of a given year")
    public List<TemperatureStatsDTO> getMonthlyStats(
            @Parameter(description = "Year for statistics", example = "1996") @RequestParam int year) {
        return service.getMonthlyStats(year);
    }
}