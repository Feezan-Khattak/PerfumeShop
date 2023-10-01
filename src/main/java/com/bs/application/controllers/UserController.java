package com.bs.application.controllers;

import com.bs.application.dtos.Response;
import com.bs.application.dtos.UserEntityDto;
import com.bs.application.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("api/user")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @GetMapping
    ResponseEntity<Optional<Response>> getUser(@RequestParam String email, @RequestParam String password){
        log.info("Request received for getting user details using email: {}", email );
        return ResponseEntity.ok(userService.getUserByEmailAndPassword(email, password));
    }

    @GetMapping("/all")
    ResponseEntity<Optional<Response>> getAllUsers(){
        log.info("Request received for getting all users");
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    ResponseEntity<Optional<Response>> getUser(@RequestBody UserEntityDto userEntityDto){
        log.info("Request received to register and update the user");
        return ResponseEntity.ok(userService.registerAndUpdateUser(userEntityDto));
    }

    @GetMapping("/{userId}")
    ResponseEntity<Optional<Response>> getUserByUserId(@PathVariable("userId") String userId){
        log.info("Request received to get user details based on user_Id: {}", userId);
        return ResponseEntity.ok(userService.getUserByUserId(userId));
    }
}
