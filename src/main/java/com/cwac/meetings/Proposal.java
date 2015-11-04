package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by David on 11/3/2015.
 */
public class Proposal {
    public final Set<Meeting> meetings;
    public final Set<User> users;
    public final Set<FailedMatch> failedMatches;

    public Proposal(Set<Meeting> meetings, Set<User> users) {
        this.meetings = meetings;
        this.users = users;
        this.failedMatches = users.parallelStream().filter(user -> !user.hasFoundMeeting())
                .map(FailedMatch::new).collect(Collectors.toSet());
    }
}
