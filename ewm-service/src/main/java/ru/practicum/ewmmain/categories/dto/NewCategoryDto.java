package ru.practicum.ewmmain.categories.dto;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;

import javax.validation.constraints.NotBlank;

@Data
public class NewCategoryDto {
    @NotBlank(groups = {Create.class})
    private String name;
}
