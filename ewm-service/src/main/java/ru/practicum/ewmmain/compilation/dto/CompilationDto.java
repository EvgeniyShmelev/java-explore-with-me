package ru.practicum.ewmmain.compilation.dto;

import lombok.Data;
import ru.practicum.ewmmain.events.dto.EventShortDto;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class CompilationDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String title;
    @NotNull(groups = {Create.class})
    private Boolean pinned;
    @NotNull(groups = {Create.class})
    private Collection<EventShortDto> events;
}
