package ru.practicum.ewmmain.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmmain.request.model.Request;
import ru.practicum.ewmmain.request.model.RequestStatus;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Request getByRequesterIdAndEventId(Long userId, Long eventId);

    List<Request> getAllByRequesterId(Long userId);

    List<Request> getAllByEventId(Long eventId);

    List<Request> findByEventIdAndStatus(Long id, RequestStatus status);
}
