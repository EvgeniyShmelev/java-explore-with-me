package ru.practicum.ewmmain.compilation.dto;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class NewCompilationDto {
    @NotNull(groups = {Create.class})
    private Collection<Long> events;
    @NotNull(groups = {Create.class})
    private Boolean pinned;
    @NotBlank(groups = {Create.class})
    private String title;
}
