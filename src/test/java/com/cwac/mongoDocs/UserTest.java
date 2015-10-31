package com.cwac.mongoDocs;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * Created by David on 10/31/2015.
 */
public class UserTest {
    Random random = new Random();

    @Test
    public void testGetUsername() throws Exception {
        String  name = RandomStringUtils.randomAlphabetic(random.nextInt(100)),
                location = RandomStringUtils.randomAlphabetic(random.nextInt(100));
        User user = new User(name, location);

        assertEquals(name, user.getUsername());
    }

    @Test
    public void testSetUsername() throws Exception {

    }

    @Test
    public void testHasFoundMeeting() throws Exception {

    }

    @Test
    public void testSetFoundMeeting() throws Exception {

    }

    @Test
    public void testGetLocation() throws Exception {
        String  name = RandomStringUtils.randomAlphabetic(random.nextInt(100)),
                location = RandomStringUtils.randomAlphabetic(random.nextInt(100));
        User user = new User(name, location);

        assertEquals(location, user.getLocation());
    }

    @Test
    public void testSetLocation() throws Exception {

    }

    @Test
    public void testIsActive() throws Exception {

    }

    @Test
    public void testSetIsActive() throws Exception {

    }

    @Test
    public void testGetHistory() throws Exception {

    }

    @Test
    public void testSetHistory() throws Exception {

    }

    @Test
    public void testAddToHistory() throws Exception {

    }

    @Test
    public void testHasMet() throws Exception {

    }
}