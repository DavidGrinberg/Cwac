package com.cwac.mongoDocs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 */
@Entity
public class Meeting {
    @Id
    private ObjectId id;
    private List<String> attendees;
    private String location;
    private boolean occurred;
    private Date creationDate;

    public Meeting(List<User> attendees, String location) {
        this.id = new ObjectId();
        this.attendees = extractUsernamesFromAtendees(attendees);
        this.location = location;
        this.occurred = true;
        this.creationDate = new Date();
        updateAttendees(attendees);
    }

    private void updateAttendees(List<User> attendees) {
        attendees.parallelStream().forEach(attendee ->{
            attendee.addToHistory(this);
            attendee.setFoundMeeting(true);
        });
    }

    private List<String> extractUsernamesFromAtendees(List<User> attendees){
        return attendees.parallelStream().map(User::getUsername).collect(Collectors.toList());
    }

    public List<String> getAttendeesUsernames() {
        return attendees;
    }

    public void setAttendees(List<User> attendees) {
        this.attendees = extractUsernamesFromAtendees(attendees);
    }

    public boolean isOccurred() {
        return occurred;
    }

    public void setOccurred(boolean occurred) {
        this.occurred = occurred;
    }

    public ObjectId getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public Date getCreationDate() {
        return creationDate;
    }
}
