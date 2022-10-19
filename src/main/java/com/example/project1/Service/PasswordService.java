package com.example.project1.Service;

import com.example.project1.Credentials.UserEntity;
import com.example.project1.Repositories.LoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private LoginRepository repo;
    PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordService(LoginRepository repo) {
        this.repo = repo;
    }

    public void save(UserEntity password) {
        repo.save(password);
    }

    public LoginRepository getRepo() {
        return repo;
    }
}
