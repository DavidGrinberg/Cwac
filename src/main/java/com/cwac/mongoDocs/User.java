package com.cwac.mongoDocs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.annotations.Version;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by David on 10/30/2015.
 */
@Entity("Users")
public class User {
    @Transient
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Version
    long version;
    @Id
    private String  username;
    private String  location;
    private boolean isActive;
    private List<ObjectId> history;
    @Transient
    private boolean foundMeeting = false;
    @Transient
    private String failureReason = "";

    //Should not be called, only used by Morphia for data translation
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

    public void addToHistory(Meeting meeting){
        if(!meeting.getAttendeeUsernames().contains(this.getUsername())){
            throw new IllegalArgumentException("User must be attendee of meeting for meeting to be added to history");
        }

        ObjectId meetingId = meeting.getId();
        if(this.history.contains(meetingId)){
            logger.warn("Attempting to add meeting {} to user {}'s history twice. Second add ignored.",
                    meetingId, this.getUsername());
            return;
        }
        this.history.add(meetingId);
    }

    public boolean hasMet(User secondUser) {
        return !Collections.disjoint(this.getHistory(), secondUser.getHistory());
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public long getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return username.equals(user.username);

    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
