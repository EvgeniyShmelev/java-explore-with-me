package ru.practicum.ewmmain.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.users.model.User;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventRepository extends JpaRepository<Event, Long> {
    Collection<Event> findAllByCategoryId(Long id);

    Collection<Event> getEventsByAdmin(Collection<User> users,
                                       Collection<EventStatus> states,
                                       Collection<Category> setOfCategories,
                                       LocalDateTime start, LocalDateTime end,
                                       Pageable pageable);

    Collection<Event> findEventsByInitiatorId(Long userId, PageRequest pageRequest);
}
