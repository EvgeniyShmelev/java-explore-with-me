package ru.practicum.ewmmain.categories.dto;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;

@Data
public class CategoryDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
}
