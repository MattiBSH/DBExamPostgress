package com.example.controllers;

import com.example.dto.UserDTO;
import com.example.models.User;
import com.example.payload.request.SignupRequest;
import com.example.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    Gson gson = new Gson();
    @Autowired
    UserRepository userRepository;
    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String allAccess() {
        List<User> users=userRepository.findAll();
        List<UserDTO> usersDTO = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            usersDTO.add(new UserDTO(users.get(i).getUsername(),users.get(i).getEmail(),users.get(i).getId()));
        }
        return gson.toJson(usersDTO);
    }

    @DeleteMapping("/delete")
    @Transactional()
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String deleteUser(@RequestParam(name = "email") String email) {
        return "User deleted";
    }
}



