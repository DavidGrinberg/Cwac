package com.cwac.poc;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 10/24/2015.
 */
public class UserCreation {
    private static final int
        NUM_LOCATIONS = 1,
        NUM_USERS = 100,
        NUM_HISTORY_EVENTS = 3,
        LOCATION_LENGTH = 3,
        USERNAME_LENGTH = 6;
    private static final double DEACTIVATED_CHANCE = 0;

    public static List<User> createUsers() {
        List<String> locations = new ArrayList<>(NUM_LOCATIONS);
        List<User> users= new ArrayList<>(NUM_USERS);

        for(int i=0; i<NUM_LOCATIONS; i++){
            locations.add(RandomStringUtils.randomAlphabetic(LOCATION_LENGTH).toLowerCase());
        }

        for(int i=0; i<NUM_USERS; i++){
            User user = new User(RandomStringUtils.randomAlphabetic(USERNAME_LENGTH).toLowerCase(),
                                new ArrayList<>(),
                                locations.get((int) (Math.random() * NUM_LOCATIONS)));

            users.add(user);
        }
        createRandomHistories(users, locations);
        deactivateRandomUsers(users);

        return users;
    }

    private static void deactivateRandomUsers(List<User> users) {
        for(User user : users){
            if(Math.random() < DEACTIVATED_CHANCE){
                user.setIsActive(false);
            }
        }
    }

    private static void createRandomHistories(List<User> users, List<String> locations) {
        int usersSize = users.size();
        for(int i = 0; i < NUM_HISTORY_EVENTS; i++) {
            int userIndex1 = (int)(Math.random() * usersSize),
                userIndex2 = (int)(Math.random() * usersSize);
            if(userIndex1==userIndex2)userIndex2++;

            User firstUser = users.get(userIndex1),
                    secondUser = users.get(userIndex2);
            Meeting meeting = new Meeting(firstUser.getUser(), secondUser.getUser(),
                    locations.get((int)(Math.random() * locations.size())));

            firstUser.addToHistory(meeting);
            secondUser.addToHistory(meeting);
        }
    }
}
