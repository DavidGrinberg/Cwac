package com.cwac.poc;

import com.cwac.codec.CwacCodecProvider;
import com.cwac.mongoDocs.User;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.util.ArrayList;

import static com.mongodb.client.model.Filters.*;

/**
 * Created by David on 10/24/2015.
 */
public class test {
    public static void main(String[] args) {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new CwacCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClient mongoClient = new MongoClient();

        MongoDatabase cwacDatabase = mongoClient.getDatabase("cwac");
        MongoCollection<User> userCollection = cwacDatabase.getCollection("users", User.class).withCodecRegistry(codecRegistry);

        Bson userFilter = eq("_id", "a");
        DeleteResult deleteResult = userCollection.deleteOne(userFilter);
        System.out.println("deleteResult = " + deleteResult);

        User user = new User("a", new ArrayList<>(),"as");
        userCollection.insertOne(user);
        System.out.println("Inserted user");

        Document foundUser = userCollection.find(userFilter).first();
        System.out.println("foundUser normal = " + foundUser);

        user.append("test", "hi");
        userCollection.replaceOne(userFilter, user);

        foundUser = userCollection.find(userFilter).first();
        System.out.println("foundUser after appending = " + foundUser);

        user.remove("location");
        userCollection.replaceOne(userFilter, user);

        foundUser = userCollection.find(userFilter).first();
        System.out.println("foundUser after removing = " + foundUser);

        deleteResult = userCollection.deleteOne(userFilter);
        System.out.println("deleteResult = " + deleteResult);
    }
}
