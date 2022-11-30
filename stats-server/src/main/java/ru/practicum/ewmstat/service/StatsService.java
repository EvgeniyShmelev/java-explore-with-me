package ru.practicum.ewmstat.service;

import ru.practicum.ewmstat.dto.HitDto;
import ru.practicum.ewmstat.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface StatsService {

    void addHit(HitDto dto);

    Collection<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uris, Boolean unique);
}
