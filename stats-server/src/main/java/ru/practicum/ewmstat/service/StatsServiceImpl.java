package ru.practicum.ewmstat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.ewmstat.dto.HitDto;
import ru.practicum.ewmstat.dto.ViewStats;
import ru.practicum.ewmstat.model.Hit;
import ru.practicum.ewmstat.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private final ModelMapper modelMapper;

    @Override
    public void addHit(HitDto hitDto) {
        log.info("Saving statistics date for hit: {}", hitDto);
        Hit hit = modelMapper.map(hitDto, Hit.class);
        statsRepository.save(hit);
    }

    @Override
    public Collection<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Collection<String> uncodedUris, Boolean unique) {
        Collection<String> uris = new ArrayList<>();
        for (String u : uncodedUris) {
            uris.add(URLDecoder.decode(u, StandardCharsets.UTF_8));
        }
        log.info("getting statistics date for uri: {}", uris);
        Collection<Hit> hits = statsRepository.findDistinctHitsByUriInAndTimestampBetween(uris, start, end);
        Collection<ViewStats> viewStats = hits.stream()
                .map(h -> modelMapper.map(h, ViewStats.class))
                .collect(Collectors.toList());
        if (viewStats.isEmpty()) {
            return List.of(new ViewStats("unavailable", "unavailable", 0));
        }
        return viewStats;
    }
}