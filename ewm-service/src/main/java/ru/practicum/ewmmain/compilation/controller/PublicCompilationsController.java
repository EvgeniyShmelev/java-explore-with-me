package ru.practicum.ewmmain.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Collection;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationsController {
    private final CompilationService compilationService;

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        log.info("Получение подборки событий по id {}", compId);
        return compilationService.getCompilationDtoById(compId);
    }

    @GetMapping
    public Collection<CompilationDto> getAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                         @RequestParam(required = false, defaultValue = "0")
                                                         @PositiveOrZero int from,
                                                         @RequestParam(required = false, defaultValue = "10")
                                                         @Positive int size) {
        log.info("Получить подборки with pinned {}, from={}, size={}", pinned, from, size);
        return compilationService.getAll(pinned, from, size);
    }
}
