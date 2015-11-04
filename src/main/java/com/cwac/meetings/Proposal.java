package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by David on 11/3/2015.
 */
public class Proposal {
    public final List<Meeting> meetings;
    public final List<User> users;
    public final List<FailedMatch> failedUsers;

    public Proposal(List<Meeting> meetings, List<User> users) {
        this.meetings = meetings;
        this.users = users;
        this.failedUsers = users.parallelStream().filter(user -> !user.hasFoundMeeting())
                .map(FailedMatch::new).collect(Collectors.toList());
    }
}
