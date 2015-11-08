package com.cwac.meetings;

import com.cwac.mongoDocs.FailedMatch;
import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.MongoCatalog;
import com.cwac.mongoDocs.User;
import com.cwac.testing.util.DataGenerator;
import com.github.fakemongo.Fongo;
import com.github.fakemongo.junit.FongoRule;
import org.apache.commons.collections.CollectionUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Morphia;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by David on 11/3/2015.
 */
public class ProposalTest {

    @Rule
    public FongoRule fongoRule = new FongoRule();

    @Before
    public void before() {
        cleanDatabase();
    }

    @After
    public void after() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        final Fongo fongo = fongoRule.getFongo();
        fongo.dropDatabase(MongoCatalog.Databases.CWAC);
    }

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

    @Test
    public void testAcceptAndSave() {
        AdvancedDatastore fongoCwacDb = initFongo();
        Set<User> randomUsers = DataGenerator.createRandomUsers(100);
        Set<Meeting> expectedMeetings = DataGenerator.createRandomMeetings(10);
        randomUsers.forEach(user -> user.setFoundMeeting(Math.random() > .5));
        Set<FailedMatch> expectedFailedMatches = randomUsers.parallelStream()
                .filter(user -> !user.hasFoundMeeting())
                .map(FailedMatch::new)
                .collect(Collectors.toSet());

        Proposal proposal = new Proposal(expectedMeetings, expectedFailedMatches);
        proposal.acceptAndSave(fongoCwacDb);

        List<FailedMatch> actualFailedMatches = fongoCwacDb.find(FailedMatch.class).asList();
        assertTrue(CollectionUtils.isEqualCollection(expectedFailedMatches, actualFailedMatches));

        List<Meeting> actualMeetings = fongoCwacDb.find(Meeting.class).asList();
        assertTrue(CollectionUtils.isEqualCollection(expectedMeetings, actualMeetings));
    }

    private AdvancedDatastore initFongo() {
        final Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreEmpties(true);
        morphia.mapPackage(MongoCatalog.Packages.CWAC_DOCS);
        return (AdvancedDatastore) morphia.createDatastore(fongoRule.getMongoClient(), MongoCatalog.Databases.CWAC);
    }
}