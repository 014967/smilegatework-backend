package com.example.userservice.domain;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class DeleteUser {

    private List<User> userList;
}
