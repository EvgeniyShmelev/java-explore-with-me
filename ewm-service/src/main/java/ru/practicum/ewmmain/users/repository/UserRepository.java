package ru.practicum.ewmmain.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.users.model.User;

import java.util.Collection;

public interface UserRepository extends JpaRepository<User, Long> {
    Collection<User> findUsersByIdIn(Collection<Long> eventId, Pageable pageable);
}
