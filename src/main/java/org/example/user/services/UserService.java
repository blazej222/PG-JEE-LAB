package org.example.user.services;

import org.example.user.entity.User;
import org.example.user.repository.api.UserRepository;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }


    public Optional<User> find(UUID id) {
        return repository.find(id);
    }

    public Optional<User> find(String name) {
        return repository.findByName(name);
    }

    public void create(User user) {
        repository.create(user);
    }

}
