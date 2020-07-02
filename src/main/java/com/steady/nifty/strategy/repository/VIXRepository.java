package com.steady.nifty.strategy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.VIX;

@Repository
public interface VIXRepository extends JpaRepository<VIX, Long> {
}
