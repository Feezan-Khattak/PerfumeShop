package com.bs.application.services;

import com.bs.application.dtos.*;
import com.bs.application.entities.User;
import com.bs.application.repos.UserRepo;
import com.bs.application.security.Role;
import com.bs.application.utils.ResponseUtil;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final ResponseUtil responseUtil;
    private final ModelMapper modelMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest){
        if(isValid(signInRequest)){
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getEmail(), signInRequest.getPassword()
            ));

            User user = userRepo.findByEmail(signInRequest.getEmail()).orElseThrow(() ->
                    new IllegalArgumentException("invalid Username and Password"));

            var token = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

            return JwtAuthenticationResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .build();
        }else{
            log.info("Please provide email and password");
            return JwtAuthenticationResponse.builder().build();
        }
    }

    private boolean isValid(SignInRequest signInRequest){
        return signInRequest != null &&
                signInRequest.getEmail() != null &&
                signInRequest.getPassword() != null;
    }

    @Transactional
    public Response registerAndUpdateUser(UserEntityDto userEntityDto) {
        Response response;
        User savedUser;
        try {
            Optional<User> user = userRepo.findByEmail(userEntityDto.getEmail());
            userEntityDto.setRole(Role.USER);
            if (user.isPresent()) {
                log.info("User already present with email: {}, updating user details", userEntityDto.getEmail());
                savedUser = updateUser(userEntityDto, user.get());
            } else {
                log.info("Saving user Details");
                savedUser = saveUser(userEntityDto);
            }
            Type type = new TypeToken<UserEntityDto>() {
            }.getType();
            UserEntityDto mappedUser = modelMapper.map(savedUser, type);
            response = responseUtil.generateSuccessResponse(mappedUser);
        } catch (Exception er) {
            er.printStackTrace();
            log.info("Failed to save and update the user Details");
            response = responseUtil.generateFailureResponse("Failed to save and update the user Details");
        }
        return response;
    }


    private User updateUser(UserEntityDto userEntityDto, User user) {
        userEntityDto.setId(user.getId());
        userEntityDto.getAddress().setId(user.getAddress().getId());
        userEntityDto.getCompany().setId(user.getCompany().getId());
        userEntityDto.setUserId(user.getUserId());
        if(StringUtils.isNotEmpty(userEntityDto.getPassword())){
            userEntityDto.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
        }
        User mappedUser = modelMapper.map(userEntityDto, User.class);
        return userRepo.save(mappedUser);
    }

    private User saveUser(UserEntityDto userEntityDto) {
        userEntityDto.setUserId(UUID.randomUUID().toString());
        userEntityDto.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
        User mappedUser = modelMapper.map(userEntityDto, User.class);
        return userRepo.save(mappedUser);
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        var email = jwtService.extractUserName(refreshTokenRequest.getToken());
        Optional<User> user = userRepo.findByEmail(email);
        if(user.isPresent()){
            if(jwtService.isTokenValid(refreshTokenRequest.getToken(), user.get())){
                var token = jwtService.generateToken(user.get());
                return JwtAuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshTokenRequest.getToken())
                        .build();
            }
        }
        return JwtAuthenticationResponse.builder().build();
    }
}
