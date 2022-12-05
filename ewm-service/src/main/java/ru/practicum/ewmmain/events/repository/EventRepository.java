package ru.practicum.ewmmain.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.model.EventStatus;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    List<Event> findAllByCategoryId(Long id);

    List<Event> findEventsByInitiatorId(Long userId, PageRequest pageRequest); //работает верно

    Event findByIdAndState(long eventId, EventStatus eventStatus);
}
