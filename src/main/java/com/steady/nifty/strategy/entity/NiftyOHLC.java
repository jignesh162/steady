package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(schema = "steady", name = "nifty_ohlc")
@EqualsAndHashCode
public class NiftyOHLC implements Serializable {

    private static final long serialVersionUID = 1L;

    public NiftyOHLC() {
        // no-args constructor required by JPA spec
    }

    @Schema(description = "Nifty OHLC ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Update.class)
    private Integer id;

    @Schema(description = "Open price", example = "11111.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal open;

    @Schema(description = "Highest price", example = "2222.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal high;

    @Schema(description = "Lowest price", example = "9999.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal low;

    @Schema(description = "Close price", example = "1111.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal close;

    @Schema(description = "Time when gone to highest price for the day", example = "2018-01-13 13:05:45")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private LocalDateTime highTime;

    @Schema(description = "Time when gone to lowest price for the day", example = "2018-01-13 15:10:26")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private LocalDateTime lowTime;

    @Schema(description = "Total up from the opening price", example = "50.45")
    @JsonView(Views.Create.class)
    private BigDecimal up;

    @Schema(description = "Total down from the opening price", example = "90.56")
    @JsonView(Views.Create.class)
    private BigDecimal down;

    @Schema(description = "Start date for OHLC data", example = "2018-01-13")
    @JsonView(Views.Create.class)
    private LocalDate startDate;

    @Schema(description = "End date for OHLC data", example = "2018-01-21")
    @JsonView(Views.Create.class)
    private LocalDate endDate;

    @Schema(description = "Is it expiry day?", example = "true/false")
    @JsonView(Views.Create.class)
    private Boolean isExpiryDay;

    @Schema(description = "Which type of OHLC?", example = "D/1W/1M/2M/3M")
    @JsonView(Views.Create.class)
    private String type;
}
