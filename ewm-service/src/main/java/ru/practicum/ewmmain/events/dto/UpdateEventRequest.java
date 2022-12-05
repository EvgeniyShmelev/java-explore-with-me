package ru.practicum.ewmmain.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewmmain.utill.Update;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class UpdateEventRequest {
    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;
    @NotNull(groups = {Update.class})
    private Long eventId;
    private Boolean paid;
    private Long participantLimit;
    private String title;
}
