package com.bs.application.controllers;

import com.bs.application.dtos.*;
import com.bs.application.services.AuthenticationService;
import com.bs.application.services.UserService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    ResponseEntity<Response> signup(@RequestBody UserEntityDto userEntityDto){
        return ResponseEntity.ok(authenticationService.registerAndUpdateUser(userEntityDto));
    }

    @PostMapping("/signin")
    ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authenticationService.signIn(signInRequest));
    }

    @PostMapping("/refresh")
    ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
    }

    @PostMapping("/reset-password-request")
    ResponseEntity<Response> resetPasswordRequest(@RequestBody ResetPasswordRequest resetPasswordRequest){
        log.info("Request received for requesting password reset using email: {}", resetPasswordRequest.getEmail());
        return ResponseEntity.of(authenticationService.resetPasswordRequest(resetPasswordRequest));
    }

    @PostMapping("/reset-password")
    ResponseEntity<Response> resetPassword(@RequestBody ResetPasswordData resetPasswordData){
        log.info("Request received for password reset using token: {}", resetPasswordData.getToken());
        return ResponseEntity.of(authenticationService.resetUserPassword(resetPasswordData));
    }

    @GetMapping("/verify-reset-token/{resetToken}")
    ResponseEntity<Response> verifyResetToken(@PathVariable String resetToken){
        log.info("Request Received for verifying the reset token: {}", resetToken);
        return ResponseEntity.of(authenticationService.verifyResetToken(resetToken));
    }
}
