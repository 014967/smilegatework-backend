package com.example.userservice.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.userservice.domain.DeleteUser;
import com.example.userservice.domain.Role;
import com.example.userservice.domain.RoleToUserForm;
import com.example.userservice.domain.User;
import com.example.userservice.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getUsers() {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/").toUriString());
        return ResponseEntity.created(uri).body(userService.getUsers());

    }

    @GetMapping(path = "/user/{userEmail}")
    public ResponseEntity<User> getUser(@PathVariable("userEmail") String useremail) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/" + useremail).toUriString());
        return ResponseEntity.created(uri).body(userService.getUser(useremail));
    }

//    @PostMapping("/user/save")
//    public ResponseEntity<User> saveUser(@RequestBody User user) {
//
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
//        return ResponseEntity.created(uri).body(userService.saveUser(user));
//
//        //response 200 -> 201 ( 서버에서 무언가 생성돼었음)
//    }

//    @PostMapping("/user/save")
//    public void addUser(@RequestBody User user) throws IllegalAccessException {
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/add").toUriString());
//        userService.addNewUser(user);
//
//    }



    @PostMapping("/user/save")
    public ResponseEntity<User> addUser(@RequestBody User user) throws IllegalAccessException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/add").toUriString());
        return ResponseEntity.created(uri).body(userService.addNewUser(user));


    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }


    @DeleteMapping(path = "/user/{userId}")
    public void deleteUser(@PathVariable("userId") int UserId) throws IllegalAccessException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/" + UserId).toUriString());
        userService.deleteUser(UserId);
    }

//    @DeleteMapping(path="/user/delete/{userEmail}")
//    public void deleteUserByEmail(@PathVariable("userEmail") String email) throws  IllegalAccessException{
//        userService.deleteUserByEmail(email);
//    }


    @DeleteMapping("/user/delete")
    public void deleteUserByEmail(HttpServletRequest request ,HttpServletResponse response, @RequestBody DeleteUser deleteUser) throws IllegalAccessException {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/delete").toUriString());
        userService.deleteUserByEmail(request, deleteUser);
    }

    @PutMapping("/user/updateRole")
    public ResponseEntity<User> updateRoletoUser(@RequestBody RoleToUserForm roleToUserForm) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/updateRole").toUriString());
        return ResponseEntity.created(uri).body(userService.updateRoletoUser(roleToUserForm.getEmail(), roleToUserForm.getName()));

    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);

                log.info(decodedJWT.getPayload());
                String username = decodedJWT.getSubject();

                User user = userService.getUser(username);


                String access_token = JWT.create()
                        .withSubject(user.getEmail()) //유저의 고유한 정보를 전달
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);



                Map<String , String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);




            } catch (Exception exception) {


                response.setHeader("error", exception.getMessage());
                //response.sendError(FORBIDDEN.value());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);


            }

        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }


}
