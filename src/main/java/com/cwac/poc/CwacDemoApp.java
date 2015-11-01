package com.cwac.poc;

import com.cwac.MeetingGenerator;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClient;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.List;

/**
 * Created by David on 10/25/2015.
 */
public class CwacDemoApp {
    public static void main(String[] args) {
        final Morphia morphia = new Morphia();
        morphia.mapPackage("com.cwac.mongoDocs");
        final AdvancedDatastore cwacDb = (AdvancedDatastore) morphia.createDatastore(new MongoClient(), "cwac");

        boolean createdUsers = false;
        while(!createdUsers){
            try {
                cwacDb.getDB().dropDatabase();
                MeetingGenerator.Attempt attempt = MockDataGenerator.createData();
                cwacDb.save(attempt.meetings);
                cwacDb.save(attempt.users);
                cwacDb.save("FailedUsedTest", attempt.users);
                createdUsers = true;
                System.out.println("Added users");
            } catch (MongoBulkWriteException e) {
                System.out.println("Failed to create users due to to duplicate keys");
            }
        }

        MeetingGenerator.generate(cwacDb);
        System.out.println("Done");
    }
}
