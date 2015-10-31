package com.cwac.mongoDocs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 10/30/2015.
 */
@Entity("Users")
public class User {
    @Id
    private String username;
    private String location;
    private boolean isActive;
    private List<ObjectId> history = new ArrayList<>();
    @Transient
    private boolean foundMeeting = false;

    public User(){}

    public User(String username, String location, List<ObjectId> history) {
        this.username = username;
        this.location = location;
        this.history = history;
        this.isActive = true;
    }

    public User(String username, String location){
        this(username, location, new ArrayList<>());
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean hasFoundMeeting() {
        return foundMeeting;
    }

    public void setFoundMeeting(boolean foundMeeting) {
        this.foundMeeting = foundMeeting;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public List<ObjectId> getHistory() {
        return history;
    }

    public void setHistory(List<ObjectId> history) {
        this.history = history;
    }

    public void addToHistory(Meeting meeting){
        this.history.add(meeting.getId());
    }

    public boolean hasMet(User secondUser) {
        return !Collections.disjoint(this.getHistory(), secondUser.getHistory());
    }
}
