package com.example.facades;

import com.example.models.Arrangement;
import com.example.models.Team;
import com.example.models.User;
import com.google.gson.Gson;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.Values.parameters;

public class NeoFacade {
    Driver driver;
    Gson gson = new Gson();

    public static void main(String[] args) {
        NeoFacade neoFacade= new NeoFacade();
        //neoFacade.getPersonWithMostWins();
        //neoFacade.getPersonWithMostSecond();
        //neoFacade.getPersonWithMostThird();
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
    public List<String> getTeamWithMostWins()
    {
        List<String> resJson = new ArrayList<>();
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Winner'}]-(p2:Event)"+
            "WITH p1, COUNT(*) AS num ORDER BY num DESC "+
            "RETURN num, p1.name;";
            Result res = session.run(query1);
            List<Record> list = res.stream().toList();
            for (Record r: list
            ) {
                resJson.add(gson.toJson(r.asMap()));
            }

        }
        close();
        return resJson;
    }
    public List<String> getTeamWithMostSecond()
    {
        List<String> resJson = new ArrayList<>();
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Second'}]-(p2:Event)"+
                    "WITH p1, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, p1.name;";
            Result res = session.run(query1);
            List<Record> list = res.stream().toList();
            for (Record r: list
            ) {
                resJson.add(gson.toJson(r.asMap()));
            }

        }
        close();
        return resJson;
    }
    public List<String> getTeamWithMostThird()
    {
        List<String> resJson = new ArrayList<>();
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Third'}]-(p2:Event)"+
                    "WITH p1, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, p1.name;";
            Result res = session.run(query1);
            List<Record> list = res.stream().toList();
            for (Record r: list
            ) {
                resJson.add(gson.toJson(r.asMap()));
            }

        }
        close();
        return resJson;
    }
    public List<String> getPersonWithMostWins()
    {
        List<String> resJson = new ArrayList<>();
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Winner'}]-(p2:Event)" +
                    "MATCH (m:Person)-[h:HAS_MEMBER]-(p1)"+
                    "WITH m, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, m.name;";
            Result res = session.run(query1);
            List<Record> list = res.stream().toList();
            for (Record r: list
                 ) {
                resJson.add(gson.toJson(r.asMap()));
            }
        }
        close();
        return resJson;
    }

    public List<String> getPersonWithMostSecond()
    {
        List<String> resJson = new ArrayList<>();
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Second'}]-(p2:Event)" +
                    "MATCH (m:Person)-[h:HAS_MEMBER]-(p1)"+
                    "WITH m, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, m.name;";
            Result res = session.run(query1);
            List<Record> list = res.stream().toList();
            for (Record r: list
            ) {
                resJson.add(gson.toJson(r.asMap()));
            }
        }
        close();
        return resJson;
    }
    public List<String> getPersonWithMostThird()
    {
        List<String> resJson = new ArrayList<>();
        startDriver("bolt://localhost:7687", "neo4j", "1234");
        try (Session session = driver.session())
        {
            String query1 = "" +
                    "MATCH (p1:Team)-[:PARTICIPANT{placement:'Third'}]-(p2:Event)" +
                    "MATCH (m:Person)-[h:HAS_MEMBER]-(p1)"+
                    "WITH m, COUNT(*) AS num ORDER BY num DESC "+
                    "RETURN num, m.name;";
            Result res = session.run(query1);
            List<Record> list = res.stream().toList();
            for (Record r: list
            ) {
                resJson.add(gson.toJson(r.asMap()));
            }
        }
        close();
        return resJson;
    }

    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }
}
