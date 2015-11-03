package com.cwac.mongoDocs;

import com.cwac.testing.util.DataGenerator;
import com.cwac.testing.util.FieldAccessTester;
import org.junit.Test;

/**
 * Created by David on 11/1/2015.
 */
public class FailedMatchTest {
    @Test
    public void testGettersAndSetters() throws Exception{
        User user = DataGenerator.createRandomUser();
        FailedMatch failedMatch = new FailedMatch(user);
        FieldAccessTester fieldAccessTester = new FieldAccessTester(failedMatch).setTestSetters(false);
        fieldAccessTester.run();
    }
}