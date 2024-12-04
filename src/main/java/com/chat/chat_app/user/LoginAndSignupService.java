package com.chat.chat_app.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Optional;

@Service
public class LoginAndSignupService {
    private final UserRepository userRepository;

    public LoginAndSignupService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserInputsErrors signup(UserSignupDTO dto) throws NoSuchAlgorithmException {
        if (userRepository.findByUsername(dto.getUsername()).isPresent()){
            UserInputsErrors userInputsErrors = new UserInputsErrors();
            userInputsErrors.getUsername().add("This username already exists.");
            return userInputsErrors;
        }
        User user = new User(dto.getUsername(), dto.getFullname(), hashPassword(dto.getPass()),false);
        userRepository.save(user);
        return null;
    }
    public UserInputsErrors login(UserLoginDTO dto, HttpServletResponse response) throws NoSuchAlgorithmException {
       Optional<User> user = userRepository.findByUsername(dto.getUsername());
        UserInputsErrors userInputsErrors = new UserInputsErrors();
        if (user.isEmpty() ){
            userInputsErrors.getUsername().add("This username doesn't exists.");
            return  userInputsErrors;
        }
        if (!user.get().getPassword().equals(hashPassword(dto.getPass()))){
            userInputsErrors.getPassword().add("Wrong Password.");
            return  userInputsErrors;
        }
        user.get().setStatus(true);
        setUsernameCookieInResponse(dto.getUsername(),response);
        userRepository.save(user.get());
        return null;
    }

    private void setUsernameCookieInResponse(@NotEmpty String username, HttpServletResponse response) {
        Cookie cookie = new Cookie("username",username);
        cookie.setPath("/");
        cookie.setMaxAge(60*60);
        response.addCookie(cookie);
    }


    public String hashPassword(String pass) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes =  messageDigest.digest(pass.getBytes(StandardCharsets.UTF_8));
        return HexFormat.of().formatHex(hashBytes);
    }

}
