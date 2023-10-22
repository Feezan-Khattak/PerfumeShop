package com.bs.application;

import com.bs.application.entities.Address;
import com.bs.application.entities.Company;
import com.bs.application.entities.User;
import com.bs.application.repos.UserRepo;
import com.bs.application.security.Role;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class Application {
    private final UserRepo userRepo;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() {
        Optional<User> adminAccount = userRepo.findByRole(Role.ADMIN);
        if (adminAccount.isEmpty()) {
            log.info("Admin not found in the system, creating one...");
            userRepo.save(User.builder()
                    .createdAt(new Timestamp(System.currentTimeMillis()))
                    .updatedAt(new Timestamp(System.currentTimeMillis()))
                    .email("feezanktk2208@gmail.com")
                    .password(new BCryptPasswordEncoder().encode("pakistan"))
                    .firstName("Feezan")
                    .lastName("Khattak")
                    .phoneNumber("+92315973234")
                    .gender("Male")
                    .image("abc-image-stream")
                    .userId(UUID.randomUUID().toString())
                            .role(Role.ADMIN)
                    .address(Address.builder()
                            .address1("Shaidu")
                            .country("Pakistan")
                            .zipCode("24100")
                            .build())
                    .company(Company.builder()
                            .faxNumber("fax-abc")
                            .website("https://github.com/Feezan-khattak")
                            .name("abc-company")
                            .build())
                    .build()
            );
        }
    }

}
