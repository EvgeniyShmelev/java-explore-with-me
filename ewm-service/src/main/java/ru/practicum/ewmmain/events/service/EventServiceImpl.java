package ru.practicum.ewmmain.events.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.categories.repository.CategoryRepository;
import ru.practicum.ewmmain.client.RestClient;
import ru.practicum.ewmmain.events.dto.*;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.events.model.Hit;
import ru.practicum.ewmmain.events.repository.EventRepository;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;
import ru.practicum.ewmmain.request.model.Request;
import ru.practicum.ewmmain.request.model.RequestStatus;
import ru.practicum.ewmmain.request.repository.RequestRepository;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.users.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<EventFullDto> getAllByAdmin(List<Long> users, List<String> states,
                                            List<Long> categories, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, int from, int size) {

        Pageable pageable = PageRequest.of(from, size, Sort.by("id"));

        ArrayList<Category> categoryEntities = new ArrayList<>();
        for (Long catId : categories) {
            Category category = categoryRepository.findById(catId)
                    .orElseThrow(() -> new NotFoundException("В БД нет категории с id " + catId));
            categoryEntities.add(category);
        }

        ArrayList<User> userEntities = new ArrayList<>();
        for (Long userId : users) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));
            userEntities.add(user);
        }

        ArrayList<EventStatus> statesEnum = new ArrayList<>();
        if (states != null) {
            for (String state : states) {
                EventStatus eventStatus = EventStatus.valueOf(state);
                statesEnum.add(eventStatus);
            }
        } else {
            statesEnum.addAll(Arrays.asList(EventStatus.values()));
        }

        List<Event> events = eventRepository.findEventsByInitiatorIdIn(
                userEntities,
                statesEnum,
                categoryEntities,
                rangeStart,
                rangeEnd,
                pageable
        );

        return events.stream()
                .map(event -> modelMapper.map(event, EventFullDto.class))
                .collect(Collectors.toList());
    }

    @Override
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
            event.setLat(adminUpdateEventRequest.getLocation().getLat());
            event.setLon(adminUpdateEventRequest.getLocation().getLat());
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
        return modelMapper.map(eventRepository.save(event), EventFullDto.class);
    }

    @Override
    public EventFullDto publishByAdmin(Long eventId) {
        log.info("Публикация события с id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventStatus.PUBLISHED);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto rejectDyAdmin(Long eventId) {
        log.info("Отклонение события id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        event.setState(EventStatus.CANCELED);
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public List<EventShortDto> getAllEventsByUser(Long id, int from, int size) {
        log.info("Список всех событий пользователя id {}", id);
        Collection<Event> events = eventRepository.findEventsByInitiatorId(id, PageRequest.of(from, size));
        return events.stream()
                .map(event -> modelMapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto newEventDto) {
        log.info("Создать новое событие: {}", newEventDto);
        Category category = categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("В БД нет категории с id " + newEventDto.getCategory()));
        User initiator = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));
        Event newEvent = modelMapper.map(newEventDto, Event.class);
        newEvent.setCategory(category);
        newEvent.setInitiator(initiator);
        newEvent.setLon(newEventDto.getLocation().getLon());
        newEvent.setLat(newEventDto.getLocation().getLat());
        newEvent.setState(EventStatus.PENDING);

        Event eventDB = eventRepository.save(newEvent);
        return modelMapper.map(eventDB, EventFullDto.class);
    }

    @Override
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
        return modelMapper.map(event, EventFullDto.class);
    }

    @Override
    public EventFullDto cancelUserEvent(Long userId, Long eventId) {
        log.info("Canceling the event with id {}", eventId);
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        event.setState(EventStatus.CANCELED);
        Event eventDB = eventRepository.save(event);
        return modelMapper.map(eventDB, EventFullDto.class);
    }

    @Override
    public List<ParticipantRequestDto> getEventRequests(Long userId, Long eventId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));

        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));

        Collection<Request> requests = requestRepository.getAllByEventId(eventId);
        return requests.stream()
                .map(request -> modelMapper.map(request, ParticipantRequestDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public ParticipantRequestDto confirmRequest(Long userId, Long eventId, Long requestId) {
        log.info("Getting all confirmed requests from user {}", userId);
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("В БД нет пользователя с id " + userId));
        eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("В БД нет события с id " + eventId));
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("В БД нет запроса с id " + requestId));
        request.setStatus(RequestStatus.CONFIRMED);
        requestRepository.save(request);
        return modelMapper.map(request, ParticipantRequestDto.class);
    }

    @Override
    public ParticipantRequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("В БД нет запроса с id " + requestId));
        request.setStatus(RequestStatus.REJECTED);
        log.info("Has changed status on " + request.getStatus() + " for request id " + requestId);
        requestRepository.save(request);
        return modelMapper.map(request, ParticipantRequestDto.class);
    }

    @Override
    public EventFullDto getShortDtoById(Long eventId, HttpServletRequest request) {
        log.info("Getting event with id {}", eventId);
        statsClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        );
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("В БД нет события с id " + eventId)));
        return modelMapper.map(event, EventFullDto.class);
    }

    /*@Override
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                      boolean onlyAvailable, String sort,
                                      Integer from, Integer size, HttpServletRequest request) {
        statsClient.postHit(
                new Hit(request.getServerName(), request.getRequestURI(), request.getRemoteAddr(),
                        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        );
        Pageable pageable = PageRequest.of(from, size, Sort.by("id"));

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
                .map(event -> modelMapper.map(event, EventShortDto.class))
                .collect(Collectors.toList());
    }*/
}
