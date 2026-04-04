package com.travelDestinationPlanner.fawry.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

import lombok.*;

@Entity
@Table(name = "destinations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Country name must not exceed 100 characters")
    @Column(name = "country_name", nullable = false, length = 100)
    private String countryName;

    @Size(max = 100, message = "Capital must not exceed 100 characters")
    @Column(name = "capital", length = 100)
    private String capital;

    @Size(max = 100, message = "Region must not exceed 100 characters")
    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "population")
    private Long population;

    @Size(max = 50, message = "Currency must not exceed 50 characters")
    @Column(name = "currency", length = 50)
    private String currency;

    @Size(max = 10, message = "Currency symbol must not exceed 10 characters")
    @Column(name = "currency_symbol", length = 10)
    private String currencySymbol;

    @Size(max = 500, message = "Flag URL must not exceed 500 characters")
    @Column(name = "flag_url", length = 500)
    private String flagUrl;

    // @Builder.Default
    @Column(name = "approved")
    private Boolean approved ;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


}
