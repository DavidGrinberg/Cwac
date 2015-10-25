package com.cwac;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.*;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by David on 10/24/2015.
 */
public class PairFinder {
    public static void main(String[] args) {
        MongoClient client = new MongoClient();
        MongoDatabase cwacDatabase = client.getDatabase("cwac");
        MongoCollection<Document>   userCollection = cwacDatabase.getCollection("users"),
                                pairCollection = cwacDatabase.getCollection("pairs"),
                                    failedPairCollection = cwacDatabase.getCollection("failedPairs");
        //for the POC, clean run every time
        userCollection.drop();
        pairCollection.drop();
        failedPairCollection.drop();
        List<User> users = UserCreation.createUsers();
        userCollection.insertMany(users);
        System.out.println("Added users");

        //group by location
        ArrayList<String> locations = userCollection.distinct("location", String.class).into(new ArrayList<>());
        Document pairingAttempt = createPairs(locations, userCollection);

        pairCollection.insertMany(pairingAttempt.get("paired", ArrayList.class));
        ArrayList unpaired = pairingAttempt.get("unpaired", ArrayList.class);
        if(unpaired.size()!=0){
            failedPairCollection.insertMany(unpaired);
        }
    }

    private static Document createPairs(ArrayList<String> locations, MongoCollection<Document> userCollection) {
        ArrayList<Document> paired = new ArrayList<>(),
                            unpaired = new ArrayList<>();

        for(String location : locations){
            Document pairingAttempt = createPairsAtLocation(location, userCollection);
            System.out.println("Completed location " + location);
            paired.addAll(pairingAttempt.get("pairings", ArrayList.class));
            unpaired.addAll(pairingAttempt.get("unpaired", ArrayList.class));
        }

        Document pairingAttempt = new Document();
        pairingAttempt.append("paired", paired)
                .append("unpaired", unpaired);
        return pairingAttempt;
    }

    private static Document createPairsAtLocation(String location, MongoCollection<Document> userCollection) {
        final String foundPair = "foundPair";
        ArrayList<Document> activeUsersAtLocation = userCollection.find(and(eq("location", location), eq("isActive", true))).into(new ArrayList<>()),
                            pairings = new ArrayList<>(activeUsersAtLocation.size() / 2),
                            unpaired = new ArrayList<>();

        Collections.shuffle(activeUsersAtLocation);

        ListIterator<Document>  leftIter = activeUsersAtLocation.listIterator();
        while(leftIter.hasNext()){
            User firstUser = (User) leftIter.next();
            if(firstUser.getBoolean(foundPair, false)){//TODO: double check if still active
                continue;
            }

            ListIterator<Document> rightIter = activeUsersAtLocation.listIterator(leftIter.nextIndex());
            while(rightIter.hasNext()){
                User secondUser = (User) rightIter.next();
                if(secondUser.getBoolean(foundPair, false) || firstUser.hasMet(secondUser)){
                    continue;
                }

                Pair pair = new Pair(firstUser.getString("_id"), secondUser.getString("_id"), location, new Date());

                firstUser.append(foundPair, true);
                secondUser.append(foundPair, true);
                pairings.add(pair);
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