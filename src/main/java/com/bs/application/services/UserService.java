package com.bs.application.services;

import com.bs.application.dtos.Response;
import com.bs.application.dtos.UserEntityDto;
import com.bs.application.entities.User;
import com.bs.application.repos.UserRepo;
import com.bs.application.utils.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final ResponseUtil responseUtil;
    private final ModelMapper modelMapper;

    public Optional<Response> getUserByEmailAndPassword(UserEntityDto userEntityDto) {
        Response response;
        Optional<User> user = userRepo.findByEmailAndPassword(userEntityDto.getEmail(), userEntityDto.getPassword());
        if (user.isPresent()) {
            log.info("User exists fetching user details...");
            Type type = new TypeToken<UserEntityDto>() {
            }.getType();
            UserEntityDto mappedDto = modelMapper.map(user.get(), type);
            response = responseUtil.generateSuccessResponse(mappedDto);
        } else {
            response = responseUtil.generateFailureResponse("Fail to find user against the given username and password");
        }
        return Optional.of(response);
    }

    @Transactional
    public Optional<Response> registerAndUpdateUser(UserEntityDto userEntityDto) {
        Response response;
        User savedUser;
        try {
            Optional<User> user = userRepo.findByEmail(userEntityDto.getEmail());
            if (user.isPresent()) {
                log.info("User already present with email: {}, updating user details", userEntityDto.getEmail());
                savedUser = updateUser(userEntityDto, user.get());
            } else {
                log.info("Saving user Details");
                savedUser = saveUser(userEntityDto);
            }
            Type type = new TypeToken<UserEntityDto>() {
            }.getType();
            UserEntityDto mappedUser = modelMapper.map(savedUser, UserEntityDto.class);
            response = responseUtil.generateSuccessResponse(mappedUser);
        } catch (Exception er) {
            er.printStackTrace();
            log.info("Failed to save and update the user Details");
            response = responseUtil.generateFailureResponse("Failed to save and update the user Details");
        }
        return Optional.of(response);
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

    private User updateUser(UserEntityDto userEntityDto, User user) {
        userEntityDto.setId(user.getId());
        userEntityDto.getAddress().setId(user.getAddress().getId());
        userEntityDto.getCompany().setId(user.getCompany().getId());
        userEntityDto.getRoles().setId(user.getRoles().getId());
        userEntityDto.setUserId(user.getUserId());
        User mappedUser = modelMapper.map(userEntityDto, User.class);
        return userRepo.save(mappedUser);
    }

    private User saveUser(UserEntityDto userEntityDto) {
        userEntityDto.setUserId(UUID.randomUUID().toString());
        User mappedUser = modelMapper.map(userEntityDto, User.class);
        return userRepo.save(mappedUser);
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
