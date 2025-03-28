package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.exception.UserNotFoundException;
import com.microcompany.accountsservice.model.User;
import com.microcompany.accountsservice.persistence.AccountRepository;
import com.microcompany.accountsservice.persistence.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements IUserService{
    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User create(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override

    public User updateUser(Long id, User user) {
        userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id)));
    }
}
