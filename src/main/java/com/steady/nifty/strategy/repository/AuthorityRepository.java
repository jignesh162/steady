package com.steady.nifty.strategy.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.Authority;
import com.steady.nifty.strategy.entity.Role;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByAuthority(Role authority);
}
