package com.cwac;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by David on 10/24/2015.
 */
public class MeetingGenerator {
    public static void generate(MongoDatabase cwacDatabase) {
        MongoCollection<Document>   userCollection = cwacDatabase.getCollection(MongoCatalog.Collections.USERS),
                meetingCollection = cwacDatabase.getCollection(MongoCatalog.Collections.MEETINGS),
                failedAttemptCollection = cwacDatabase.getCollection(MongoCatalog.Collections.FAILED_PAIRING_ATTEMPS);

        ArrayList<String> locations = userCollection.distinct("location", String.class).into(new ArrayList<>());
        Document pairingAttempt = attemptToFindPairs(locations, userCollection);

        meetingCollection.insertMany(pairingAttempt.get("paired", ArrayList.class));
        ArrayList unpaired = pairingAttempt.get("unpaired", ArrayList.class);
        if(unpaired.size()!=0){
            failedAttemptCollection.insertMany(unpaired);
        }
    }

    private static Document attemptToFindPairs(ArrayList<String> locations, MongoCollection<Document> userCollection) {
        ArrayList<Document> paired = new ArrayList<>(),
                            unpaired = new ArrayList<>();

        for(String location : locations){
            Document pairingAttempt = attemptToFindPairsAtLocation(location, userCollection);
            System.out.println("Completed location " + location);
            paired.addAll(pairingAttempt.get("paired", ArrayList.class));
            unpaired.addAll(pairingAttempt.get("unpaired", ArrayList.class));
        }

        Document pairingAttempt = new Document();
        pairingAttempt.append("paired", paired)
                .append("unpaired", unpaired);
        return pairingAttempt;
    }

    private static Document attemptToFindPairsAtLocation(String location, MongoCollection<Document> userCollection) {
        final String foundPair = "foundPair";
        ArrayList<Document> activeUsersAtLocation = userCollection.find(and(eq("location", location), eq("isActive", true))).into(new ArrayList<>()),
                            pairings = new ArrayList<>(activeUsersAtLocation.size() / 2),
                            unpaired = new ArrayList<>();

        Collections.shuffle(activeUsersAtLocation);

        ListIterator<Document>  leftIter = activeUsersAtLocation.listIterator();
        while(leftIter.hasNext()){
            User firstUser = new User(leftIter.next());
            if(firstUser.getBoolean(foundPair, false)){//TODO: double check if still active
                continue;
            }

            ListIterator<Document> rightIter = activeUsersAtLocation.listIterator(leftIter.nextIndex());
            while(rightIter.hasNext()){
                User secondUser = new User(rightIter.next());
                if(secondUser.getBoolean(foundPair, false) || firstUser.hasMet(secondUser)){
                    continue;
                }

                Meeting meeting = new Meeting(firstUser.getString("_id"), secondUser.getString("_id"), location);

                firstUser.append(foundPair, true);
                secondUser.append(foundPair, true);
                pairings.add(meeting);
                break;
            }

            if(!firstUser.getBoolean(foundPair, false)){
                firstUser.append(foundPair, false);
                unpaired.add(firstUser);
            }
        }
        Document pairingAttempt = new Document();
        pairingAttempt.append("paired", pairings)
                        .append("unpaired", unpaired);
        return pairingAttempt;
    }

}