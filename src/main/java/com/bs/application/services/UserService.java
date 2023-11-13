package com.bs.application.services;

import com.bs.application.dtos.*;
import com.bs.application.entities.ResetPassword;
import com.bs.application.entities.User;
import com.bs.application.repos.ResetPasswordRepo;
import com.bs.application.repos.UserRepo;
import com.bs.application.utils.AppProps;
import com.bs.application.utils.ResponseUtil;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.*;

import static com.bs.application.utils.Mail.FORGOT_PASSWORD;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final ResponseUtil responseUtil;
    private final ModelMapper modelMapper;

    public UserDetailsService userDetailsService() {
        return username ->
                userRepo.findByEmail(username).orElseThrow(() ->
                        new UsernameNotFoundException("Failed to authenticate user with the given email"));
    }

    public Optional<Response> getUserByUserId(String userId) {
        Response response;
        Optional<User> user = userRepo.findByUserId(userId);
        if (user.isPresent()) {
            log.info("User exists fetching user details...");
            Type type = new TypeToken<UserEntityDto>() {
            }.getType();
            UserEntityDto mappedUser = modelMapper.map(user.get(), type);
            response = responseUtil.generateSuccessResponse(mappedUser);
        } else {
            response = responseUtil.generateFailureResponse("Fail to find user against the given userId");
        }

        return Optional.of(response);
    }

    public Optional<Response> getAllUsers() {
        Response response;
        List<User> allUsers = userRepo.findAll();
        if (!allUsers.isEmpty()) {
            Type type = new TypeToken<List<UserEntityDto>>() {
            }.getType();
            List<UserEntityDto> users = modelMapper.map(allUsers, type);
            response = responseUtil.generateSuccessResponse(users);
        } else {
            log.info("Failed to fetch user details, no user found");
            response = responseUtil.generateFailureResponse("No User found");
        }
        return Optional.of(response);
    }

    public Optional<Response> getUser(String email) {
        Response response;
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            Type type = new TypeToken<UserEntityDto>() {
            }.getType();
            UserEntityDto fetchedUser = modelMapper.map(user, type);
            response = responseUtil.generateSuccessResponse(fetchedUser);
        } else {
            response = responseUtil.generateFailureResponse("Failed to fetch user detail using email: " + email);
        }
        return Optional.of(response);
    }

    public Optional<Response> updatePartialUpdate(String userId, Map<String, Object> updates) {
        Response response;
        Optional<User> user = userRepo.findByUserId(userId);
        if(user.isPresent()){
            applyUpdates(user.get(), updates);
            response = responseUtil.generateSuccessResponse("User details successfully updated");
        } else {
            response = responseUtil.generateFailureResponse("Failed to find user details against userId: " + userId);
        }
        return Optional.of(response);
    }

    private void applyUpdates(User user, Map<String, Object> updates){
        if(updates.containsKey("isEnabled")){
            user.setIsEnabled((Boolean) updates.get("isEnabled"));
            userRepo.save(user);
        }
    }
}
