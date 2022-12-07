package ru.practicum.ewmmain.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.compilation.dto.CompilationDto;
import ru.practicum.ewmmain.compilation.dto.NewCompilationDto;
import ru.practicum.ewmmain.compilation.model.Compilation;
import ru.practicum.ewmmain.compilation.repository.CompilationRepository;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.repository.EventRepository;
import ru.practicum.ewmmain.exception.BadRequestException;
import ru.practicum.ewmmain.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CompilationDto> getAll(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from, size, Sort.by("id"));
        Collection<Compilation> compilations = compilationRepository.findAll(pageable).getContent();
        return compilations.stream()
                .map(compilation -> modelMapper.map(compilation, CompilationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationDtoById(Long id) {
        log.info("Получение подборки по id {}", id);
        Compilation compilation = compilationRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id %s has not been found", id)));
        return modelMapper.map(compilation, CompilationDto.class);
    }

    @Override
    @Transactional
    public CompilationDto add(NewCompilationDto newCompilationDto) {
        log.info("Добавление подборки: {}", newCompilationDto);
        Compilation compilation = modelMapper.map(newCompilationDto, Compilation.class);
        Collection<Long> eventsList = newCompilationDto.getEvents();
        Collection<Event> events = new ArrayList<>();
        for (Long eventId : eventsList) {
            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
            events.add(event);
        }
        compilation.setEvents(events);
        if (compilation.getTitle() == null || compilation.getTitle().isBlank()) {
            throw new BadRequestException("Пустое название подборки");
        }
        compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationDto.class);
    }

    @Override
    @Transactional
    public CompilationDto addEventToCompilation(Long compilationId, Long eventId) {
        log.info("Обновление события с id {} в подборке с id {}", eventId, compilationId);
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("В БД нет подборки с id" + compilationId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        Collection<Event> events = compilation.getEvents();
        events.add(event);
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationDto.class);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compilationId) {
        log.info("Удаление подборки с id {}", compilationId);
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("В БД нет подборки с id" + compilationId));
        compilationRepository.delete(compilation);
        log.info("Подборка с id {} удалена", compilationId);
    }

    @Override
    @Transactional
    public CompilationDto pinCompilation(Long compilationId) {
        log.info("Закрепление подборки с id {}", compilationId);
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("В БД нет подборки с id" + compilationId));
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        return modelMapper.map(compilation, CompilationDto.class);

    }

    @Override
    @Transactional
    public void deletePinCompilation(Long compilationId) {
        log.info("Открепление подборки id {}", compilationId);
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("В БД нет подборки с id" + compilationId));
        compilation.setPinned(false);
        compilationRepository.save(compilation);
    }

    @Override
    @Transactional
    public void deleteEventFromCompilation(Long compilationId, Long eventId) {
        log.info("Удаление события id {} из подборки id {}", eventId, compilationId);
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("В БД нет подборки с id" + compilationId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        compilation.getEvents().remove(event);
        Collection<Event> events = compilation.getEvents();
        compilation.setEvents(events);
        compilationRepository.save(compilation);
        log.info("Событие с id {} удалено из подборки с id {}", eventId, compilationId);
    }
}
