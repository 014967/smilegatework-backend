package com.example.userservice.repo;


import com.example.userservice.domain.User;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;



public interface UserRepo extends JpaRepository<User, Integer> {
    User findById(int id);

    User findByEmail(String email);
    boolean existsByEmail(String email);

    void deleteByEmail(String email);

    //@Query("SELECT u FROM User u WHERE u.email  = ?1")
    //Optional<User> findByEmail(String email);



}
