package ru.practicum.ewmmain.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.ewmmain.categories.model.Category;
import ru.practicum.ewmmain.users.model.User;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    @NotBlank(groups = {Create.class})
    private String annotation;
    @NotNull(groups = {Create.class})
    private Category category;
    private Long confirmedRequests;
    @NotNull(groups = {Create.class})
    private LocalDateTime eventDate;
    private Long id;
    @NotNull(groups = {Create.class})
    private User initiator;
    @NotNull(groups = {Create.class})
    private Boolean paid;
    @NotBlank(groups = {Create.class})
    private String title;
    private Long views;
}
