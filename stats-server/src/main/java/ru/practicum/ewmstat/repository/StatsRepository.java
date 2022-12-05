package ru.practicum.ewmstat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmstat.model.Hit;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsRepository extends JpaRepository<Hit, Long> {

    Collection<Hit> findDistinctStatByUriInAndTimestampBetween(Collection<String> uri, LocalDateTime start, LocalDateTime end);

}