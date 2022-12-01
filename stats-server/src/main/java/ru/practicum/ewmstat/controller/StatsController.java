package ru.practicum.ewmstat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmstat.dto.HitDto;
import ru.practicum.ewmstat.dto.ViewStats;
import ru.practicum.ewmstat.service.StatsService;

import java.time.LocalDateTime;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
public class StatsController {
    private final StatsService statsService;

    @PostMapping("/hit")
    public void addHit(@RequestBody HitDto dto) {
        log.info("Получен POST запрос для добавления пакета статиски: {}", dto);
        statsService.addHit(dto);
    }

    @GetMapping("/stats")
    public Collection<ViewStats> getStats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                          @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                          @RequestParam Collection<String> uris,
                                          @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Received GET request on /stats");
        return statsService.getStats(start, end, uris, unique);
    }
}
