package com.example.facades;

import com.example.models.Arrangement;
import com.example.models.Team;
import com.example.models.User;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.time.LocalDateTime;
import java.util.Date;

import static org.neo4j.driver.Values.parameters;

public class NeoFacade {
    Driver driver;

    public static void main(String[] args) {
        NeoFacade neoFacade= new NeoFacade();
        neoFacade.getTeamWithMostWins();
        neoFacade.getTeamWithMostSecond();
        neoFacade.getTeamWithMostThird();
    }
    public void startDriver(String uri, String user, String password)
    {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    public void addPerson(String name)
    {
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            session.writeTransaction(tx -> tx.run("CREATE (a:Person {name: $x, joinedAt: $y})", parameters("x", name, "y", LocalDateTime.now())));
        }
        close();
    }

    public void addTeam(Team team)
    {
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            for (User u: team.getMembers()
                 ) {
                session.writeTransaction(tx -> tx.run("" +
                                "Merge (t:Team {name:$x})" +
                                "Merge (p:Person {name:$z})" +
                                "MERGE (t)-[m:HAS_MEMBER {joinedTeam: $y}]-(p)",
                        parameters("x", team.getName(), "y", LocalDateTime.now(), "z", u.getUsername())));
            }
        }
        close();
    }

    public void addEvent(Arrangement a)
    {
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "Merge (t:Team {name:$x})" +
                    "Merge (a:Event {name:$z, type:$b, winner:$w, second:$s, third:$e})" +
                    "MERGE (a)-[m:PARTICIPANT {participated: $y}]-(t)";
            String query2 = "" +
                    "Merge (t:Team {name:$x})" +
                    "Merge (a:Event {name:$z, type:$b, winner:$w, second:$s, third:$e})" +
                    "MERGE (a)-[m:PARTICIPANT {participated: $y, placement:$p}]-(t)";

            for (Team t: a.getTeamsParticipated()
            ) {
                String query = "";
                if(t.getId() == a.getWinner().getId()){
                    session.writeTransaction(tx -> tx.run(query2,
                            parameters("x", t.getName(), "y", LocalDateTime.now(),
                                    "z", a.getName(), "b", a.getType(), "p", "Winner",
                                    "w", a.getWinner().getId(), "s", a.getSecond().getId(),
                                    "e", a.getThird().getId())));
                } else if(t.getId() == a.getSecond().getId()){
                    session.writeTransaction(tx -> tx.run(query2,
                            parameters("x", t.getName(), "y", LocalDateTime.now(),
                                    "z", a.getName(), "b", a.getType(), "p", "Second",
                                    "w", a.getWinner().getId(), "s", a.getSecond().getId(),
                                    "e", a.getThird().getId())));
                } else if(t.getId() == a.getThird().getId()){
                    session.writeTransaction(tx -> tx.run(query2,
                            parameters("x", t.getName(), "y", LocalDateTime.now(),
                                    "z", a.getName(), "b", a.getType(), "p", "Third",
                                    "w", a.getWinner().getId(), "s", a.getSecond().getId(),
                                    "e", a.getThird().getId())));
                } else{
                    session.writeTransaction(tx -> tx.run(query1,
                            parameters("x", t.getName(), "y", LocalDateTime.now(),
                                    "z", a.getName(), "b", a.getType(),
                                    "w", a.getWinner().getId(), "s", a.getSecond().getId(),
                                    "e", a.getThird().getId())));
                }
            }
        }
        close();
    }
    public void getTeamWithMostWins()
    {
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Winner'}]-(p2:Event)"+
            "WITH p1, COUNT(*) AS num ORDER BY num DESC "+
            "RETURN num, p1.name;";
            Result str=session.run(query1);
            System.out.println(str.stream().toList());

        }
        close();
    }
    public void getTeamWithMostSecond()
    {
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Second'}]-(p2:Event)"+
                    "WITH p1, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, p1.name;";
            Result str=session.run(query1);
            System.out.println(str.stream().toList());

        }
        close();
    }
    public void getTeamWithMostThird()
    {
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Third'}]-(p2:Event)"+
                    "WITH p1, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, p1.name;";
            Result str=session.run(query1);
            System.out.println(str.stream().toList());

        }
        close();
    }
    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }
}
