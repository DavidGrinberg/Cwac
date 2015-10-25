package com.cwac;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created by David on 10/24/2015.
 */
public class test {
    public static void main(String[] args) {
        User user = new User("a", new ArrayList<>(),true,"as");
        MongoClient client = new MongoClient();
        MongoDatabase cwacDatabase = client.getDatabase("cwac");
        MongoCollection<Document> userCollection = cwacDatabase.getCollection("users");
        userCollection.insertOne(user);
    }

}
