package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;


public interface StatsRepository extends JpaRepository<EndpointHit, Long> {

    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri,count(distinct hit.ip))" +
            "from EndpointHit hit " +
            "where hit.timestamp >= :start " +
            "and hit.timestamp <= :end " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStats> getStatsUnique(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end,
                                   @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit hit " +
            "where hit.timestamp >= :start " +
            "and hit.timestamp <= :end " +
            "and hit.uri IN (:uris) " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStats> getStatsNotUnique(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end,
                                      @Param("uris") List<String> uris);

    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri,count(distinct hit.ip))" +
            "from EndpointHit hit " +
            "where hit.timestamp >= :start " +
            "and hit.timestamp <= :end " +
            "group by hit.app, hit.uri " +
            "order by count(distinct hit.ip) desc")
    List<ViewStats> getStatsUniqueNoUris(@Param("start") LocalDateTime start,
                                   @Param("end") LocalDateTime end);

    @Query("select new ru.practicum.model.ViewStats(hit.app, hit.uri, count(hit.ip)) " +
            "from EndpointHit hit " +
            "where hit.timestamp >= :start " +
            "and hit.timestamp <= :end " +
            "group by hit.app, hit.uri " +
            "order by count(hit.ip) desc")
    List<ViewStats> getStatsNotUniqueNoUris(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);
}
