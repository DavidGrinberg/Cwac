package com.cwac;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 10/24/2015.
 */
public class UserCreation {
    public static List<User> createUsers() {
        int numLocations = 30, numUsers = 20000;
        List<String> locations = new ArrayList<>(numLocations);
        List<User> users= new ArrayList<>(numUsers);

        for(int i=0; i<numLocations; i++){
            locations.add(RandomStringUtils.randomAlphabetic(3).toLowerCase());
        }

        for(int i=0; i<numUsers; i++){
            User user = new User(RandomStringUtils.randomAlphabetic(6).toLowerCase(),
                                new ArrayList<>(),
                                Math.random() > .3,
                                locations.get((int) (Math.random() * numLocations)));

            users.add(user);
        }
        createHistory(users);

        return users;
    }

    private static void createHistory(List<User> users) {
        int usersSize = users.size();
        for(int i = 0; i < 50000; i++) {
            int userIndex1 = (int)(Math.random() * usersSize),
                    userIndex2 = (int)(Math.random() * usersSize);
            if(userIndex1==userIndex2)userIndex2++;

            User firstUser = users.get(userIndex1),
                    secondUser = users.get(userIndex2);
            Pair pair = new Pair(firstUser.getKerberos(), secondUser.getKerberos(), "xxx", new Date());
            firstUser.addToHistory(pair);
            secondUser.addToHistory(pair);
        }
    }
}
