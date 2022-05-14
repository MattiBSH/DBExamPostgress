package com.example.controllers;
import com.example.dto.ArrangementDTO;
import com.example.dto.UserDTO;
import com.example.models.Arrangement;
import com.example.models.User;
import com.example.repositories.ArrangementRepository;
import com.example.repositories.UserRepository;
import com.example.security.services.ArrangementService;
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
@RequestMapping("/api/arrangement")
public class ArrangementController {
    Gson gson = new Gson();
    @Autowired
    ArrangementRepository arrangementRepository;
    @Autowired
    ArrangementService arrangementService;
    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String allAccess() {
        List<Arrangement> arrangements=arrangementRepository.findAll();
        List<ArrangementDTO> arrangementDTOs = new ArrayList<>();
        for (int i = 0; i < arrangements.size(); i++) {
            List<Long> ids = new ArrayList<>();
            for (int j = 0; j < arrangements.get(i).getUsersParticipated().size(); j++) {
                ids.add(arrangements.get(i).getUsersParticipated().get(j).getId());
            }
            arrangementDTOs.add(new ArrangementDTO(arrangements.get(i).getName(),arrangements.get(i).getType(),ids));
        }
        return gson.toJson(arrangementDTOs);
    }

    @DeleteMapping("/delete")
    @Transactional()
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public String deleteUser(@RequestParam(name = "id") Long id) {
        arrangementRepository.deleteArrangementById(id);
        return "User deleted";
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> createVehicle(@RequestBody ArrangementDTO arrangementDTO){
        return new ResponseEntity<>(arrangementService.createArrangement(arrangementDTO), HttpStatus.CREATED);
    }
}