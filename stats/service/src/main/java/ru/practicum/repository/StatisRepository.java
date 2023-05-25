package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ViewStatsDto;
import ru.practicum.model.Hit;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface StatisRepository extends JpaRepository<Hit, Long> {
    @Query("SELECT NEW ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Hit AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.ip, s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getUrisViews(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(s.ip)) " +
            "FROM Hit AS s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN (:uris) " +
            "GROUP BY s.uri, s.ip, s.app " +
            "ORDER BY COUNT(s.ip) DESC")
    List<ViewStatsDto> getUrisViewsFromSet(Set<String> uris, LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Hit s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "GROUP BY s.uri, s.ip, s.app " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> getUrisViewsUnique(LocalDateTime start, LocalDateTime end);

    @Query("SELECT NEW ru.practicum.ViewStatsDto(s.app, s.uri, COUNT(DISTINCT s.ip)) " +
            "FROM Hit s " +
            "WHERE s.timestamp BETWEEN :start AND :end " +
            "AND s.uri IN (:uris) " +
            "GROUP BY s.uri, s.ip, s.app " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC")
    List<ViewStatsDto> getUrisViewsFromSetUnique(Set<String> uris, LocalDateTime start, LocalDateTime end);
}
