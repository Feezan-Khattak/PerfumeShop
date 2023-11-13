package com.bs.application.controllers;

import com.bs.application.dtos.Response;
import com.bs.application.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

import static java.util.Optional.of;

@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(value = "*")
@RequestMapping("api/v1/user")
@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
public class UserController {
    private final UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('admin:read')")
    ResponseEntity<Optional<Response>> getAllUsers() {
        log.info("Request received for getting all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Optional<Response>> getUserByUserId(@PathVariable("userId") String userId) {
        log.info("Request received to get user details based on user_Id: {}", userId);
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }

    @GetMapping("/get")
    @PreAuthorize("hasAnyAuthority('admin:read', 'user:read')")
    ResponseEntity<Response> getUser() {
        log.info("Request received to get user details, ");
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("using email: {}", email);
        return ResponseEntity.of(userService.getUser(email));
    }

    @PatchMapping("/{userId}")
    ResponseEntity<Response> partialUserUpdate(@PathVariable String userId, @RequestBody Map<String, Object> updates){
        log.info("Request received to partial update the user against userId: {}", userId);
        return ResponseEntity.of(userService.updatePartialUpdate(userId, updates));
    }

}
