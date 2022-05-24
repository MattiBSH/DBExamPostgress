package com.example.models;

import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;

@Entity
@Table(	name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Arrangement> events = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    private List<User> members = new ArrayList<>();

    public Team() {
    }
    public Team(String name, ArrayList<User> members) {
        this.name=name;
        this.members=members;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Arrangement> getEvents() {
        return (ArrayList<Arrangement>) events;
    }

    public ArrayList<User> getMembers() {
        return (ArrayList<User>) members;
    }

    public void setMembers(ArrayList<User> members) {
        this.members = members;
    }
}
