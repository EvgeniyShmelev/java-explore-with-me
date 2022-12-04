package ru.practicum.ewmmain.events.model;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class Location {
    private Float lat;
    private Float lon;
}
