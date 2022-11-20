package ru.practicum.ewmmain.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewmmain.categories.dto.CategoryDto;
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
    private CategoryDto category;
    private Long confirmedRequests;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    private String description;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private String eventDate;
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
    private String state;
    @NotBlank(groups = {Create.class})
    private String title;
    private Long views;
}
