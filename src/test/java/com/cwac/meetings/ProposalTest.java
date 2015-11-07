package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import com.cwac.testing.util.DataGenerator;
import org.junit.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Created by David on 11/3/2015.
 */
public class ProposalTest {

    @Test
    public void testProposalConstructor() {
        Set<User> randomUsers = DataGenerator.createRandomUsers(100);
        Set<Meeting> randomMeetings = DataGenerator.createRandomMeetings(10);
        randomUsers.forEach(user -> user.setFoundMeeting(Math.random() > .5));
        Set<FailedMatch> failedMatches = randomUsers.parallelStream()
                .filter(user -> !user.hasFoundMeeting())
                .map(FailedMatch::new)
                .collect(Collectors.toSet());

        Proposal proposal = new Proposal(randomMeetings, failedMatches);

        assertEquals(proposal.meetings, randomMeetings);
        assertEquals(proposal.failedMatches, failedMatches);
    }
}