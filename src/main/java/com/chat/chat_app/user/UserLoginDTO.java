package com.chat.chat_app.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class UserLoginDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String pass;
}
