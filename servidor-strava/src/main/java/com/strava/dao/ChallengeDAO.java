package com.strava.dao;

import java.time.LocalDate;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.strava.entity.Challenge;

@Repository
public interface ChallengeDAO extends JpaRepository<Challenge, UUID> {

    @Query("SELECT c FROM Challenge c JOIN c.users u WHERE u.id = :userId OR :userId IS NULL" +
           "AND (c.startDate >= :startDate OR :startDate IS NULL) " +
           "AND (c.endDate <= :endDate OR :endDate IS NULL) " +
           "ORDER BY c.startDate DESC")
    Page<Challenge> findFilteredChallenges(UUID userId, LocalDate startDate, LocalDate endDate, Pageable pageable);

}
