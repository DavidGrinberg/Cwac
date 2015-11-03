package com.cwac.mongoDocs;

import com.cwac.testing.util.DataGenerator;
import com.cwac.testing.util.FieldAccessTester;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
/**
 * Created by David on 11/2/2015.
 */
public class MeetingTest {
    @Test
    public void testGettersAndSetters() throws Exception{
        Meeting meeting = DataGenerator.createRandomMeeting();
        Map<String, String> methodRenaming = new HashMap<>();
        methodRenaming.put("getOccurred", "hasOccurred");
        FieldAccessTester fieldAccessTester = new FieldAccessTester(meeting).setTestSetters(false)
                .setAccessMethodRenaming(methodRenaming);

        fieldAccessTester.run();
    }

    @Test
    public void testSetOccurred(){
        Meeting meeting = DataGenerator.createRandomMeeting();
        assertTrue(meeting.hasOccurred());

        meeting.setOccurred(false);
        assertFalse(meeting.hasOccurred());

        meeting.setOccurred(true);
        assertTrue(meeting.hasOccurred());
    }

    @Test
    public void testModifyAtendees(){
        Meeting meeting = DataGenerator.createRandomMeeting();
        String[] attendeeUsernames = meeting.getAttendeeUsernames().toArray(new String[0]);

        User firstUser = new User(attendeeUsernames[0], "testLocation");
        meeting.removeAttendee(firstUser);
        Set<String> newAttendeeUsernames = Arrays.stream(attendeeUsernames).skip(1).collect(Collectors.toSet());
        assertEquals(newAttendeeUsernames, meeting.getAttendeeUsernames());

        User secondUser = DataGenerator.createRandomUser();
        meeting.addAttendee(secondUser);
        newAttendeeUsernames.add(secondUser.getUsername());
        assertEquals(newAttendeeUsernames, meeting.getAttendeeUsernames());

        Set<User> users = Stream.generate(DataGenerator::createRandomUser)
                .limit(new Random().nextInt(10)+1)//to avoid 0
                .collect(Collectors.toSet());
        Set<String> usernames = users.parallelStream().map(User::getUsername).collect(Collectors.toSet());
        meeting.setAttendees(users);
        assertEquals(meeting.getAttendeeUsernames(), usernames);
    }

    @Test
    public void testDefaultConstructor(){
        new Meeting();//implied assertion that there is no exception thrown.
    }
}