package com.example.controllers;

import com.example.dto.ArrangementDTO;
import com.example.dto.TeamDTO;
import com.example.dto.UserDTO;
import com.example.models.Arrangement;
import com.example.models.Team;
import com.example.models.User;
import com.example.repositories.ArrangementRepository;
import com.example.repositories.TeamRepository;
import com.example.repositories.UserRepository;
import com.example.security.services.ArrangementService;
import com.example.security.services.TeamDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        userRepository.deleteUserByEmail(email);
        return "User deleted";
    }

    @Autowired
    ArrangementRepository arrangementRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    ArrangementService arrangementService;
    @Autowired
    TeamDetailsService teamDetailsService;

    @GetMapping("allArrangements")
    public String allArrangements() {
        List<Arrangement> arrangements=arrangementRepository.findAll();
        List<ArrangementDTO> arrangementDTOs = new ArrayList<>();
        for (int i = 0; i < arrangements.size(); i++) {
            List<Long> ids = new ArrayList<>();
            for (int j = 0; j < arrangements.get(i).getTeamsParticipated().size(); j++) {
                ids.add(arrangements.get(i).getTeamsParticipated().get(j).getId());
            }
            Long winner=arrangements.get(i).getWinner().getId();
            Long second=arrangements.get(i).getSecond().getId();
            Long third=arrangements.get(i).getThird().getId();

            arrangementDTOs.add(new ArrangementDTO(arrangements.get(i).getId(),arrangements.get(i).getName(),arrangements.get(i).getType(),ids,winner,second,third,arrangements.get(i).getDate()));
        }
        return gson.toJson(arrangementDTOs);

    }

    @DeleteMapping("/arrangement/delete")
    @Transactional()
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteArrangement(@RequestParam(name = "id") Long id) {
        arrangementRepository.deleteArrangementById(id);
        return "Arrangement deleted";
    }

    @PostMapping("/arrangement")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createArrangement(@RequestBody ArrangementDTO arrangementDTO){
        return new ResponseEntity<>(arrangementService.createArrangement(arrangementDTO), HttpStatus.CREATED);
    }
    @PostMapping("/team")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createTeam(@RequestBody TeamDTO teamDTO){
        return new ResponseEntity<>(teamDetailsService.createTeam(teamDTO), HttpStatus.CREATED);
    }
    @GetMapping("/allFromIds")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public String getAllByID(@RequestBody ArrangementDTO arrangementDTO){
        List<User> users=userRepository.getAllByIdIn((ArrayList<Long>) arrangementDTO.getTeamIds());
        List<UserDTO> usersDTO = new ArrayList<>();
        for (int i = 0; i < users.size(); i++) {
            usersDTO.add(new UserDTO(users.get(i).getUsername(),users.get(i).getEmail(),users.get(i).getId()));
        }
        return gson.toJson(usersDTO);
    }
}



