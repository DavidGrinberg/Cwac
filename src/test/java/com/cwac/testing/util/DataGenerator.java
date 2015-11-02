package com.cwac.testing.util;

import com.cwac.mongoDocs.User;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Random;

/**
 * Created by David on 11/1/2015.
 */
public class DataGenerator {
    private static final Random random = new Random();
    public static User createRandomUser(){
        return new User(RandomStringUtils.randomAlphabetic(random.nextInt(10)),
                RandomStringUtils.randomAlphabetic(random.nextInt(10)));
    }
}
