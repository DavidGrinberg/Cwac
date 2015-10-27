package com.cwac;

import com.cwac.mongoDocs.User;
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
        User user = new User(name,
                new ArrayList<>(),
                location);

        return user;
    }

    @Test
    public void UserShouldBehaveLikeRegularDocument(){
        User user = createRandomUser();

        Document document = user;
        assertEquals("Document ID is the user's kerb", document.getString("_id"), user.getUser());
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
        assertEquals("Kerberos is the ID", user.getUser(), user.getString("_id"));
    }
}
