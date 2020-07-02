package com.steady.nifty.strategy.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class NiftyId implements Serializable {
    private static final long serialVersionUID = 1L;

    private BigDecimal value;
    private LocalDateTime dateTime;
}
