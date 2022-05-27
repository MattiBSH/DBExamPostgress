package com.example;


import com.example.dto.TeamDTO;
import com.example.facades.ForumFacade;
import com.example.facades.NeoFacade;
import com.example.models.*;
import com.example.repositories.UserRepository;
import com.example.security.services.TeamDetailsService;
import com.github.javafaker.Faker;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.UnknownHostException;
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
        for (int j = 0; j < 30; j++) {

        List<Optional<User>> userInGroup=new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            //first number should be first id in user
            Long userId =random.nextLong(493, 493+users.size());
            Optional<User> user=userRepository.findById(userId);
            userInGroup.add(user);
        }
        List<Long> usersGroup= new ArrayList<>();
        List<User> usersToNeo= new ArrayList<>();
            System.out.println("userInGroup: " + userInGroup.size());

            for (Optional<User> user:
             userInGroup) {
            if(user.isPresent()){
                System.out.println(user.get().getUsername());
                usersGroup.add(user.get().getId());
                usersToNeo.add(user.get());

            }
        }
            System.out.println("usersToNeo: " + usersToNeo.size());
        String teamName=faker.color().name()+faker.beer().name();
        TeamDTO teamDTO= new TeamDTO(2123213L,teamName,usersGroup);
        teamDetailsService.createTeam(teamDTO);
            ArrayList<User>users1=new ArrayList<>();
            for (int i = 0; i < usersToNeo.size(); i++) {
                if(usersToNeo.contains(usersToNeo.get(i))){
                }else{
                    users1.add(usersToNeo.get(i));
                }
            }
        Team team = new Team(teamName, users1);
            System.out.println("users1: " + users1.size());
        NeoFacade neoFacade = new NeoFacade();
        neoFacade.addTeam(team);
        }
    }

    public void generatePosts(int iterations) throws UnknownHostException {
        ForumFacade facade = new ForumFacade();
        for (int i = 0; i < iterations; i++) {
            Post post = new Post();

            String author = faker.name().fullName();
            post.setAuthor(author);

            String content = faker.chuckNorris().fact();
            post.setContent(content);

            post.setComments(new ArrayList<>());

            facade.addPost(post);
        }
    }
}
