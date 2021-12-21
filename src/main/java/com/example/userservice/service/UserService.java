package com.example.userservice.service;

import com.example.userservice.domain.DeleteUser;
import com.example.userservice.domain.Role;
import com.example.userservice.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;



public interface UserService {


    User saveUser(User user);
    User getUser(String email);
    User addNewUser(User user) throws IllegalAccessException;
    Role saveRole(Role role);
    List<User> getUsers();
    void deleteUser(int UserId) throws IllegalAccessException;

    List<User> deleteUserByEmail(DeleteUser deleteUser) throws IllegalAccessException;

    User updateRoletoUser(String email, String name);


}
