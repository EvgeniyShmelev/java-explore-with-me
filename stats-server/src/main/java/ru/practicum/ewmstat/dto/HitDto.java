package ru.practicum.ewmstat.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class HitDto {
    private String app;
    @NotBlank
    private String uri;
    @NotBlank
    @Size(max = 15)
    private String ip;
    @NotNull
    private String timestamp;
}
