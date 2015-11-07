package com.cwac.poc;

import com.cwac.meetings.Proposal;
import com.cwac.meetings.ProposalGenerator;
import com.cwac.mongoDocs.MongoCatalog;
import com.cwac.mongoDocs.User;
import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoClient;
import org.mongodb.morphia.AdvancedDatastore;
import org.mongodb.morphia.Morphia;

/**
 * Created by David on 10/25/2015.
 */
public class CwacDemoApp {
    public static void main(String[] args) {
        final Morphia morphia = new Morphia();
        morphia.getMapper().getOptions().setStoreEmpties(true);
        morphia.mapPackage(MongoCatalog.Packages.CWAC_DOCS);
        final AdvancedDatastore cwacDb = (AdvancedDatastore) morphia.createDatastore(new MongoClient(), MongoCatalog.Databases.CWAC);

        boolean createdUsers = false;
        while(!createdUsers){
            try {
                cwacDb.getDB().dropDatabase();
                MockDataGenerator.createDataIn(cwacDb);
                createdUsers = true;
                cwacDb.ensureIndex(User.class, "location");
                System.out.println("Database Prepped");
            } catch (DuplicateKeyException e) {
                System.out.println("Failed to create users probably due to to duplicate keys: " + e.getMessage());
            }
        }


        ProposalGenerator proposalGenerator = new ProposalGenerator(cwacDb);
        final Proposal proposal = proposalGenerator.proposeMeetings();

        proposal.acceptAndSave(cwacDb);
        System.out.println("Done");
    }
}
