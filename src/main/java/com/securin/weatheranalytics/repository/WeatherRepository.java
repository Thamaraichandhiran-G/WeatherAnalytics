package com.securin.weatheranalytics.repository;

import com.securin.weatheranalytics.entity.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    // Get by exact date-time
    List<Weather> findByDateTimeBetween(LocalDateTime start, LocalDateTime end);

    // Get records for a specific month
    @Query("SELECT w FROM Weather w WHERE MONTH(w.dateTime) = :month")
    List<Weather> findByMonth(@Param("month") int month);

    // Get records for specific year and month
    @Query("SELECT w FROM Weather w WHERE YEAR(w.dateTime)=:year AND MONTH(w.dateTime)=:month")
    List<Weather> findByYearAndMonth(@Param("year") int year,
                                     @Param("month") int month);
}