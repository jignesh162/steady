package com.steady.nifty.strategy.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.Nifty;

@Repository
public interface NiftyRepository extends JpaRepository<Nifty, Long> {
    @Query("SELECT N FROM Nifty N WHERE N.dateTime BETWEEN :startTime AND :endTime ORDER BY N.dateTime")
    List<Nifty> findNiftyTicksForDay(LocalDateTime startTime, LocalDateTime endTime);
}
