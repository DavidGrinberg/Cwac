package com.cwac.mongoDocs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Version;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 * Represents the meeting of multiple users (normally just 2) at a single date and location.
 */
@Entity("Meetings")
public class Meeting {
    @Id
    private ObjectId id;
    private Set<String> attendeeUsernames;
    private String location;
    private boolean occurred;
    private Date creationDate;
    @Version
    long version;

    //Used by Morphia, do not call
    public Meeting(){}

    public Meeting(Set<User> attendees, String location) {
        this.id = new ObjectId();
        this.attendeeUsernames = extractUsernamesFromAtendees(attendees);
        this.location = location;
        this.occurred = true;
        this.creationDate = new Date();
        updateAttendees(attendees);
    }

    private void updateAttendees(Set<User> attendees) {
        attendees.parallelStream().forEach(attendee ->{
            attendee.addToHistory(this);
            attendee.setFoundMeeting(true);
        });
    }

    private Set<String> extractUsernamesFromAtendees(Set<User> attendees){
        return attendees.parallelStream().map(User::getUsername).collect(Collectors.toSet());
    }

    public Set<String> getAttendeesUsernames() {
        return attendeeUsernames;
    }

    public void setAttendees(Set<User> attendees) {
        this.attendeeUsernames = extractUsernamesFromAtendees(attendees);
    }

    public boolean hasOccurred() {
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
