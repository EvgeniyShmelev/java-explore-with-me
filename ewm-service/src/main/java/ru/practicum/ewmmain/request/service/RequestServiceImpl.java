package ru.practicum.ewmmain.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewmmain.events.model.Event;
import ru.practicum.ewmmain.events.repository.EventRepository;
import ru.practicum.ewmmain.exception.BadRequestException;
import ru.practicum.ewmmain.exception.NotFoundException;
import ru.practicum.ewmmain.request.dto.ParticipantRequestDto;
import ru.practicum.ewmmain.request.mapper.RequestMapper;
import ru.practicum.ewmmain.request.model.Request;
import ru.practicum.ewmmain.request.model.RequestStatus;
import ru.practicum.ewmmain.request.repository.RequestRepository;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.users.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RequestServiceImpl implements RequestService {
    private final UserRepository userRepository;
    public final EventRepository eventRepository;
    private final RequestRepository requestRepository;

    @Override
    @Transactional
    public ParticipantRequestDto add(Long userId, Long eventId) {
        if (eventId == null) {
            throw new BadRequestException("EventId �� ����� ���� null");
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("� �� ��� ������������ � id " + userId));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("� �� ��� ������� � id " + eventId));
        log.info("���������� ������� �� ������������ � id {} �� ������� � ������� � id {}", userId, eventId);
        Request newRequest = new Request(null, event, user, LocalDateTime.now(), RequestStatus.PENDING);
        if (!event.getRequestModeration()) {
            newRequest.setStatus(RequestStatus.CONFIRMED);
        }
        log.info("�������� ������ {}", newRequest);
        return RequestMapper.toRequestDto(requestRepository.save(newRequest));
    }

    @Override
    @Transactional
    public ParticipantRequestDto update(Long userId, Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("� �� ��� ������� � id " + requestId));
        log.info("������ ������� �� ������������ � id {} �� ������� � ������� � id {}", userId, requestId);
        request.setStatus(RequestStatus.CANCELED);
        return RequestMapper.toRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ParticipantRequestDto> getAll(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("� �� ��� ������������ � id " + userId));
        log.info("��������� ���������� � ������� ������������ {} �� ������� � ����� ��������", userId);
        Collection<Request> requests = requestRepository.getAllByRequesterId(user.getId());
        List<ParticipantRequestDto> requestsDto = requests.stream()
                .map(RequestMapper::toRequestDto)
                .collect(Collectors.toList());
        log.info("������ ������: {} ", requestsDto);
        return requestsDto;
    }
}
