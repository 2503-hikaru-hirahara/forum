package com.example.forum.repository;

import com.example.forum.repository.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {
    public List<Report> findAllByOrderByUpdatedDateDesc();
    @Query("SELECT r FROM Report r WHERE r.updatedDate BETWEEN :start AND :end ORDER BY r.updatedDate DESC")
    public List<Report> findByUpdatedDateBetweenOrderByUpdatedDateDesc(
            @Param("start") LocalDateTime startDateTime,
            @Param("end") LocalDateTime endDateTime
    );
}
