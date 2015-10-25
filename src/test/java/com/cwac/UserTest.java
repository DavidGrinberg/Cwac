package com.cwac;

import org.apache.commons.lang3.RandomStringUtils;
import org.bson.Document;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by David on 10/25/2015.
 */
public class UserTest {

    private User createRandomUser(){
        Random random = new Random();
        String name = RandomStringUtils.randomAlphabetic(random.nextInt(100)),
                location = RandomStringUtils.randomAlphabetic(random.nextInt(100));
        boolean isActive = Math.random() > 5;
        User user = new User(name,
                new ArrayList<>(),
                isActive,
                location);

        return user;
    }

    @Test
    public void UserShouldBehaveLikeRegularDocument(){
        User user = createRandomUser();

        Document document = user;
        assertEquals("Document ID is the user's kerb", document.getString("_id"), user.getKerberos());
    }

    @Test
    public void testGetHistory() throws Exception {

    }

    @Test
    public void testAddToHistory() throws Exception {

    }

    @Test
    public void testHasMet() throws Exception {

    }

    @Test
    public void testGetUsersMet() throws Exception {

    }

    @Test
    public void testGetKerberos() throws Exception {
        User user = createRandomUser();
        assertEquals("Kerberos is the ID", user.getKerberos(), user.getString("_id"));
    }
}
