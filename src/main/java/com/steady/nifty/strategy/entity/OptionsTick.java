package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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

@Getter
@Entity
@Table(schema = "steady", name = "options_tick")
@EqualsAndHashCode
public class OptionsTick implements Serializable {

    private static final long serialVersionUID = 1L;

    public OptionsTick() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    @Schema(description = "Options tick ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Update.class)
    private Integer id;

    @Schema(description = "Tick date time", example = "2018-01-13 15:10:26")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private LocalDateTime dateTime;

    @Schema(description = "Last traded price", example = "120.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal ltp;

    @Schema(description = "Buy price", example = "121.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal buyprice;

    @Schema(description = "Buy quantity", example = "300")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private Integer buyqty;

    @Schema(description = "Sell price", example = "122.25")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal sellprice;

    @Schema(description = "Sell quantity", example = "300")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private Integer sellqty;

    @Schema(description = "Last traded quantity", example = "150")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private Integer ltq;

    @Schema(description = "Open interest", example = "750000")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private Integer openinterest;

    @Schema(description = "Options ID", example = "5")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private Integer optionsId;
}
