package com.bs.application.controllers;

import com.bs.application.dtos.Response;
import com.bs.application.dtos.UserEntityDto;
import com.bs.application.services.AuthenticationService;
import com.bs.application.services.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read'")
    ResponseEntity<Optional<Response>> getAllUsers(){
        log.info("Request received for getting all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getUserByUserId(@PathVariable("userId") String userId){
        log.info("Request received to get user details based on user_Id: {}", userId);
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }
}
