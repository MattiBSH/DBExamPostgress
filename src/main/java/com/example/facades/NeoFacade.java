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
            for (Team t: a.getTeamsParticipated()
            ) {
                session.writeTransaction(tx -> tx.run("" +
                                "Merge (t:Team {name:$x})" +
                                "Merge (a:Event {name:$z, type:$b})" +
                                "MERGE (a)-[m:PARTICIPANT {participated: $y}]-(t)",
                        parameters("x", t.getName(), "y", LocalDateTime.now(),
                                "z", a.getName(), "b", a.getType())));
            }
        }
        close();
    }

    public void close()
    {
        // Closing a driver immediately shuts down all open connections.
        driver.close();
    }
}
