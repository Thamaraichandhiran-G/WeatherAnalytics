package com.securin.weatheranalytics.service;

import com.securin.weatheranalytics.dto.TemperatureStatsDTO;
import com.securin.weatheranalytics.dto.WeatherResponseDTO;
import com.securin.weatheranalytics.entity.Weather;
import com.securin.weatheranalytics.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherService {

    private final WeatherRepository repository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd-HH:mm");

    public void loadCsvData() {
        log.info("Starting CSV data load from resources/testset.csv");
        try (InputStream is = getClass().getResourceAsStream("/testset.csv")) {
            if (is == null) {
                log.error("testset.csv not found in resources!");
                return;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
                String line = br.readLine(); // Skip header

                List<Weather> batch = new ArrayList<>();
                int batchSize = 1000;
                int totalCount = 0;

                while ((line = br.readLine()) != null) {

                    String[] data = line.split(",", -1); // Use -1 to keep empty trailing fields

                    if (data.length < 20)
                        continue;

                    Weather weather = new Weather();

                    weather.setDateTime(LocalDateTime.parse(data[0], FORMATTER));
                    weather.setWeatherCondition(data[1]);
                    weather.setDewPoint(parseDouble(data[2]));
                    weather.setFog(parseInt(data[3]));
                    weather.setHail(parseInt(data[4]));
                    weather.setHeatIndex(parseDouble(data[5]));
                    weather.setHumidity(parseDouble(data[6]));
                    weather.setPrecipitation(parseDouble(data[7]));
                    weather.setPressure(parseDouble(data[8]));
                    weather.setRain(parseInt(data[9]));
                    weather.setSnow(parseInt(data[10]));
                    weather.setTemperature(parseDouble(data[11]));
                    weather.setThunder(parseInt(data[12]));
                    weather.setTornado(parseInt(data[13]));
                    weather.setVisibility(parseDouble(data[14]));
                    weather.setWindDirectionDegrees(parseDouble(data[15]));
                    weather.setWindDirection(data[16]);
                    weather.setWindGust(parseDouble(data[17]));
                    weather.setWindChill(parseDouble(data[18]));
                    weather.setWindSpeed(parseDouble(data[19]));

                    batch.add(weather);
                    totalCount++;

                    if (batch.size() == batchSize) {
                        repository.saveAll(batch);
                        batch.clear();
                    }
                }

                if (!batch.isEmpty()) {
                    repository.saveAll(batch);
                }
                log.info("Successfully loaded {} weather records.", totalCount);
            }
        } catch (Exception e) {
            log.error("Error occurred while loading CSV data: {}", e.getMessage(), e);
        }
    }

    private Double parseDouble(String value) {
        if (value == null || value.isBlank() || value.equals("-9999"))
            return null;

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        if (value == null || value.isBlank() || value.equals("-9999"))
            return null;

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // 1️⃣ Get weather by specific date (whole day)
    public List<WeatherResponseDTO> getWeatherByDate(String date) {
        log.info("Fetching weather details for date: {}", date);
        LocalDateTime start = LocalDateTime.parse(date + "T00:00:00");
        LocalDateTime end = LocalDateTime.parse(date + "T23:59:59");

        return repository.findByDateTimeBetween(start, end).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // 2️⃣ Get weather by month (across 20 years)
    public List<WeatherResponseDTO> getWeatherByMonth(int month) {
        log.info("Fetching weather details for month: {}", month);
        return repository.findByMonth(month).stream()
                .map(this::mapToResponseDTO)
                .toList();
    }

    // 3️⃣ Get High, Min, Median temperature per month of given year
    public List<TemperatureStatsDTO> getMonthlyStats(int year) {
        log.info("Calculating temperature stats for year: {}", year);
        List<TemperatureStatsDTO> result = new ArrayList<>();

        for (int month = 1; month <= 12; month++) {

            List<Weather> records = repository.findByYearAndMonth(year, month);

            List<Double> temps = records.stream()
                    .map(Weather::getTemperature)
                    .filter(Objects::nonNull)
                    .sorted()
                    .toList();

            if (temps.isEmpty())
                continue;

            double min = temps.get(0);
            double max = temps.get(temps.size() - 1);
            double median;

            int size = temps.size();
            if (size % 2 == 0) {
                median = (temps.get(size / 2 - 1) + temps.get(size / 2)) / 2;
            } else {
                median = temps.get(size / 2);
            }

            result.add(new TemperatureStatsDTO(year, month, min, max, median));
        }

        return result;
    }

    private WeatherResponseDTO mapToResponseDTO(Weather weather) {
        return WeatherResponseDTO.builder()
                .dateTime(weather.getDateTime())
                .weatherCondition(weather.getWeatherCondition())
                .temperature(weather.getTemperature())
                .humidity(weather.getHumidity())
                .pressure(weather.getPressure())
                .build();
    }
}