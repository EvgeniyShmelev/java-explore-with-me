package ru.practicum.ewmmain.events.dto;

import lombok.Data;
import ru.practicum.ewmmain.events.model.Location;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    private Long category;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private LocalDateTime eventDate;
    @NotNull(groups = {Create.class})
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    @NotBlank(groups = {Create.class})
    private String title;
}
