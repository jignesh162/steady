package com.steady.nifty.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.NiftyOHLC;

@Repository
public interface NiftyOHLCRepository extends JpaRepository<NiftyOHLC, Long> {
    Iterable<NiftyOHLC> findAllByOrderByStartDate();
}
