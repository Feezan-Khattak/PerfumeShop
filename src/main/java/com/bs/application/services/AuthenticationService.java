package com.bs.application.services;

import com.bs.application.dtos.*;
import com.bs.application.entities.ResetPassword;
import com.bs.application.entities.User;
import com.bs.application.repos.ResetPasswordRepo;
import com.bs.application.repos.UserRepo;
import com.bs.application.security.Role;
import com.bs.application.utils.AppProps;
import com.bs.application.utils.ResponseUtil;
import jakarta.mail.MessagingException;
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
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import static com.bs.application.utils.Mail.FORGOT_PASSWORD;

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
    private final ResetPasswordRepo resetPasswordRepo;
    private final AppProps appProps;
    private final MailService mailService;

    public JwtAuthenticationResponse signIn(SignInRequest signInRequest) {
        if (isValid(signInRequest)) {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getEmail(), signInRequest.getPassword()
            ));

            User user = userRepo.findByEmail(signInRequest.getEmail()).orElseThrow(() ->
                    new IllegalArgumentException("invalid Username and Password"));
            if (user.isEnabled()) {
                var token = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);

                return JwtAuthenticationResponse.builder()
                        .status("OK")
                        .token(token)
                        .refreshToken(refreshToken)
                        .build();
            } else {
                log.info("User is not enabled, please contact costumer support");
                return JwtAuthenticationResponse.builder().status("User not enabled").build();
            }
        } else {
            log.info("Please provide email and password");
            return JwtAuthenticationResponse.builder().status("Invalid Credentials").build();
        }
    }

    private boolean isValid(SignInRequest signInRequest) {
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
        if (StringUtils.isNotEmpty(userEntityDto.getPassword())) {
            userEntityDto.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
        }
        User mappedUser = modelMapper.map(userEntityDto, User.class);
        return userRepo.save(mappedUser);
    }

    private User saveUser(UserEntityDto userEntityDto) {
        userEntityDto.setUserId(UUID.randomUUID().toString());
        userEntityDto.setPassword(passwordEncoder.encode(userEntityDto.getPassword()));
        userEntityDto.setEnabled(true);
        User mappedUser = modelMapper.map(userEntityDto, User.class);
        return userRepo.save(mappedUser);
    }

    public JwtAuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
        var email = jwtService.extractUserName(refreshTokenRequest.getToken());
        Optional<User> user = userRepo.findByEmail(email);
        if (user.isPresent()) {
            if (jwtService.isTokenValid(refreshTokenRequest.getToken(), user.get())) {
                var token = jwtService.generateToken(user.get());
                return JwtAuthenticationResponse.builder()
                        .token(token)
                        .refreshToken(refreshTokenRequest.getToken())
                        .build();
            }
        }
        return JwtAuthenticationResponse.builder().build();
    }

    public Optional<Response> resetPasswordRequest(ResetPasswordRequest resetPasswordRequest) {
        Response response = null;
        Optional<User> user = userRepo.findByEmail(resetPasswordRequest.getEmail());
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            ResetPassword resetPassword = new ResetPassword();
            response = sendEmail(token, resetPasswordRequest.getEmail());
            if (response.getStatus().equals("OK")) {
                resetPassword.setEmailSent(true);
                resetPassword.setToken(token);
                resetPassword.setExpiresAt(new Timestamp(System.currentTimeMillis() + 3600000));
                resetPassword.setUser_id(user.get());
                resetPasswordRepo.save(resetPassword);
                response = responseUtil.generateSuccessResponse(resetPassword);
            }

        } else {
            response = responseUtil.generateFailureResponse("Fail to sent email");
        }
        return Optional.ofNullable(response);
    }

    private Response sendEmail(String token, String email) {
        HashMap<String, String> map = new HashMap<>();
        map.put("password_reset_link", appProps.getResetPasswordUrl() + "/" + token);
        try {
            return mailService.sendEmail(MailRequest.builder()
                    .subject("Password Reset Request")
                    .email(email)
                    .template(FORGOT_PASSWORD.name())
                    .data(map)
                    .build());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<Response> resetUserPassword(ResetPasswordData resetPasswordData) {
        Response response = null;
        Optional<ResetPassword> resetPasswordToken = resetPasswordRepo.findByToken(resetPasswordData.getToken());
        if (resetPasswordToken.isPresent() && canReset(resetPasswordToken.get(), resetPasswordData)) {
            Optional<User> user = userRepo.findByEmail(resetPasswordData.getEmail());
            if (user.isPresent()) {
                user.get().setPassword(passwordEncoder.encode(resetPasswordData.getPassword()));
                user.get().setId(user.get().getId());
                user.get().setAddress(user.get().getAddress());
                user.get().setCompany(user.get().getCompany());
                user.get().setUserId(user.get().getUserId());
                userRepo.save(user.get());
                response = responseUtil.generateSuccessResponse("Password Successfully updated");
            }
        } else {
            response = responseUtil.generateFailureResponse("Failed to update the password, please check the email and token");
        }

        return Optional.ofNullable(response);
    }

    private boolean canReset(ResetPassword resetPassword, ResetPasswordData resetPasswordData) {
        return !resetPassword.getExpiresAt().before(new Timestamp(System.currentTimeMillis())) &&
                resetPassword.getUser_id().getEmail().equals(resetPasswordData.getEmail());
    }

    public Optional<Response> verifyResetToken(String resetToken) {
        Response response;
        Optional<ResetPassword> resetPasswordToken = resetPasswordRepo.findByToken(resetToken);
        if (resetPasswordToken.isPresent()) {
            boolean before = resetPasswordToken.get().getExpiresAt().before(new Timestamp(System.currentTimeMillis()));
            if (!before) {
                response = responseUtil.generateSuccessResponse("Reset Token is valid and ready to use");
            } else {
                response = responseUtil.generateFailureResponse("Failed to validate the token, token is expired");
            }
        } else {
            response = responseUtil.generateFailureResponse("Please provide a valid token");
        }
        return Optional.of(response);
    }
}
