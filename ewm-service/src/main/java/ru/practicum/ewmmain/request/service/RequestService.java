package ru.practicum.ewmmain.request.service;

import org.springframework.stereotype.Repository;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;

import java.util.Collection;

@Repository
public interface RequestService {

    Collection<ParticipantRequestDto> getAll(Long userId);

    ParticipantRequestDto add(Long userId, Long eventId);

    ParticipantRequestDto update(Long userId, Long requestId);
}
