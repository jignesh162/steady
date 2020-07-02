package com.steady.nifty.strategy.repository;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.WorkingDay;

@Repository
public interface WorkingDayRepository extends JpaRepository<WorkingDay, Long> {
    Iterable<WorkingDay> findAllByOrderByDay();
    
    @Query("SELECT W FROM WorkingDay W WHERE W.isExpiryDay = TRUE ORDER BY W.day")
    Iterable<WorkingDay> findAllExpiryDaysOrderByDay();

    @Query(value = "SELECT * from (SELECT MAX(day) as eDay FROM steady.working_day WHERE is_expiry_day = TRUE GROUP BY EXTRACT(MONTH FROM day)) e ORDER BY e.eDay",
    nativeQuery = true)
    Iterable<Date> findMonthEndExpiryDaysGroupByMonth();

    @Query(value = "SELECT MAX(w.day) FROM steady.working_day w WHERE w.is_expiry_day = TRUE and EXTRACT(MONTH FROM w.day) = :month and EXTRACT(YEAR FROM w.day) = :year",
    nativeQuery = true)
    LocalDate findMonthEndExpiryByMonthAndYear(int month, int year);
}
