package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonView;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Entity
@IdClass(NiftyId.class)
@Table(schema = "steady", name = "vix")
@EqualsAndHashCode
public class VIX implements Serializable {

    private static final long serialVersionUID = 1L;

    protected VIX() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    @Schema(description = "VIX value", example = "25.12")
    @Id
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private BigDecimal value;

    @Schema(description = "Date time for the VIX value tick", example = "2018-01-13 13:05:45")
    @Id
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private LocalDateTime dateTime;
}
