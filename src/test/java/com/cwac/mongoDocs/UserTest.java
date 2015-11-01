package com.cwac.mongoDocs;

import com.cwac.testing.util.GetterSetterReflectionTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.bson.types.ObjectId;
import org.junit.*;
import org.junit.rules.ExpectedException;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by David on 10/31/2015.
 */
public class UserTest {
    final Random random = new Random();
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    private User createRandomUser(){
        User user = new User(RandomStringUtils.randomAlphabetic(random.nextInt(10)),
                            RandomStringUtils.randomAlphabetic(random.nextInt(10)));

        return user;
    }

    @Test
    public void testGettersAndSetters() throws Exception{
        User user = createRandomUser();
        Map<String, String> accessMethodRenaming = new HashMap<>();
        accessMethodRenaming.put("getIsActive", "isActive");
        accessMethodRenaming.put("getFoundMeeting", "hasFoundMeeting");
        GetterSetterReflectionTest.run(user, accessMethodRenaming);
    }

    @Test
    public void testAddToHistoryShouldTrackMeetingId() {
        User user = createRandomUser();
        assertEquals(user.getHistory().size(), 0);

        Meeting expectedMeeting = new Meeting(Arrays.asList(user), "testLocation");
        assertEquals(user.getHistory().size(), 1);

        ObjectId actualMeetingId = user.getHistory().get(0);
        assertEquals(actualMeetingId, expectedMeeting.getId());
    }

    @Test
    public void testAddToHistoryShouldThrowExceptionIfUserNotInMeeting(){
        User userInMeeting = createRandomUser(),
             userNotInMeeting = createRandomUser();

        Meeting meeting = new Meeting(Arrays.asList(userInMeeting), "testLocation");

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User must be attendee of meeting for meeting to be added to history");
        userNotInMeeting.addToHistory(meeting);

        assertEquals(userNotInMeeting.getHistory().size(), 0);
    }

    @Test
    public void testHasMet() throws Exception {
        User user1 = createRandomUser(),
             user2 = createRandomUser(),
             user3 = createRandomUser();

        new Meeting(Arrays.asList(user1, user2), "testLocation");

        assertTrue(user1.hasMet(user2));
        assertTrue(user2.hasMet(user1));

        assertFalse(user1.hasMet(user3));
        assertFalse(user3.hasMet(user1));
        assertFalse(user2.hasMet(user3));
        assertFalse(user3.hasMet(user2));
    }
}