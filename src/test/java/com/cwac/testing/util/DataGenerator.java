package com.cwac.testing.util;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by David on 11/1/2015.
 */
public class DataGenerator {
    private static final Random random = new Random();
    public static User createRandomUser(){
        return new User(RandomStringUtils.randomAlphabetic(random.nextInt(10)),
                RandomStringUtils.randomAlphabetic(random.nextInt(10)));
    }

    public static Set<User> createRandomUsers(int number) {
        return Stream.generate(DataGenerator::createRandomUser)
                .limit(random.nextInt(number) + 1)//to avoid 0
                .collect(Collectors.toSet());
    }

    public static Meeting createRandomMeeting(){
        Set<User> users = createRandomUsers(10);

        return new Meeting(users, RandomStringUtils.randomAlphabetic(random.nextInt(10)));
    }

    public static Set<Meeting> createRandomMeetings(int number) {
        return Stream.generate(DataGenerator::createRandomMeeting)
                .limit(random.nextInt(number) + 1)//to avoid 0
                .collect(Collectors.toSet());
    }
}
