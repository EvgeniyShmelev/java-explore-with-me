package ru.practicum.ewmmain.users.dto;

import lombok.Data;
import ru.practicum.ewmmain.utill.Create;
import ru.practicum.ewmmain.utill.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class NewUserRequest {
    @NotBlank(groups = {Create.class})
    private String name;
    @Email(groups = {Update.class, Create.class})
    @NotNull(groups = {Create.class})
    private String email;
}
