package com.steady.nifty.strategy.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Entity
@Table(schema = "steady", name = "authorities")
@Getter
@EqualsAndHashCode
@Data
public class Authority implements Serializable {
    private static final long serialVersionUID = 1L;

    protected Authority() {
        // no-args constructor required by JPA spec
        // this one is protected since it shouldn't be used directly
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Size(max = 50)
    @Column(length = 50, nullable = false)
    private Role authority;
}
