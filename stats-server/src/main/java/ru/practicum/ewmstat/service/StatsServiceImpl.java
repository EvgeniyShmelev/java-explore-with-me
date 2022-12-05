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
        log.info("Сохранение данных статистки показов: {}", hitDto);
        Hit hit = modelMapper.map(hitDto, Hit.class);
        log.info("Маппинг DTO: {}", hit);
        statsRepository.save(hit);
    }

    @Override
    public Collection<ViewStats> getStats(LocalDateTime start, LocalDateTime end,
                                          Collection<String> encodedUris, Boolean unique) {
        Collection<String> uris = new ArrayList<>();
        for (String u : encodedUris) {
            uris.add(URLDecoder.decode(u, StandardCharsets.UTF_8));
        }
        Collection<Hit> hits1 = statsRepository.findAll();
        for (Hit hit : hits1) {
            log.info("Что внутри просмотра: {}", hit);
        }
        log.info("Получение данных статистики для uri: {}", uris);
        Collection<Hit> hits = statsRepository.findDistinctStatByUriInAndTimestampBetween(uris, start, end);
        log.info("Количество просмотров: {}", hits.size());
        log.info("Количество всех просмотров: {}", statsRepository.findAll().size());
        Collection<ViewStats> viewStats = hits.stream()
                .map(h -> modelMapper.map(h, ViewStats.class))
                .collect(Collectors.toList());
        if (viewStats.isEmpty()) {
            return List.of(new ViewStats("unavailable", "unavailable", 0L));
        }
        return viewStats;
    }
}
