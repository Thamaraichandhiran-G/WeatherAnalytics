package com.securin.weatheranalytics.service;

import com.securin.weatheranalytics.dto.TemperatureStatsDTO;
import com.securin.weatheranalytics.entity.Weather;
import com.securin.weatheranalytics.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherServiceTest {

    @Mock
    private WeatherRepository repository;

    @InjectMocks
    private WeatherService service;

    @Test
    void testGetMonthlyStats_EmptyRecords() {
        int year = 1996;
        // Stub all months to return empty by default
        when(repository.findByYearAndMonth(ArgumentMatchers.eq(year), ArgumentMatchers.anyInt()))
                .thenReturn(Collections.emptyList());

        List<TemperatureStatsDTO> stats = service.getMonthlyStats(year);

        assertTrue(stats.isEmpty(), "Stats should be empty when no records found");
    }

    @Test
    void testGetMonthlyStats_Calculations() {
        int year = 1996;
        int month = 11;

        Weather w1 = new Weather();
        w1.setTemperature(10.0);
        Weather w2 = new Weather();
        w2.setTemperature(20.0);
        Weather w3 = new Weather();
        w3.setTemperature(30.0);

        // Stub the specific month and others
        when(repository.findByYearAndMonth(ArgumentMatchers.eq(year), ArgumentMatchers.anyInt()))
                .thenReturn(Collections.emptyList());
        when(repository.findByYearAndMonth(year, month)).thenReturn(List.of(w1, w2, w3));

        List<TemperatureStatsDTO> stats = service.getMonthlyStats(year);

        assertEquals(1, stats.size());
        TemperatureStatsDTO novStats = stats.get(0);
        assertEquals(10.0, novStats.getMinTemperature());
        assertEquals(30.0, novStats.getMaxTemperature());
        assertEquals(20.0, novStats.getMedianTemperature());
    }

    @Test
    void testGetMonthlyStats_MedianEven() {
        int year = 1996;
        int month = 11;

        Weather w1 = new Weather();
        w1.setTemperature(10.0);
        Weather w2 = new Weather();
        w2.setTemperature(20.0);

        when(repository.findByYearAndMonth(year, month)).thenReturn(List.of(w1, w2));
        for (int m = 1; m <= 12; m++) {
            if (m != month) {
                when(repository.findByYearAndMonth(year, m)).thenReturn(Collections.emptyList());
            }
        }

        List<TemperatureStatsDTO> stats = service.getMonthlyStats(year);

        assertEquals(1, stats.size());
        assertEquals(15.0, stats.get(0).getMedianTemperature());
    }
}
