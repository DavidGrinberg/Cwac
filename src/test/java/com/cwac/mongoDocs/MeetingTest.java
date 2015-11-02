package com.cwac.mongoDocs;

import com.cwac.testing.util.DataGenerator;
import com.cwac.testing.util.FieldAccessTester;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by David on 11/2/2015.
 */
public class MeetingTest {
    @Test
    public void testGettersAndSetters() throws Exception{
        User user = DataGenerator.createRandomUser();
        Meeting meeting = new Meeting(new HashSet<>(Arrays.asList(user)), "testLocation");
        Map<String, String> methodRenaming = new HashMap<>();
        methodRenaming.put("getOccurred", "hasOccurred");
        FieldAccessTester fieldAccessTester = new FieldAccessTester(meeting).setTestSetters(false)
                .setAccessMethodRenaming(methodRenaming);

        fieldAccessTester.run();
    }
}