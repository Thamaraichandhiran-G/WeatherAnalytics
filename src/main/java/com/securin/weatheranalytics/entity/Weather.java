package com.securin.weatheranalytics.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "weather", indexes = {
        @Index(name = "idx_datetime", columnList = "dateTime")
})
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    private String weatherCondition;
    private Double dewPoint;
    private Integer fog;
    private Integer hail;
    private Double heatIndex;
    private Double humidity;
    private Double precipitation;
    private Double pressure;
    private Integer rain;
    private Integer snow;
    private Double temperature;
    private Integer thunder;
    private Integer tornado;
    private Double visibility;
    private Double windDirectionDegrees;
    private String windDirection;
    private Double windGust;
    private Double windChill;
    private Double windSpeed;
}
