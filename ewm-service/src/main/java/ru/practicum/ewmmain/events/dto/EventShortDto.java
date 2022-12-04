package ru.practicum.ewmmain.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.ewmmain.categories.dto.CategoryDto;
import ru.practicum.ewmmain.users.dto.UserShortDto;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventShortDto {
    @NotBlank(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    private CategoryDto category;
    private Long confirmedRequests;
    @NotNull(groups = {Create.class})
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Long id;
    @NotNull(groups = {Create.class})
    private UserShortDto initiator;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    @NotBlank(groups = {Create.class})
    private String title;
    private Long views;
}
