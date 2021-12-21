package com.example.userservice.service;

import com.example.userservice.domain.DeleteUser;
import com.example.userservice.domain.Role;
import com.example.userservice.domain.User;
import com.example.userservice.repo.RoleRepo;
import com.example.userservice.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public  class UserServiceImpl implements UserService ,UserDetailsService {


    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            log.error("User not found in the database");

        } else {
            log.info("User found in the database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
    //1:30:43


    @Override
    public User saveUser(User user) {
        log.info("Saving new user {}  to the databased", user.getName());
        return userRepo.save(user);
    }

    @Override
    public User getUser(String  email)
    {
        User userByEmail = userRepo.findByEmail(email);
        log.info(String.valueOf(userByEmail));
        return userByEmail;
    }

    @Override
    public User addNewUser(User user) throws IllegalAccessException {
//        Optional<User> userByEmail =  userRepo.findByEmail(user.getEmail());
//        if(userByEmail.isPresent()){
//            throw new IllegalAccessException("email taken");
//        }
//        else {
//            userRepo.save(user);
//        }


        //기존에 db에 있는지 없는지 검사를 할때 두가지 방법이 있다
        // 하나는 findBy를 통해서 optional로 받아서 찾는거고
        // 또 하나는 exisit를 이용하면됌~

       user.setPassword(passwordEncoder.encode(user.getPassword()));
       Role role = roleRepo.findByName("ROLE_USER");
       user.getRoles().add(role);

        boolean exists = userRepo.existsByEmail(user.getEmail());
        if (!exists) {
            return userRepo.save(user);
        } else {
            throw new IllegalAccessException("email taken");
        }


    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving new role {} to the databased", role.getName());
        return roleRepo.save(role);
    }


    @Override
    public List<User> getUsers() {
        log.info("Fetching users ");
        return userRepo.findAll();
    }


    @Override
    public void deleteUser(int UserId) throws IllegalAccessException {

        boolean exisits = userRepo.existsById(UserId);
        if (!exisits) {
            throw new IllegalAccessException("student with id " + UserId + "does not exists");
        } else {
            userRepo.deleteById(UserId);
        }
    }

    @Override
    public List<User> deleteUserByEmail(DeleteUser deleteUser) throws IllegalAccessException {



        for(int i=0; i<deleteUser.getUserList().size();i++)
        {

            if (deleteUser.getUserList().get(i) != null)
            {
                User user = userRepo.findByEmail(deleteUser.getUserList().get(i).getEmail());
                userRepo.deleteByEmail(user.getEmail());

                //이메일이 안맞을 수도 있음
            }


        }
        return userRepo.findAll();




    }


    @Override
    public User updateRoletoUser(String email, String name) {
        log.info("email : ", email);
        log.info("name : ", name);

        User user = userRepo.findByEmail(email);
        Role role = roleRepo.findByName(name);

        user.getRoles().add(role);
        return user;

    }


}
