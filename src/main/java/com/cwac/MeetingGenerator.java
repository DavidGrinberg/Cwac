package com.cwac;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.mongodb.morphia.Datastore;

import java.util.*;

/**
 * Created by David on 10/24/2015.
 */
public class MeetingGenerator {
    public static Attempt generate(Datastore cwacDatabase) {
        List<String> locations = cwacDatabase.getCollection(User.class).distinct("location");
        Attempt attempt = attemptToFindPairs(locations, cwacDatabase);

        cwacDatabase.save(attempt.meetings);
        cwacDatabase.save(attempt.users);

        return attempt;
    }

    private static Attempt attemptToFindPairs(List<String> locations, Datastore cwacDatabase) {
        List<Meeting>   paired = new ArrayList<>();
        List<User>      unpaired = new ArrayList<>();

        for(String location : locations){
            Attempt attempt = attemptToFindPairsAtLocation(location, cwacDatabase);
            System.out.println("Completed location " + location);
            paired.addAll(attempt.meetings);
            unpaired.addAll(attempt.users);
        }

        //Can probably save to database here to reduce memory footprint, but not needed for now
        return new Attempt(paired, unpaired);
    }

    private static Attempt attemptToFindPairsAtLocation(String location, Datastore cwacDatabase) {
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

                Meeting meeting = new Meeting(Arrays.asList(firstUser,secondUser), location);
                pairings.add(meeting);
                break;
            }
        }

        return new Attempt(pairings, activeUsersAtLocation);
    }

    public static class Attempt {
        public final List<Meeting> meetings;
        public final List<User> users;

        public Attempt(List<Meeting> meetings, List<User> users) {
            this.meetings = meetings;
            this.users = users;
        }
    }
}