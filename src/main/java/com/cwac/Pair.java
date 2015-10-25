package com.cwac;

import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.*;
import java.util.Date;
import java.util.List;

/**
 * Created by David on 10/24/2015.
 */
public class Pair extends Document{
    Pair(String firstUser, String secondUser, String location, Date date){
        this.append("_id", new ObjectId())
                .append("firstUser", firstUser)
                .append("secondUser", secondUser)
                .append("location", location)
                .append("date", date)
                .append("occured", true);
    }

    Pair(String firstUser, String secondUser, String location, Date date, boolean occured) {
        this(firstUser, secondUser, location, date);
        this.append("occured", occured);
    }

    public String getFirstUser(){
        return this.getString("firstUser");
    }

    public String getSecondUser(){
        return this.getString("secondUser");
    }

    public List<String> getUsers(){
        return Arrays.asList(this.getFirstUser(), this.getSecondUser());
    }
}
