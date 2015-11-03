package com.cwac.meetings;

import com.cwac.mongoDocs.*;
import org.mongodb.morphia.Datastore;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 */
public class ProposalGenerator {
    public static Proposal generate(Datastore cwacDatabase) {
        List<String> locations = cwacDatabase.getCollection(User.class).distinct("location");
        Proposal proposal = proposeMeetings(locations, cwacDatabase);

        cwacDatabase.save(proposal.meetings);
        cwacDatabase.save(proposal.users);

        return proposal;
    }

    public static Proposal proposeMeetings(List<String> locations, Datastore cwacDatabase) {
        List<Meeting>   paired = new ArrayList<>();
        List<User>      unpaired = new ArrayList<>();

        for(String location : locations){
            Proposal proposal = proposeMeetingsAtLocation(location, cwacDatabase);
            System.out.println("Completed location " + location);
            paired.addAll(proposal.meetings);
            unpaired.addAll(proposal.users);
        }

        //Can probably save to database here to reduce memory footprint, but not needed for now
        return new Proposal(paired, unpaired);
    }

    public static Proposal proposeMeetingsAtLocation(String location, Datastore cwacDatabase) {
        List<User> activeUsersAtLocation = cwacDatabase.find(User.class).field("location").equal(location).field("isActive").equal(true).asList();
        List<Meeting> pairings = new ArrayList<>(activeUsersAtLocation.size() / 2);
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

        return new Proposal(pairings, activeUsersAtLocation);
    }

    public static class Proposal {
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
}