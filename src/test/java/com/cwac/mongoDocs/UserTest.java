package com.cwac.mongoDocs;

import com.cwac.testing.util.DataGenerator;
import com.cwac.testing.util.FieldAccessTester;
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
    @Rule
    public final ExpectedException expectedException = ExpectedException.none();

    @Test
    public void testGettersAndSetters() throws Exception{
        User user = DataGenerator.createRandomUser();
        Map<String, String> accessMethodRenaming = new HashMap<>();
        accessMethodRenaming.put("getIsActive", "isActive");
        accessMethodRenaming.put("getFoundMeeting", "hasFoundMeeting");
        accessMethodRenaming.put("setUsername", null);
        accessMethodRenaming.put("setHistory", null);
        accessMethodRenaming.put("setVersion", null);
        accessMethodRenaming.put("setLogger", null);
        accessMethodRenaming.put("getLogger", null);
        Map<String, Object> nonInstantiableFieldsDefaultValues = new HashMap<>();
        nonInstantiableFieldsDefaultValues.put("java.util.List", new ArrayList<>());

        FieldAccessTester fieldAccessTester = new FieldAccessTester(user).setAccessMethodRenaming(accessMethodRenaming)
                .setNonInstantiableFieldsDefaultValues(nonInstantiableFieldsDefaultValues);
        fieldAccessTester.run();
    }

    @Test
    public void testAddToHistoryShouldTrackMeetingId() {
        User user = DataGenerator.createRandomUser();
        assertEquals(user.getHistory().size(), 0);

        Meeting expectedMeeting = new Meeting(new HashSet<>(Arrays.asList(user)), "testLocation");
        assertEquals(user.getHistory().size(), 1);

        ObjectId actualMeetingId = user.getHistory().get(0);
        assertEquals(actualMeetingId, expectedMeeting.getId());
    }

    @Test
    public void testAddToHistoryShouldThrowExceptionIfUserNotInMeeting(){
        User userInMeeting = DataGenerator.createRandomUser(),
             userNotInMeeting = DataGenerator.createRandomUser();

        Meeting meeting = new Meeting(new HashSet<>(Arrays.asList(userInMeeting)), "testLocation");

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("User must be attendee of meeting for meeting to be added to history");
        userNotInMeeting.addToHistory(meeting);

        assertEquals(userNotInMeeting.getHistory().size(), 0);
    }

    @Test
    public void testAddToHistoryShouldNotAllowAddingSameMeetingTwice(){
        User user = DataGenerator.createRandomUser();
        Meeting meeting = new Meeting(new HashSet<>(Arrays.asList(user)), "testLocation");
        assertEquals(user.getHistory().size(), 1);
        assertEquals(user.getHistory().get(0), meeting.getId());
        user.addToHistory(meeting);
        assertEquals(user.getHistory().size(), 1);
        assertEquals(user.getHistory().get(0), meeting.getId());
    }

    @Test
    public void testHasMet() throws Exception {
        User user1 = DataGenerator.createRandomUser(),
             user2 = DataGenerator.createRandomUser(),
             user3 = DataGenerator.createRandomUser();

        new Meeting(new HashSet<>(Arrays.asList(user1, user2)), "testLocation");

        //User 1 and 2 met each other
        assertTrue(user1.hasMet(user2));
        assertTrue(user2.hasMet(user1));

        //No one was in a meeting with User 3
        assertFalse(user1.hasMet(user3));
        assertFalse(user3.hasMet(user1));
        assertFalse(user2.hasMet(user3));
        assertFalse(user3.hasMet(user2));
    }

    @Test
    public void testDefaultConstructor(){
        new User();
    }
}