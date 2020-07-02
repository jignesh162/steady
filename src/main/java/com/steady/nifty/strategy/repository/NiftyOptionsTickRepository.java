package com.steady.nifty.strategy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.OptionsTick;

@Repository
public interface NiftyOptionsTickRepository extends JpaRepository<OptionsTick, Long> {
    @Query("SELECT OT FROM OptionsTick OT WHERE OT.dateTime BETWEEN :startDateTime AND :endDateTime AND OT.optionsId = :optionsId")
    List<OptionsTick> findByOptionsId(LocalDateTime startDateTime, LocalDateTime endDateTime, Integer optionsId);
}
