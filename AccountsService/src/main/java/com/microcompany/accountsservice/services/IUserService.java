package com.microcompany.accountsservice.services;

import com.microcompany.accountsservice.model.User;

import java.util.List;

public interface IUserService {
    User create(User user);

    List<User> getUsers();

    User getUser(Long id);

    User updateUser(Long id, User user);

    void delete(Long id);
}
