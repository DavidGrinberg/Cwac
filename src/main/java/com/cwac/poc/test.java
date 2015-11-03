package com.cwac.poc;

import com.cwac.mongoDocs.Meeting;
import com.cwac.mongoDocs.User;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by David on 10/24/2015.
 */
public class test {
    public static void main(String[] args) {
        String[] strings = {"a", "b", "c"};
        Set<String> stringSet = Arrays.stream(strings).skip(1).collect(Collectors.toSet());
        System.out.println(stringSet);
    }
}
