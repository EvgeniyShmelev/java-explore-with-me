package ru.practicum.ewmmain.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewmmain.request.model.RequestStatus;

import java.time.LocalDateTime;

@Data
public class ParticipantRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;
    private RequestStatus status;
}
