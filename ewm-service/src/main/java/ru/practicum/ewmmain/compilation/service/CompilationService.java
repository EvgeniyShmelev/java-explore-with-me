package ru.practicum.ewmmain.compilation.service;

import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {

    List<CompilationDto> getAll(Boolean pinned, int from, int size);

    CompilationDto getCompilationDtoById(Long id);

    CompilationDto add(NewCompilationDto newCompilationDto);

    CompilationDto addEventToCompilation(Long compilationId, Long eventId);

    void deleteCompilation(Long id);

    CompilationDto pinCompilation(Long id);

    void deletePinCompilation(Long id);

    void deleteEventFromCompilation(Long compilationId, Long eventId);
}