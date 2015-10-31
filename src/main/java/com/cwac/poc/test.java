package com.cwac.poc;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Arrays;
import java.util.List;

/**
 * Created by David on 10/24/2015.
 */
public class test {
    public static void main(String[] args) {
        final Morphia morphia = new Morphia();

        // tell morphia where to find your classes
        // can be called multiple times with different packages or classes
        morphia.mapPackage("com.cwac.mongoDocs");

        // create the Datastore connecting to the database running on the default port on the local host
        final Datastore datastore = morphia.createDatastore(new MongoClient(), "cwac");
        datastore.getDB().dropDatabase();
        User firstUser = new User("david2", "nyc"),
            secondUser = new User("matt", "nyc");
        List<User> users = Arrays.asList(firstUser, secondUser);
        Meeting meeting = new Meeting(users, "nyc" );

        datastore.save(users);
        datastore.save(meeting);
    }
}
