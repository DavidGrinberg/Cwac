package com.cwac.poc;

import com.cwac.MeetingGenerator;
import com.cwac.MongoCatalog;
import com.cwac.codec.CwacCodecProvider;
import com.cwac.mongoDocs.User;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.List;

/**
 * Created by David on 10/25/2015.
 */
public class App {
    public static void main(String[] args) {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new CwacCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClient client = new MongoClient();
        MongoDatabase cwacDatabase = client.getDatabase(MongoCatalog.Databases.CWAC).withCodecRegistry(codecRegistry);
        MongoCollection<Document> userCollection = cwacDatabase.getCollection(MongoCatalog.Collections.USERS),
                meetingCollection = cwacDatabase.getCollection(MongoCatalog.Collections.MEETINGS),
                failedPairCollection = cwacDatabase.getCollection(MongoCatalog.Collections.FAILED_PAIRING_ATTEMPS);
        //for the POC, clean run every time
        userCollection.drop();
        meetingCollection.drop();
        failedPairCollection.drop();

        List<User> users = null;
        boolean createdUsers = false;
        while(!createdUsers)
        try {
            users = UserCreation.createUsers();
            userCollection.insertMany(users);
            createdUsers = true;
            System.out.println("Added users");
        } catch (MongoBulkWriteException e) {
            System.out.println("Failed to create users due to to duplicate keys");
        }

        MeetingGenerator.generate(cwacDatabase);
        System.out.println("Done");
    }
}
