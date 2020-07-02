package com.steady.nifty.strategy.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.steady.nifty.strategy.entity.Role;
import com.steady.nifty.strategy.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = { "authorities.authority" })
    Optional<User> findByUsername(String username);

    @EntityGraph(attributePaths = { "authorities.authority" })
    Iterable<User> findByAuthorities_Authority(Role authority);

    @EntityGraph(attributePaths = { "authorities.authority" })
    @Override
    List<User> findAll();
}
