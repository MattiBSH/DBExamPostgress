package com.example;


import com.example.dto.ArrangementDTO;
import com.example.dto.TeamDTO;
import com.example.facades.ForumFacade;
import com.example.facades.NeoFacade;
import com.example.models.*;
import com.example.repositories.ArrangementRepository;
import com.example.repositories.TeamRepository;
import com.example.repositories.UserRepository;
import com.example.security.services.ArrangementService;
import com.example.security.services.ArrangementServiceImpl;
import com.example.security.services.TeamDetailsService;
import com.example.security.services.TeamDetailsServiceImpl;
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
    public static void generateAll(int userAmount, int teamAmount, int arrangementAmount, UserRepository userRepository, PasswordEncoder encoder, TeamDetailsService teamDetailsService, TeamRepository teamRepository, ArrangementRepository arrangementRepository){
        RandomGenerator randomGenerator = new RandomGenerator();
        ArrangementServiceImpl arrangementServiceImpl = new ArrangementServiceImpl();

        for (int d = 0; d < userAmount; d++) {
            randomGenerator.makeUser(userRepository,encoder);
        }

        List<User> users= userRepository.findAll();

        for (int e = 0; e < teamAmount; e++) {
            randomGenerator.createTeam(teamDetailsService,users);
        }

        Random random = new Random();

        for (int b = 0; b < arrangementAmount; b++) {
            ArrayList<Long>teamsIds=new ArrayList<>();
            ArrayList<Team>teams= (ArrayList<Team>) teamRepository.findAll();
            for (int a = 0; a< 6; a++) {
                int id =random.nextInt(0,teams.size());
                teamsIds.add(teams.get(id).getId());
            }
            Long winner =teams.get(random.nextInt(0,teamsIds.size())).getId();
            Long second =teams.get(random.nextInt(0,teamsIds.size())).getId();
            Long third =teams.get(random.nextInt(0,teamsIds.size())).getId();

            arrangementServiceImpl.createArrangement(new ArrangementDTO(124124124l,"Arrangement "+faker.beer().name(),"Tournament",teamsIds,winner,second,third, LocalDateTime.now().toString()),teamRepository,arrangementRepository);
        }
    }

    public void createTeam(TeamDetailsService teamDetailsService,List<User>users){
        List<Optional<User>> userInGroup=new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Random random = new Random();
            //first number should be first id in user
            Long userId =random.nextLong(0, users.size());

            userInGroup.add(Optional.ofNullable(users.get(userId.intValue())));
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

