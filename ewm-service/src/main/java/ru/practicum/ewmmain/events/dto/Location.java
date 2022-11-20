package ru.practicum.ewmmain.events.dto;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Location {
    @NotBlank(groups = {Create.class})
    private Float lat;
    @NotNull(groups = {Create.class})
    private Float lon;
}
