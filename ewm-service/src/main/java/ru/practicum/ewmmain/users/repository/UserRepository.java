package ru.practicum.ewmmain.users.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.users.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByIdIn(Collection<Long> eventId, Pageable pageable);

    Optional<User> getUserByName(String name);
}
