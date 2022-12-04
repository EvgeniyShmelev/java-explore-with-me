package ru.practicum.ewmmain.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.events.model.EventStatus;
import ru.practicum.ewmmain.events.model.Location;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventFullDto {
    @NotBlank(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    private Category category;
    private Long confirmedRequests;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    @NotNull(groups = {Create.class})
    private User initiator;
    @NotNull(groups = {Create.class})
    private Location location;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    private Long participantLimit;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventStatus state;
    @NotBlank(groups = {Create.class})
    private String title;
    private Long views;
}
