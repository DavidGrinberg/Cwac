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
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

/**
 * Created by David on 10/25/2015.
 */
public class CwacDemoApp {
    public static void main(String[] args) {
        final Morphia morphia = new Morphia();

        // tell morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("com.cwac.mongoDocs");

        // create the Datastore connecting to the database running on the default port on the local host
        final Datastore cwacDb = morphia.createDatastore(new MongoClient(), "cwac");


        List<User> users = null;
        boolean createdUsers = false;
        while(!createdUsers)
        try {
            cwacDb.getDB().dropDatabase();
            MockDataGenerator.createDataInDb(cwacDb);
            createdUsers = true;
            System.out.println("Added users");
        } catch (MongoBulkWriteException e) {
            System.out.println("Failed to create users due to to duplicate keys");
        }

        MeetingGenerator.generate(cwacDb);
        System.out.println("Done");
    }
}
