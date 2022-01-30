package com.example.fila.demo.config;

import com.example.fila.demo.entity.User;
import com.example.fila.demo.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DatabaseLoader implements CommandLineRunner {

    @Autowired
    private UserRepository repository;

    // call at first run
    @Override
    public void run(String... args) {
        createUser("asd", "asd");
    }

    private void createUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        repository.save(user);

        log.info("Created user ({}) with username '{}' and password '{}'", user.getId(), username, password);
    }
}
