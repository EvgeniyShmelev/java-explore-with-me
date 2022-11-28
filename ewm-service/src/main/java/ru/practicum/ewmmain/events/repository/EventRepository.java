package ru.practicum.ewmmain.events.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.users.model.User;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByCategoryId(Long id);

    List<Event> findEventsByInitiatorIdIn(List<User> users,
                                 List<EventStatus> states,
                                 List<Category> setOfCategories,
                                 LocalDateTime start, LocalDateTime end,
                                 Pageable pageable);

    List<Event> findEventsByInitiatorId(Long userId, PageRequest pageRequest);
}
