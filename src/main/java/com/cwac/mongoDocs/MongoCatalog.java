package com.cwac.mongoDocs;

/**
 * Created by David on 10/26/2015.
 * Utility class just to store some common strings. There should not be any logic here. Perhaps move to config?
 */
public class MongoCatalog {
    public static final class Databases{
        public static final String CWAC = "cwac";
    }

    public static final class Collections{
        public static final String
            USERS = "users",
            FAILED_PAIRING_ATTEMPTS = "failedPairingAttempts",
            MEETINGS = "meetings";
    }

    public static final class Packages{
        public static final String
            CWAC_DOCS = "com.cwac.mongoDocs";
    }
}
