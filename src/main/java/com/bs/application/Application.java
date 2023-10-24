package com.bs.application;

import com.bs.application.entities.Address;
import com.bs.application.entities.Company;
import com.bs.application.entities.Template;
import com.bs.application.entities.User;
import com.bs.application.repos.TemplateRepo;
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

import static com.bs.application.utils.Mail.FORGOT_PASSWORD;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class Application {
    private final UserRepo userRepo;
    private final TemplateRepo templateRepo;

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

        Optional<Template> forgotPasswordTemplate = templateRepo.findByTemplateKey(FORGOT_PASSWORD.name());
        if(forgotPasswordTemplate.isEmpty()){
            templateRepo.save(Template.builder()
                            .createdAt(new Timestamp(System.currentTimeMillis()))
                            .updatedAt(new Timestamp(System.currentTimeMillis()))
                            .templateKey(FORGOT_PASSWORD.name())
                            .subject("Password Reset Request")
                            .template("<!DOCTYPE html>\n" +
                                    "<html lang=\"en-US\">\n" +
                                    "\n" +
                                    "<head>\n" +
                                    "    <meta content=\"text/html; charset=utf-8\" http-equiv=\"Content-Type\">\n" +
                                    "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                                    "    <title>Reset Password</title>\n" +
                                    "    <meta name=\"description\" content=\"Reset Password\">\n" +
                                    "    <style type=\"text/css\">\n" +
                                    "        a:hover {\n" +
                                    "            text-decoration: underline !important;\n" +
                                    "        }\n" +
                                    "\n" +
                                    "        @media screen and (max-width: 600px) {\n" +
                                    "            table {\n" +
                                    "                width: 100% !important;\n" +
                                    "            }\n" +
                                    "\n" +
                                    "            td {\n" +
                                    "                display: block;\n" +
                                    "                width: 100%;\n" +
                                    "                box-sizing: border-box;\n" +
                                    "                padding: 10px;\n" +
                                    "            }\n" +
                                    "        }\n" +
                                    "    </style>\n" +
                                    "</head>\n" +
                                    "\n" +
                                    "<body marginheight=\"0\" topmargin=\"0\" marginwidth=\"0\" style=\"margin: 0px; background-color: #f2f3f8;\" leftmargin=\"0\">\n" +
                                    "    <!--100% body table-->\n" +
                                    "    <table cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"100%\" bgcolor=\"#f2f3f8\"\n" +
                                    "        style=\"font-family: 'Open Sans', sans-serif;\">\n" +
                                    "        <tr>\n" +
                                    "            <td>\n" +
                                    "                <table style=\"background-color: #f2f3f8; max-width:670px;  margin:0 auto;\" width=\"100%\" border=\"0\"\n" +
                                    "                    align=\"center\" cellpadding=\"0\" cellspacing=\"0\">\n" +
                                    "                    <tr>\n" +
                                    "                        <td style=\"height:20px;\">&nbsp;</td>\n" +
                                    "                    </tr>\n" +
                                    "                    <tr>\n" +
                                    "                        <td>\n" +
                                    "                            <table width=\"95%\" border=\"0\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\"\n" +
                                    "                                style=\"max-width:670px;background:#fff; border-radius:3px; text-align:center;-webkit-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);-moz-box-shadow:0 6px 18px 0 rgba(0,0,0,.06);box-shadow:0 6px 18px 0 rgba(0,0,0,.06);\">\n" +
                                    "                                <tr>\n" +
                                    "                                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                                    "                                </tr>\n" +
                                    "                                <tr>\n" +
                                    "                                    <td style=\"padding:0 35px;\">\n" +
                                    "                                        <h1 style=\"color:#1e1e2d; font-weight:500; margin:0;font-size:28px;font-family:'Rubik',sans-serif;\">You have\n" +
                                    "                                            requested to reset your password</h1>\n" +
                                    "                                        <span\n" +
                                    "                                            style=\"display:inline-block; vertical-align:middle; margin:20px 0 18px; border-bottom:1px solid #cecece; width:100px;\"></span>\n" +
                                    "                                        <p style=\"color:#455056; font-size:14px;line-height:22px; margin:0;\">\n" +
                                    "                                            A unique link to reset your password has been generated for you. To reset your password, click the\n" +
                                    "                                            button below and follow the instructions.\n" +
                                    "                                        </p>\n" +
                                    "                                        <a href=\"{{password_reset_link}}\"\n" +
                                    "                                            style=\"background:#20e277;text-decoration:none !important; font-weight:500; margin-top:30px; color:#fff;text-transform:uppercase; font-size:14px;padding:10px 20px;display:inline-block;border-radius:50px;\">Reset\n" +
                                    "                                            Password</a>\n" +
                                    "                                    </td>\n" +
                                    "                                </tr>\n" +
                                    "                                <tr>\n" +
                                    "                                    <td style=\"height:20px;\">&nbsp;</td>\n" +
                                    "                                </tr>\n" +
                                    "                            </table>\n" +
                                    "                        </td>\n" +
                                    "                    </tr>\n" +
                                    "                    <tr>\n" +
                                    "                        <td style=\"height:20px;\">&nbsp;</td>\n" +
                                    "                    </tr>\n" +
                                    "                </table>\n" +
                                    "            </td>\n" +
                                    "        </tr>\n" +
                                    "    </table>\n" +
                                    "    <!--/100% body table-->\n" +
                                    "</body>\n" +
                                    "\n" +
                                    "</html>\n")
                    .build());
        }
    }
}
