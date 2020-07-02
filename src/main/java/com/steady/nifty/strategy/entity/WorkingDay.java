package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Table(schema = "steady", name = "working_day")
@Getter
@EqualsAndHashCode
@Data
public class WorkingDay implements Serializable {
    private static final long serialVersionUID = 1L;

    protected WorkingDay() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private LocalDate day;

    @Column(nullable = false)
    private Boolean isExpiryDay;
}
