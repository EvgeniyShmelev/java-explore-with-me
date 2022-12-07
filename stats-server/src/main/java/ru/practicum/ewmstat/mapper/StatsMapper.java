package ru.practicum.ewmstat.mapper;

import ru.practicum.ewmstat.dto.ViewStats;
import ru.practicum.ewmstat.model.Hit;

public class StatsMapper {
    public static ViewStats toViewStats(Hit hits, long totalHits) {
        return new ViewStats(
                hits.getApp(),
                hits.getUri(),
                totalHits
        );
    }
}
