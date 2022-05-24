package com.example;


import com.example.dto.TeamDTO;
import com.example.facades.NeoFacade;
import com.example.models.ERole;
import com.example.models.Role;
import com.example.models.User;
import com.example.repositories.UserRepository;
import com.example.security.services.TeamDetailsService;
import com.github.javafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.*;

public class RandomGenerator {
    static Faker faker = new Faker();

    public void makeUser(UserRepository userRepository, PasswordEncoder encoder){
        User user= new User();
        Set roles = new HashSet<>();
        Role r =new Role();
        r.setId(1);
        r.setName(ERole.ROLE_USER);
        roles.add(r);
        user.setRoles(roles);
        user.setDate(LocalDateTime.now().toString());
        String name = faker.name().fullName()+faker.beer().style();
        user.setUsername(name);
        user.setEmail(name.replace(" ","")+"@gmail.com");
        user.setPassword(encoder.encode("1234"));
        userRepository.save(user);
        System.out.println(user);
        NeoFacade neoFacade = new NeoFacade();
        neoFacade.addPerson(user.getUsername());
    }

    public void makeTeams(UserRepository userRepository, TeamDetailsService teamDetailsService){
        List<User> users= userRepository.findAll();
        for (int j = 0; j < 10; j++) {

        List<Optional<User>> userInGroup=new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            //first number should be first id in user
            Long userId =random.nextLong(1482, 1482+users.size());
            Optional<User> user=userRepository.findById(userId);
            userInGroup.add(user);
            i++;
        }
        List<Long> usersGroup= new ArrayList<>();
        for (Optional<User> user:
             userInGroup) {
            if(user.isPresent()){
                System.out.println(user.get().getUsername());
                usersGroup.add(user.get().getId());
            }
        }
        String teamName=faker.color().name()+faker.beer().name();
        TeamDTO teamDTO= new TeamDTO(2123213L,teamName,usersGroup);
        teamDetailsService.createTeam(teamDTO);
    j++;
        }
    }
}
