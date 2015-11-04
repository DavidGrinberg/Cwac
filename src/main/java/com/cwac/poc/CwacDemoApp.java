package com.cwac.poc;

import com.cwac.meetings.Proposal;
import com.cwac.meetings.ProposalGenerator;
import com.cwac.mongoDocs.MongoCatalog;
import com.mongodb.MongoBulkWriteException;
import com.mongodb.MongoClient;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by David on 10/25/2015.
 */
public class CwacDemoApp {
    public static void main(String[] args) {
        final Morphia morphia = new Morphia();
        morphia.mapPackage(MongoCatalog.Packages.CWAC_DOCS);
        final AdvancedDatastore cwacDb = (AdvancedDatastore) morphia.createDatastore(new MongoClient(), MongoCatalog.Databases.CWAC);

        boolean createdUsers = false;
        while(!createdUsers){
            try {
                cwacDb.getDB().dropDatabase();
                Proposal proposal = MockDataGenerator.createData();
                cwacDb.save(proposal.meetings);
                cwacDb.save(proposal.users);
                cwacDb.save("FailedUsedTest", proposal.users);
                createdUsers = true;
                System.out.println("Added users");
            } catch (MongoBulkWriteException e) {
                System.out.println("Failed to create users probably due to to duplicate keys");
            }
        }

        ProposalGenerator.generate(cwacDb);
        System.out.println("Done");
    }
}
