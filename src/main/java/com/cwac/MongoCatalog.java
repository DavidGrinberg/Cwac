package com.cwac;

/**
 * Created by David on 10/26/2015.
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
