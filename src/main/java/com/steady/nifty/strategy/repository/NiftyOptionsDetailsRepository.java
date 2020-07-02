package com.steady.nifty.strategy.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.OptionsDetail;

@Repository
public interface NiftyOptionsDetailsRepository extends JpaRepository<OptionsDetail, Long> {
    OptionsDetail findByExpiryDateAndStrikeAndOption(LocalDate expiryDate, Integer strike, String option);
}
