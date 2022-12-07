package ru.practicum.ewmmain.users.dto;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;

@Data
public class UserShortDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
}
