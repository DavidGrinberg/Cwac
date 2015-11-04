package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import com.cwac.testing.util.DataGenerator;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by David on 11/3/2015.
 */
public class ProposalTest {

    @Test
    public void testProposalConstructor() {
        Set<User> randomUsers = DataGenerator.createRandomUsers(100);
        Set<Meeting> randomMeetings = DataGenerator.createRandomMeetings(10);
        randomUsers.forEach(user -> user.setFoundMeeting(Math.random() > .5));
        Set<FailedMatch> failedMatches = randomUsers.stream()
                .filter(user -> !user.hasFoundMeeting())
                .map(FailedMatch::new)
                .collect(Collectors.toSet());

        Proposal proposal = new Proposal(randomMeetings, randomUsers);


        assertEquals(proposal.users, randomUsers);
        assertEquals(proposal.meetings, randomMeetings);
        testIfFailedMatchesAreEqual(proposal.failedMatches, failedMatches);
    }

    /* Equality for FailedMatches is tested by comparing the object Ids. Since the Proposal constructor creates new
     * instances of FailedMatches, they get their own ObjectId and there is no way to predict them. Therefore the
     * FailedMatches are being tested for equality in this terrible O(n^2) way.
     */
    private void testIfFailedMatchesAreEqual(Set<FailedMatch> failedMatchesLeft, Set<FailedMatch> failedMatchesRight) {
        for (FailedMatch failedMatchLeft : failedMatchesLeft) {
            boolean foundFailedMatch = false;
            for (FailedMatch failedMatchRight : failedMatchesRight) {
                if (compareFailedMatches(failedMatchLeft, failedMatchRight)) {
                    foundFailedMatch = true;
                    break;
                }
            }
            assertTrue(foundFailedMatch);
        }
        assertTrue(true);
    }

    private boolean compareFailedMatches(FailedMatch failedMatchLeft, FailedMatch failedMatchRight) {
        return failedMatchLeft.getUsername().equals(failedMatchRight.getUsername())
                && failedMatchLeft.getFailureReason().equals(failedMatchRight.getFailureReason())
                && failedMatchLeft.getLocation().equals(failedMatchRight.getLocation())
                //since the objects are constructed at slightly different times we only really care if the day is the same
                && DateUtils.isSameDay(failedMatchLeft.getFailureDate(), failedMatchRight.getFailureDate());

    }
}