package com.cwac.mongoDocs;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.util.Date;

/**
 * Created by David on 10/30/2015.
 */
@Entity("FailedMatches")
public class FailedMatch {
    @Id
    private final ObjectId id;
    private final String
            username,
            location,
            failureReason;
    private final Date failureDate;

    public FailedMatch(User user){
        this.id = new ObjectId();
        this.username = user.getUsername();
        this.location = user.getLocation();
        this.failureDate = new Date();
        this.failureReason = user.getFailureReason();
    }

    public ObjectId getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public Date getFailureDate() {
        return failureDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FailedMatch that = (FailedMatch) o;

        return id.equals(that.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
