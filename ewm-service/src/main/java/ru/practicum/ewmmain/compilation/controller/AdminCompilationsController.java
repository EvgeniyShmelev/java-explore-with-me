package ru.practicum.ewmmain.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.dto.NewCompilationDto;
import ru.practicum.ewmmain.compilation.service.CompilationService;

@RestController
@RequestMapping(path = "/admin/compilations")
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationsController {
    private final CompilationService compilationService;

    @PostMapping
    public CompilationDto createCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("�������� ����� �������� �������: {}", newCompilationDto);
        return compilationService.add(newCompilationDto);
    }

    @DeleteMapping("/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("������� �������� ������� {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable Long compId,
                                           @PathVariable Long eventId) {
        log.info("������� ������� {} �� �������� ������� {}", eventId, compId);
        compilationService.deleteEventFromCompilation(compId, eventId);
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public CompilationDto addEventToCompilation(@PathVariable Long compId,
                                                @PathVariable Long eventId) {
        log.info("��������� ������� {} � �������� ������� {}", eventId, compId);
        return compilationService.addEventToCompilation(compId, eventId);
    }

    @DeleteMapping("/{compId}/pin")
    public void delPinCompilation(@PathVariable Long compId) {
        log.info("�������� ������� {} ����� � ����������", compId);
        compilationService.deletePinCompilation(compId);
    }

    @PatchMapping("/{compId}/pin")
    public CompilationDto pinCompilation(@PathVariable Long compId) {
        log.info("�������� ������� {} ������������", compId);
        return compilationService.pinCompilation(compId); // � ��� ���������. ��������� ��� � ��� ���� ����������
    }
}
