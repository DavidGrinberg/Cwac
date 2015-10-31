package com.cwac.poc;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by David on 10/24/2015.
 */
public class MockDataGenerator {
    private static final int
        NUM_LOCATIONS = 1,
        NUM_USERS = 100,
        NUM_HISTORY_EVENTS = 5,
        LOCATION_LENGTH = 3,
        USERNAME_LENGTH = 6;
    private static final double DEACTIVATED_CHANCE = 0;

    public static void createDataInDb(Datastore datastore) {
        List<String> locations = Stream.generate(() -> RandomStringUtils.randomAlphabetic(LOCATION_LENGTH).toLowerCase())
                .limit(NUM_LOCATIONS).collect(Collectors.toList());
        List<User> users= Stream.generate(() -> new User(RandomStringUtils.randomAlphabetic(USERNAME_LENGTH).toLowerCase(),
                                                        locations.get((int) (Math.random() * NUM_LOCATIONS))))
                                .limit(NUM_USERS).collect(Collectors.toList());
        users.parallelStream().forEach((user) -> user.setIsActive(Math.random() > DEACTIVATED_CHANCE));

        List<Meeting> meetings = createRandomHistories(users, locations);

        datastore.save(users);
        datastore.save(meetings);
    }

    private static List<Meeting> createRandomHistories(List<User> users, List<String> locations) {
        List<Meeting> meetings = new ArrayList<>(NUM_HISTORY_EVENTS);
        for(int i = 0; i < NUM_HISTORY_EVENTS; i++) {
            int userIndex1 = (int)(Math.random() * NUM_USERS),
                userIndex2 = (int)(Math.random() * NUM_USERS);
            if(userIndex1==userIndex2)userIndex2++;

            User firstUser = users.get(userIndex1),
                    secondUser = users.get(userIndex2);
            Meeting meeting = new Meeting(Arrays.asList(firstUser, secondUser),
                    locations.get((int)(Math.random() * NUM_LOCATIONS)));
            meetings.add(meeting);
        }
        return meetings;
    }
}
