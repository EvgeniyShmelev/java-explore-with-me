package ru.practicum.ewmmain.events.service;

import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.categories.repository.CategoryRepository;
import ru.practicum.ewmmain.client.RestClient;
import ru.practicum.ewmmain.events.dto.*;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.events.model.Hit;
import ru.practicum.ewmmain.events.model.ViewStats;
import ru.practicum.ewmmain.events.repository.EventRepository;
import ru.practicum.ewmmain.exception.BadRequestException;
import ru.practicum.ewmmain.exception.ConflictException;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;
import ru.practicum.ewmmain.request.mapper.RequestMapper;
import ru.practicum.ewmmain.request.model.Request;
import ru.practicum.ewmmain.request.model.RequestStatus;
import ru.practicum.ewmmain.request.repository.RequestRepository;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.users.repository.UserRepository;
import ru.practicum.ewmmain.utill.QPredicates;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.ewmmain.events.model.QEvent.event;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final ModelMapper modelMapper;
    private final RestClient statsClient;


    @Override
    public List<EventFullDto> getAllByAdmin(List<Long> users, List<EventStatus> states,
                                            List<Long> categories, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, int from, int size) {
        log.info("Поиск событий по параметрам: users {}, states {}, categories {}, start {}, " +
                "end {}, ", users, states, categories, rangeStart, rangeEnd);
        Predicate predicate = QPredicates.builder()
                .add(users, event.initiator.id::in)
                .add(states, event.state::in)
                .add(categories, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .buildAnd();
        List<Event> events = eventRepository.findAll(predicate, PageRequest.of(from, size)).getContent();

        log.info("Найденные события {}", events);
        return events.stream()
                .map(this::toFullDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateByAdmin(Long eventId, AdminUpdateEventRequest adminUpdateEventRequest) {
        if (adminUpdateEventRequest == null) {
            throw new IllegalArgumentException("Объект пустой");
        }
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));

        if (adminUpdateEventRequest.getAnnotation() != null) {
            event.setAnnotation(adminUpdateEventRequest.getAnnotation());
        }
        if (adminUpdateEventRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById(adminUpdateEventRequest.getCategory()).get());
        }
        if (adminUpdateEventRequest.getDescription() != null) {
            event.setDescription(adminUpdateEventRequest.getDescription());
        }
        if (adminUpdateEventRequest.getEventDate() != null) {
            event.setEventDate(adminUpdateEventRequest.getEventDate());
        }
        if (adminUpdateEventRequest.getLocation() != null) {
            event.setLocation(adminUpdateEventRequest.getLocation());
        }
        if (adminUpdateEventRequest.getPaid() != null) {
            event.setPaid(adminUpdateEventRequest.getPaid());
        }
        if (adminUpdateEventRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(adminUpdateEventRequest.getParticipantLimit());
        }
        if (adminUpdateEventRequest.getTitle() != null) {
            event.setTitle(adminUpdateEventRequest.getTitle());
        }
        if (adminUpdateEventRequest.getRequestModeration() != null) {
            event.setRequestModeration(adminUpdateEventRequest.getRequestModeration());
        }
        return toFullDto(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventFullDto publishByAdmin(Long eventId) {
        log.info("Публикация события с id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        if (event.getState().equals(EventStatus.PENDING)) {
            event.setState(EventStatus.PUBLISHED);
            event.setRequestModeration(true);
            Event eventSave = eventRepository.save(event);
            log.info("Опубликовано событие с id {}", eventId);
            return toFullDto(eventSave);
        } else if (event.getState().equals(EventStatus.CANCELED)) {
            throw new NotFoundException(
                    "Событие отменено");
        } else {
            throw new NotFoundException(
                    "Событие уже опубликовано");
        }
    }

    @Override
    @Transactional
    public EventFullDto rejectDyAdmin(Long eventId) {
        log.info("Отклонение события id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        event.setState(EventStatus.CANCELED);
        return toFullDto(event);
    }

    @Override
    public List<EventShortDto> getAllEventsByUser(Long id, int from, int size) {
        log.info("Список всех событий пользователя id {}", id);
        Collection<Event> events = eventRepository.findEventsByInitiatorId(id, PageRequest.of(from, size));
        return events.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());

    }

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("В БД нет категории с id " + newEventDto.getCategory()));
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));
        Event newEvent = modelMapper.map(newEventDto, Event.class);
        if (newEvent.getAnnotation() == null || newEvent.getAnnotation().isBlank()) {
            throw new BadRequestException("Нет описания события.");
        }
        if (categoryRepository.findAll().contains(newEvent.getAnnotation())) {
            throw new ConflictException(
                    "Событие " + newEvent.getAnnotation() + " уже существует .");
        }
        newEvent.setCategory(category);
        newEvent.setInitiator(initiator);
        newEvent.setLocation(newEventDto.getLocation());
        newEvent.setState(EventStatus.PENDING);

        Event eventDB = eventRepository.save(newEvent);
        log.info("Создано новое событие: {}", eventDB);
        return toFullDto(eventDB);
    }

    @Override
    @Transactional
    public EventFullDto updateUserEvent(Long userId, UpdateEventRequest updateEventRequest) {
        log.info("Обновление события: {}", updateEventRequest);
        Event updateEvent = eventRepository.findById(updateEventRequest.getEventId())
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + updateEventRequest.getEventId()));
        Category category = categoryRepository.findById(updateEventRequest.getCategory())
                .orElseThrow(() -> new NotFoundException("В БД нет категории с id " + updateEventRequest.getCategory()));
        updateEvent.setCategory(category);
        if (updateEventRequest.getAnnotation() != null) {
            updateEvent.setAnnotation(updateEventRequest.getAnnotation());
        }
        if (updateEventRequest.getDescription() != null) {
            updateEvent.setDescription(updateEventRequest.getDescription());
        }
        if (updateEventRequest.getEventDate() != null) {
            updateEvent.setEventDate(updateEventRequest.getEventDate());
        }
        if (updateEventRequest.getPaid() != null) {
            updateEvent.setPaid(updateEventRequest.getPaid());
        }
        if (updateEventRequest.getParticipantLimit() != null) {
            updateEvent.setParticipantLimit(updateEventRequest.getParticipantLimit());
        }
        if (updateEventRequest.getTitle() != null) {
            updateEvent.setTitle(updateEventRequest.getTitle());
        }

        Event eventDB = eventRepository.save(updateEvent);
        return modelMapper.map(eventDB, EventFullDto.class);
    }

    @Override
    public EventFullDto getUserEvent(Long userId, Long eventId) {
        log.info("Получить событие {}, добавленное пользователем: {}", eventId, userId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        return toFullDto(event);
    }

    @Override
    @Transactional
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        log.info("Отмена события с id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        return toFullDto(eventDB);
    }

    @Override
    public List<ParticipantRequestDto> getEventRequests(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));

        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));

        Collection<Request> requests = requestRepository.getAllByEventId(eventId);
        return requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ParticipantRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        log.info("Получение всех подтвержденных запросов пользователя с id {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("В БД нет запроса с id " + requestId));
        request.setStatus(RequestStatus.CONFIRMED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public ParticipantRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("В БД нет запроса с id " + requestId));
        request.setStatus(RequestStatus.REJECTED);
        log.info("Изменение статуса события на {}, для запроса с id: {}", request.getStatus(), requestId);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    @Transactional
    public EventFullDto getShortDtoById(Long eventId, HttpServletRequest request) {
        log.info("Получение события с id {}", eventId);
        statsClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        );
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        return toFullDto(event);
    }

    @Override
    @Transactional
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      boolean onlyAvailable, String sort,
                                      Integer from, Integer size, HttpServletRequest request) {
        statsClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        );

        Predicate predicate = QPredicates.builder()
                .add(text, txt -> event.annotation.containsIgnoreCase(txt)
                        .or(event.description.containsIgnoreCase(txt)))
                .add(categories, event.category.id::in)
                .add(rangeStart, event.eventDate::after)
                .add(rangeEnd, event.eventDate::before)
                .add(paid, event.paid::eq)
                .buildAnd();
        List<Event> events = eventRepository.findAll(predicate, PageRequest.of(from, size)).getContent();
        if (onlyAvailable) {
            events = events.stream()
                    .filter(e -> (e.getParticipantLimit() >
                            requestRepository.findByEventIdAndStatus(e.getId(), RequestStatus.CONFIRMED).size()))
                    .collect(Collectors.toList());
        }
        return events.stream()
                .map(this::toShortDto)
                .collect(Collectors.toList());
    }

    private EventShortDto toShortDto(Event event) {
        log.info("Конвертация события event: {} в формат EventShortDto", event);
        long confirmedRequests = requestRepository.findByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED).size();
        ViewStats viewStats = statsClient.getStats(
                event.getId().intValue(),
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1));
        EventShortDto eventShortDto = modelMapper.map(event, EventShortDto.class);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        eventShortDto.setViews(viewStats.getHits());
        return eventShortDto;
    }

    private EventFullDto toFullDto(Event event) {
        log.info("Конвертация события event: {} в формат EventFullDto", event);
        long confirmedRequests = requestRepository.findByEventIdAndStatus(event.getId(), RequestStatus.CONFIRMED).size();
        ViewStats viewStats = statsClient.getStats(
                event.getId().intValue(),
                LocalDateTime.now().minusMonths(1),
                LocalDateTime.now().plusMonths(1));
        EventFullDto eventFullDto = modelMapper.map(event, EventFullDto.class);
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(viewStats.getHits());
        return eventFullDto;
    }
}
