package com.example.project1.Controller;

import com.example.project1.Credentials.TokenEntity;
import com.example.project1.Credentials.UserEntity;
import com.example.project1.Repositories.LoginRepository;
import com.example.project1.Repositories.TokenRepository;
import com.example.project1.Service.EmailSenderService;
import com.example.project1.Service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class Controller {
    @Autowired
    LoginRepository loginRepository;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    EmailSenderService emailSenderService;
    @Autowired
    PasswordService passwordService;
    PasswordEncoder passwordEncoder;

    @Autowired
    public Controller(PasswordService passwordRepo) {
        this.passwordService = passwordRepo;
    }

    @PostMapping("/register")
    public Object register(@Valid @RequestBody UserEntity userEntity){

        UserEntity existingUser = loginRepository.findByEmailIdIgnoreCase(userEntity.getMail());
        if(existingUser != null)
        {
            return "This email already exists!";
        }
        else
        {
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            userEntity.setPassword(bCryptPasswordEncoder.encode(userEntity.getPassword()));
            loginRepository.save(userEntity);

            TokenEntity tokenVerification = new TokenEntity(userEntity);
            tokenRepository.save(tokenVerification);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(userEntity.getMail());
            mailMessage.setSubject("Complete Registration!");
            mailMessage.setFrom("aarthi@divum.in");
            mailMessage.setText("To confirm your account, please click here : "
                    +"http://localhost:8080/confirm-account?token="+tokenVerification.getConfirmationtoken());
            emailSenderService.sendEmail(mailMessage);
        }
        return "Successfully registered";
    }
    @PostMapping("/confirm-account")
    public Object confirmUserAccount(@RequestParam("token")String tokenVerification) {
        TokenEntity token = null;
        List<TokenEntity> all = (List<TokenEntity>) tokenRepository.findAll();
        for (TokenEntity tokenObj : all) {
            if (tokenObj.getConfirmationtoken() != null && tokenObj.getConfirmationtoken().equals(tokenVerification)) {
                token = tokenObj;
                break;
            }
        }
        if (token != null) {
            Long userId = token.getUserEntity().getId();
            Optional<UserEntity> byId = loginRepository.findById(userId);
            byId.get().setEnabled(true);
            loginRepository.save(byId.get());
            return "Account Verified";
        }
        return "The link is invalid or broken!";

    }
    @PostMapping("/login")
    public Object login(@RequestBody UserEntity userEntity){
        UserEntity existing = loginRepository.findByEmailIdIgnoreCase(userEntity.getMail());
        if(existing!=null){
            String encodedpassword = loginRepository.findByEmailIdIgnoreCase(userEntity.getMail()).getPassword();
            BCryptPasswordEncoder bCryptPasswordEncoder=new BCryptPasswordEncoder();
            if(bCryptPasswordEncoder.matches(userEntity.getPassword(), encodedpassword)){
                return loginRepository.findByEmailIdIgnoreCase(userEntity.getMail()).getFirstname()+" logged in Successfully";
            }

        }
        return "Incorrect mailid / password";
    }
}
