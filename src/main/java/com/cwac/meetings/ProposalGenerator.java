package com.cwac.meetings;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.mongodb.morphia.Datastore;

import java.util.*;

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
        Set<Meeting> paired = new HashSet<>();
        Set<User> unpaired = new HashSet<>();

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
        List<User> activeUsersAtLocation =
                cwacDatabase.find(User.class)
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

        return new Proposal(pairings, new HashSet<>(activeUsersAtLocation));
    }
}