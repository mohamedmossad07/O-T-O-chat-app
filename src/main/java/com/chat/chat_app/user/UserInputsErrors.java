package com.chat.chat_app.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
@Data
@NoArgsConstructor
public class UserInputsErrors {
    public ArrayList<String> fullname = new ArrayList<>();
    public ArrayList<String> username = new ArrayList<>();
    public ArrayList<String> password = new ArrayList<>();
}
