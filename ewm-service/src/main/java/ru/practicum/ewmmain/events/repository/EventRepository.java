package ru.practicum.ewmmain.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.events.model.Event;

import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long> {
    Collection<Event> findAllByCategoryId(Long id);
}
