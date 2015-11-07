package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.mongodb.morphia.Datastore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 */
public class ProposalGenerator {
    private final Datastore cwacDatastore;

    public ProposalGenerator(Datastore cwacDatastore) {
        this.cwacDatastore = cwacDatastore;
    }

    public Proposal proposeMeetings() {
        final List<String> locations = cwacDatastore.getCollection(User.class).distinct("location");
        Set<Meeting> meetings = new HashSet<>();
        Set<FailedMatch> failedMatches = new HashSet<>();

        for(String location : locations){
            Proposal proposal = proposeMeetingsAtLocation(location);
            System.out.println("Completed location " + location);
            meetings.addAll(proposal.meetings);
            failedMatches.addAll(proposal.failedMatches);
        }

        return new Proposal(meetings, failedMatches);
    }

    public Proposal proposeMeetingsAtLocation(String location) {
        List<User> activeUsersAtLocation =
                cwacDatastore.find(User.class)
                        .field("location").equal(location)
                        .field("isActive").equal(true)
                        .asList();
        Set<Meeting> pairings = new HashSet<>(activeUsersAtLocation.size() / 2);
        Collections.shuffle(activeUsersAtLocation);

        ListIterator<User>  leftIter = activeUsersAtLocation.listIterator();
        while(leftIter.hasNext()){
            User firstUser = leftIter.next();
            if(firstUser.hasFoundMeeting()){//TODO: double check if still active
                continue;
            }

            ListIterator<User> rightIter = activeUsersAtLocation.listIterator(leftIter.nextIndex());
            while(rightIter.hasNext()){
                User secondUser = rightIter.next();
                if(secondUser.hasFoundMeeting() || firstUser.hasMet(secondUser)){
                    continue;
                }

                Meeting meeting = new Meeting(new HashSet<>(Arrays.asList(firstUser,secondUser)), location);
                pairings.add(meeting);
                break;
            }
        }

        return new Proposal(pairings, extractFailedMatches(activeUsersAtLocation));
    }

    private Set<FailedMatch> extractFailedMatches(List<User> users) {
        return users.parallelStream().filter(user -> !user.hasFoundMeeting())
                .map(FailedMatch::new).collect(Collectors.toSet());
    }
}