package com.bs.application.services;

import com.bs.application.dtos.Response;
import com.bs.application.dtos.UserEntityDto;
import com.bs.application.entities.User;
import com.bs.application.repos.UserRepo;
import com.bs.application.utils.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepo userRepo;
    private final ResponseUtil responseUtil;
    private final ModelMapper modelMapper;

    public UserDetailsService userDetailsService(){
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
}
