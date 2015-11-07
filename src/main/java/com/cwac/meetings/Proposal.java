package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Set;

/**
 * Created by David on 11/3/2015.
 */
public class Proposal {
    public final Set<Meeting> meetings;
    public final Set<FailedMatch> failedMatches;

    public Proposal(Set<Meeting> meetings, Set<FailedMatch> failedMatches) {
        this.meetings = meetings;
        this.failedMatches = failedMatches;
    }

    public void acceptAndSave(AdvancedDatastore datastore) {
        meetings.parallelStream().forEach(meeting -> {
            datastore.insert(meeting);
            meeting.getAttendeeUsernames().parallelStream().forEach(username -> {
                final Query<User> usernameQuery = datastore.createQuery(User.class).field(Mapper.ID_KEY).equal(username);
                final UpdateOperations<User> addToMeetingToHistory =
                        datastore.createUpdateOperations(User.class).add("history", meeting, false);
                datastore.update(usernameQuery, addToMeetingToHistory);
            });
        });
        datastore.insert(failedMatches.toArray());
    }
}
