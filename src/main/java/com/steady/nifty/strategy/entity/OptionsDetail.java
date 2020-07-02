package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.time.LocalDate;

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
@Table(schema = "steady", name = "options_details")
@EqualsAndHashCode
public class OptionsDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    protected OptionsDetail() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    @Schema(description = "Options contract details ID", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Update.class)
    private Integer id;

    @Schema(description = "Expiry date for the contract", example = "2018-01-21")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private LocalDate expiryDate;

    @Schema(description = "Strike price", example = "11250")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private Integer strike;

    @Schema(description = "Option type", example = "CE/PE")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private String option;

    @Schema(description = "Options's name", example = "NIFTY")
    @Column(nullable = false)
    @JsonView(Views.Create.class)
    private String name;
}
