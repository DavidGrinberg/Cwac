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

    public static Meeting createRandomMeeting(){
        Set<User> users = Stream.generate(DataGenerator::createRandomUser)
                                .limit(random.nextInt(10)+1)//to avoid 0
                                .collect(Collectors.toSet());

        return new Meeting(users, RandomStringUtils.randomAlphabetic(random.nextInt(10)));
    }
}
