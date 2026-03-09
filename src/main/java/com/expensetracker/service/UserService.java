package com.expensetracker.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expensetracker.entity.User;
import com.expensetracker.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    UserRepository repo;

    public User register(User user) {

        user.setRole("USER");

        return repo.save(user);
    }

    public User login(String email,String password){

        User user = repo.findByEmail(email).orElse(null);

        if(user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }

}